/*
 * Copyright 2011-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.analysis.kr;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.kr.morph.*;
import org.apache.lucene.analysis.kr.utils.DictionaryUtil;
import org.apache.lucene.analysis.kr.utils.HanjaUtils;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.util.*;

public class KoreanFilter extends TokenFilter {

    private LinkedList<String> morphQueue;

    private MorphAnalyzer morph;

    private WordSpaceAnalyzer wsAnal;

    private boolean bigrammable = true;

    private boolean hasOrigin = true;

    private boolean exactMatch = false;

    private char[] curTermBuffer;

    private int curTermLength;

    private String curType;

    private String curSource;

    private int tokStart;

    private CompoundNounAnalyzer cnAnalyzer = new CompoundNounAnalyzer();

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

    private static final String APOSTROPHE_TYPE = ClassicTokenizer.TOKEN_TYPES[ClassicTokenizer.APOSTROPHE];
    private static final String ACRONYM_TYPE = ClassicTokenizer.TOKEN_TYPES[ClassicTokenizer.ACRONYM];

    public KoreanFilter(TokenStream input) {
        super(input);
        morphQueue = new LinkedList();
        morph = new MorphAnalyzer();
        wsAnal = new WordSpaceAnalyzer();
        cnAnalyzer.setExactMach(false);
    }

    /**
     * @param input  input token stream
     * @param bigram Whether the bigram index term return or not.
     */
    public KoreanFilter(TokenStream input, boolean bigram) {
        this(input);
        bigrammable = bigram;
    }

    public KoreanFilter(TokenStream input, boolean bigram, boolean has) {
        this(input, bigram);
        hasOrigin = has;
    }

    public KoreanFilter(TokenStream input, boolean bigram, boolean has, boolean match) {
        this(input, bigram, has);
        this.exactMatch = match;
    }

    public final boolean incrementToken() throws IOException {
        if (curTermBuffer != null && morphQueue.size() > 0) {
            setTermBufferByQueue();
            return true;
        }

        if (!input.incrementToken()) {
            return false;
        }

        curTermBuffer = termAtt.buffer().clone();
        curTermLength = termAtt.length();
        tokStart = offsetAtt.startOffset();
        curType = typeAtt.type();

        try {
            if (KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.KOREAN].equals(curType)) {
                analysisKorean(new String(curTermBuffer, 0, termAtt.length()));
            } else if (KoreanTokenizer.TOKEN_TYPES[KoreanTokenizer.CHINESE].equals(curType)) {
                analysisChinese(new String(curTermBuffer, 0, termAtt.length()));
            } else {
                analysisETC(new String(curTermBuffer, 0, termAtt.length()));
            }
        } catch (MorphException e) {
            throw new IOException("Korean Filter MorphException\n" + e.getMessage());
        }

        if (morphQueue == null || morphQueue.size() == 0) {
            return true;
        }

        setTermBufferByQueue();

        return true;

    }

    /**
     * queue 에 저장된 값으로 buffer의 값을 복사한다.
     */
    private void setTermBufferByQueue() {
        clearAttributes();
        String term = morphQueue.removeFirst();
        int pos = new String(curTermBuffer).indexOf(term);
        termAtt.copyBuffer(term.toCharArray(), 0, term.length());
        offsetAtt.setOffset(tokStart + pos, tokStart + pos + term.length());
    }

    /**
     * 한글을 분석한다.
     *
     * @param token
     * @param skipinc
     * @return
     * @throws org.apache.lucene.analysis.kr.morph.MorphException
     *
     */
    private void analysisKorean(String input) throws MorphException {
        List<AnalysisOutput> outputs = morph.analyze(input);
        if (outputs.size() == 0) return;

        Map<String, Integer> map = new LinkedHashMap();
        if (hasOrigin) map.put(input, new Integer(1));

        if (outputs.get(0).getScore() == AnalysisOutput.SCORE_CORRECT) {
            extractKeyword(outputs, map);
        } else {
            try {
                List<AnalysisOutput> list = wsAnal.analyze(input);

                List<AnalysisOutput> results = new ArrayList();
                if (list.size() > 1) {
                    for (AnalysisOutput o : list) {
                        if (hasOrigin) map.put(o.getSource(), new Integer(1));
                        results.addAll(morph.analyze(o.getSource()));
                    }
                } else {
                    results.addAll(list);
                }

                extractKeyword(results, map);
            } catch (Exception e) {
                extractKeyword(outputs, map);
            }
        }

        Iterator<String> iter = map.keySet().iterator();

        int i = 0;
        while (iter.hasNext()) {
            String text = iter.next();
            if (text.length() <= 1) continue;
            morphQueue.add(text);
        }

    }

    private void extractKeyword(List<AnalysisOutput> outputs, Map<String, Integer> map) throws MorphException {
        for (AnalysisOutput output : outputs) {

            if (output.getPos() != PatternConstants.POS_VERB) {
                map.put(output.getStem(), new Integer(1));
//			}else {
//				map.put(output.getStem()+"다", new Integer(1));	
            }
            if (exactMatch) continue;

            if (output.getScore() >= AnalysisOutput.SCORE_COMPOUNDS) {
                List<CompoundEntry> cnouns = output.getCNounList();
                for (int jj = 0; jj < cnouns.size(); jj++) {
                    CompoundEntry cnoun = cnouns.get(jj);
                    if (cnoun.getWord().length() > 1) map.put(cnoun.getWord(), new Integer(0));
                    if (jj == 0 && cnoun.getWord().length() == 1)
                        map.put(cnoun.getWord() + cnouns.get(jj + 1).getWord(), new Integer(0));
                    else if (jj > 1 && cnoun.getWord().length() == 1)
                        map.put(cnouns.get(jj).getWord() + cnoun.getWord(), new Integer(0));
                }

            } else if (bigrammable) {
                addBiagramToMap(output.getStem(), map);
            }

        }

    }

    private void addBiagramToMap(String input, Map map) {
        int offset = 0;
        int strlen = input.length();
        while (offset < strlen - 1) {
            if (isAlphaNumChar(input.charAt(offset))) {
                String text = findAlphaNumeric(input.substring(offset));
                map.put(text, new Integer(0));
                offset += text.length();
            } else {
                String text = input.substring(offset,
                                              offset + 2 > strlen ? strlen : offset + 2);
                map.put(text, new Integer(0));
                offset++;
            }
        }
    }

    private String findAlphaNumeric(String text) {
        int pos = 0;
        for (int i = 0; i < text.length(); i++) {
            if (!isAlphaNumChar(text.charAt(i))) break;
            pos++;
        }
        return text.substring(0, pos);
    }

    /**
     * 한자는 2개이상의 한글 음으로 읽혀질 수 있다.
     * 두음법칙이 아님.
     *
     * @param term
     * @throws org.apache.lucene.analysis.kr.morph.MorphException
     *
     */
    private void analysisChinese(String term) throws MorphException {
        morphQueue.add(term);
        if (term.length() < 2) return; // 1글자 한자는 색인어로 한글을 추출하지 않는다.

        List<StringBuffer> candiList = new ArrayList();
        candiList.add(new StringBuffer());

        for (int i = 0; i < term.length(); i++) {

            char[] chs = HanjaUtils.convertToHangul(term.charAt(i));
            if (chs == null) continue;

            int caniSize = candiList.size();
            for (int j = 0; j < caniSize; j++) {
                StringBuffer sb = candiList.get(j);

                for (int k = 1; k < chs.length; k++) { // 추가로 생성된 음에 대해서 새로운 텍스트를 생성한다.
                    if (k == 1) break; // 2개 이상의 음을 가지고 있는 경우 첫번째 음으로만 처리를 한다.
                    StringBuffer cpSb = new StringBuffer(sb);
                    cpSb.append(chs[k]);

                    candiList.add(cpSb);
                }

                sb.append(chs[0]);
            }
        }

        int maxCandidate = 5;
        if (candiList.size() < maxCandidate) maxCandidate = candiList.size();

        for (int i = 0; i < maxCandidate; i++) {
            morphQueue.add(candiList.get(i).toString());
        }

        Map<String, String> cnounMap = new HashMap();

        // 추출된 명사가 복합명사인 경우 분리한다.
        for (int i = 0; i < maxCandidate; i++) {
            List<CompoundEntry> results = confirmCNoun(candiList.get(i).toString());

            int pos = 0;
            for (CompoundEntry entry : results) {
                pos += entry.getWord().length();
                if (cnounMap.get(entry.getWord()) != null) continue;

                // 한글과 매치되는 한자를 짤라서 큐에 저장한다.
                morphQueue.add(term.substring(pos - entry.getWord().length(), pos));

                cnounMap.put(entry.getWord(), entry.getWord());

                if (entry.getWord().length() < 2) continue; //  한글은 2글자 이상만 저장한다.

                // 분리된 한글을 큐에 저장한다.
                morphQueue.add(entry.getWord());
            }
        }
    }

    private List confirmCNoun(String input) throws MorphException {
        WordEntry cnoun = DictionaryUtil.getCNoun(input);
        if (cnoun != null && cnoun.getFeature(WordEntry.IDX_NOUN) == '2') {
            return cnoun.getCompounds();
        }

        return cnAnalyzer.analyze(input);

    }

    private void analysisETC(String term) throws MorphException {
        final char[] buffer = termAtt.buffer();
        final int bufferLength = termAtt.length();
        final String type = typeAtt.type();

        if (type == APOSTROPHE_TYPE &&      // remove 's
                bufferLength >= 2 &&
                buffer[bufferLength - 2] == '\'' &&
                (buffer[bufferLength - 1] == 's' || buffer[bufferLength - 1] == 'S')) {
            // Strip last 2 characters off
            morphQueue.add(term.substring(0, bufferLength - 2));
        } else if (type == ACRONYM_TYPE) {      // remove dots
            int upto = 0;
            for (int i = 0; i < bufferLength; i++) {
                char c = buffer[i];
                if (c != '.')
                    buffer[upto++] = c;
            }
            morphQueue.add(term.substring(0, upto));
        } else {
            morphQueue.add(term);
        }

    }

    private boolean isAlphaNumChar(int c) {
        if ((c >= 48 && c <= 57) || (c >= 65 && c <= 122)) return true;
        return false;
    }

    public void setHasOrigin(boolean has) {
        hasOrigin = has;
    }

    public void setExactMatch(boolean match) {
        this.exactMatch = match;
    }
}
