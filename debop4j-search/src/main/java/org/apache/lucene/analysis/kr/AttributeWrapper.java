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

import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class AttributeWrapper {

    private TermAttribute termAtt;
    private OffsetAttribute offsetAtt;
    private PositionIncrementAttribute posIncrAtt;
    private TypeAttribute typeAtt;

    public AttributeWrapper(TermAttribute term, OffsetAttribute offset,
                            PositionIncrementAttribute pos, TypeAttribute type) {
        this.termAtt = term;
        this.offsetAtt = offset;
        this.posIncrAtt = pos;
        this.typeAtt = type;
    }

    public AttributeWrapper() {

    }

    public TermAttribute getTermAtt() {
        return termAtt;
    }

    public void setTermAtt(TermAttribute termAtt) {
        this.termAtt = termAtt;
    }

    public OffsetAttribute getOffsetAtt() {
        return offsetAtt;
    }

    public void setOffsetAtt(OffsetAttribute offsetAtt) {
        this.offsetAtt = offsetAtt;
    }

    public PositionIncrementAttribute getPosIncrAtt() {
        return posIncrAtt;
    }

    public void setPosIncrAtt(PositionIncrementAttribute posIncrAtt) {
        this.posIncrAtt = posIncrAtt;
    }

    public TypeAttribute getTypeAtt() {
        return typeAtt;
    }

    public void setTypeAtt(TypeAttribute typeAtt) {
        this.typeAtt = typeAtt;
    }


}
