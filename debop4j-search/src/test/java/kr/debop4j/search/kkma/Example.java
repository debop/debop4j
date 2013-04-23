package kr.debop4j.search.kkma;

import kr.debop4j.core.AutoStopwatch;
import kr.debop4j.core.parallelism.Parallels;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.snu.ids.ha.index.Keyword;
import org.snu.ids.ha.index.KeywordExtractor;
import org.snu.ids.ha.index.KeywordList;
import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.util.Timer;

import java.util.List;

/**
 * kr.debop4j.search.kkma.Example
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 28.
 */
@Slf4j
public class Example {

    private static final String str = "저는 대학생이구요. 소프트웨어 관련학과입니다. DB는 수업을 한번 들은 적이 있으며, 수학은 대학에서 통계학, 선형대수학, 이산수학, 대학수학 등을 배웠지만... 자주 사용을 안하다보니 모두 까먹은 상태입니다." +
            "전관예우, 병역면제, 증여세탈루 및 부동산 투기 등 의혹\n" +
            "국가보안법 폐지 반대, '5·16혁명' 가치관도 논란\n" +
            "\n" +
            "【서울=뉴시스】박준호 기자 = 황교안 법무부 장관 후보자에 대한 국회 법사위원회 인사청문회가 28일 국회에서 열린다.\n" +
            "\n" +
            "황 후보자가 험로가 예고되는 인사청문회를 넘기 위해서는 이중 소득공제, 증여세탈루 및 부동산 투기 의혹, 전관예우 논란, 국가보안법에 대한 가치관 및 종교 편향 논란 등에 대해 적절한 해명이 필요할 것으로 보인다.\n" +
            "\n" +
            "\n" +
            "우선 황 후보자는 부동산 투기 논란에 휩싸여 있다.\n" +
            "\n" +
            "황 후보자의 부인은 99년 10월 용인시 수지구 성복동의 아파트(전용면적 164.24㎡)를 3억8000여만원에 분양받아 전세를 준 뒤, 지금껏 서울 서초구 잠원동 아파트에서 거주해온 것으로 드러났다.\n" +
            "\n" +
            "용인 수지 지역은 한때 아파트 값이 급등하면서 서울 강남3구와 함께 '버블세븐' 지역으로 분류돼 황 후보자가 투기 목적으로 아파트를 매입한 의혹이 제기됐다.\n" +
            "\n" +
            "이에 대해 황 후보자는 \"지방에 사는 처남 대신 장인·장모를 자주 찾아뵙기 위해 아파트를 구입한 것\"이라며 \"자녀들이 모두 서울 강북 지역 대학에 진학하는 바람에 통학거리가 길어 아직 이사를 하지 못했다\"고 해명했다.\n" +
            "\n" +
            "아파트 시세가 9억원 상당에서 현재 4억6000만~5억2000만원으로 떨어졌지만 큰 폭의 가격 변동에도 아파트를 매각하지 않고 계속 보유한 것도 시세 차익 의도가 없다는 점을 반증한다고 법무부는 설명했다.\n" +
            "\n" +
            "소득세법 위반(이중 공제) 의혹도 논란의 대상이다.\n" +
            "\n" +
            "황 후보자는 2008년치 연말정산에서 배우자에 대한 부양가족 기본공제신청을 했지만 당시 신학대학에 재직해 연소득 700만원이 넘는 배우자 역시 이미 본인 몫의 기본공제를 신청해 이중공제를 받았다.\n" +
            "\n" +
            "소득금액 100만원 이상(근로소득연봉 700만원 이상)일 경우 부양가족 공제를 신청할 수 없는 소득세법 규정을 위반한 것이다.\n" +
            "\n" +
            "황 후보자는 연말정산 과정에서 서류작성을 담당한 담당 직원의 착오를 뒤늦게 발견하고 몇 달 후 환급했다고 해명했다.\n" +
            "\n" +
            "아들에 대한 증여세 탈루 의혹도 쟁점 중 하나다.\n" +
            "\n" +
            "황 후보자의 장남은 2012년 8월 서울 잠원동 아파트(76.3㎡) 전세를 3억원에 계약했지만 그에 대한 증여세 납부나 채무관계는 인사청문요청안에 포함돼있지 않아 전세 자금을 불법 증여한 의혹이 제기됐다.\n" +
            "\n" +
            "현행 증여세법에 의하면 직계존속간 증여도 성인인 경우 3000만원 이내의 경우만 증여재산공제가 되어 황 후보자가 장남에게 증여를 했다면 2억7000만에 대한 증여세납부기록이 있어야 한다.\n" +
            "\n" +
            "이에 대해 황 후보자는 \"당시 결혼을 앞두고 있던 아들의 자립을 위해 교육 차원에서 3억원을 대여하고 차용증을 작성했다\"며 \"올해 2월까지 통장으로 매달 이자를 받아왔다\"고 의혹을 부인했다.\n" +
            "\n" +
            "최근 법조계를 중심으로 문제가 되고 있는 '전관예우' 논란도 비켜갈 수 없을 전망이다.\n" +
            "\n" +
            "황 후보자는 부산고검장을 끝으로 검찰 퇴임 후 2009년 9월~2013년 1월 태평양 법무법인에서 근무하며 15억9040만여원의 보수를 받아 전관예우 논란이 강하게 일고 있다.\n" +
            "\n" +
            "이와 함께 병역 면제 및 논문 특헤 의혹도 받고 있다.\n" +
            "\n" +
            "황 후보자는 1980년 7월 징병검사에서 일종의 두드러기 증상인 만성담마진(慢性蕁麻疹)으로 징집면제(제2국민역) 처분을 받은 것으로 확인됐다.\n" +
            "\n" +
            "신체검사를 3차례에 걸쳐 연기하고, 피부질환을 이유로 군복무 면제 판정을 받은 것을 놓고 병역의무를 회피하기 위해 편법을 부린 게 아니냐는 의혹이 제기됐지만, 황 후보자는 당시 '징병 신체검사 등 검사규칙'에 따라 '고도(신체검사 평가기준: 3급)'에 해당돼 적법한 면제 판정을 받은 것이라는 입장이다.\n" +
            "\n" +
            "또 황 후보자는 1995년 성균관대학원 수료 이후 2005년 10월 석사학위 논문을 제출하고 같은해 12월 논문심사를 통과해 석사하위를 취득했다.\n" +
            "\n" +
            "이 과정에서 황 후보자가 석사논문 제출기한을 어겨 특혜 의혹이 제기됐지만 황 후보자는 학칙에 따라 종합·외국어시험에 각각 재응시해 논문을 제출할 수 있는 자격을 얻었다고 해명했다.\n" +
            "\n" +
            "독실한 기독교 신자로 알려진 황 후보자에 대해 특정 종교 편향 우려도 일고 있지만, 황 후보자는 \"우리나라가 종교의 자유를 상당한 정도로 보장하고 있으므로 국가의 법질서를 존중하는 범주 안에서 종교생활과 신앙생활을 해야 한다\"는 게 기본 철학이라고 설명했다.\n" +
            "\n" +
            "이밖에 2009년 출간한 '집시법 해설'에서 5·16 쿠데타를 '혁명'으로 표현한 것과 통일 이후에도 국가보안법 필요성을 고수하는 입장에 대해 야권에서 문제제기할 것으로 예상된다.\n" +
            "\n" +
            "지난 10년간 본인 명의의 차량 2대로 교통법규를 3차례 위반하고도 과태료를 제때 내지 않은 점, 지방세와 자동차세도 각각 1차례씩 연체한 점도 법무부 장관으로서의 준법정신과 기본 자질에 의문이 제기될 수 있다.\n" +
            "\n" +
            "2007년 법무부 근무 당시 경기고 동창인 노회찬 전 진보정의당 의원에게 정치 후원금 10만원을 기부한 점도 공무원의 정치적 중립성 훼손 논란을 불러일으킬 수 있다.\n" +
            "\n" +
            "검사 시절 강정구 동국대 교수의 국가보안법 위반 사건, '안기부 X파일' 사건 등도 수사결과를 놓고 야당의 집중 공세를 피할 수 없을 것으로 보인다.";

    @Test
    public void kkma_test() {

        try {
            MorphemeAnalyzer ma = new MorphemeAnalyzer();
            ma.createLogger(null);

            Timer timer = new Timer();
            timer.start();
            List<MExpression> ret = ma.analyze(str);
            timer.stop();
            timer.printMsg("Time");

            ret = ma.postProcess(ret);
            ret = ma.leaveJustBest(ret);

            List<Sentence> stl = ma.divideToSentences(ret);
            for (int i = 0; i < stl.size(); i++) {
                Sentence st = stl.get(i);
                System.out.println("=================================== " + st.getSentence());
                for (int j = 0; j < st.size(); j++) {
                    System.out.println(st.get(j));
                }
            }
            ma.closeLogger();

            Assert.assertTrue(true);
        } catch (Exception e) {
            log.error("예외가 발생했습니다.", e);
        }
    }

    /**
     * 인덱스 추출 -> 이 걸 이용해서 실시간 키워드 및 연관검색도 가능하다.
     */
    @Test
    public void keywordTest() {
        KeywordExtractor ke = new KeywordExtractor();

        final String strToExtrtKwrd = "저는 대학생이구요. 소프트웨어 관련학과 입니다. DB는 수업을 한번 들은 적이 있으며, 수학은 대학에서 통계학, 선형대수학, 이산수학, 대학수학 등을 배웠지만... 자주 사용을 안하다보니 모두 까먹은 상태입니다.";
        for (int k = 0; k < 10; k++) {
            AutoStopwatch stopwatch = new AutoStopwatch();
            KeywordList kl = ke.extractKeyword(strToExtrtKwrd, false);
            for (int i = 0; i < kl.size(); i++) {
                Keyword kwd = kl.get(i);
                log.debug("Keyword=[{}] => [{}]", kwd.getString(), kwd.getCnt());
            }
            stopwatch.close();
        }
    }

    @Test
    public void extractKeywordInParallel() throws Exception {
        final KeywordExtractor ke = new KeywordExtractor();

        final String strToExtrtKwrd = "저는 대학생이구요. 소프트웨어 관련학과 입니다. DB는 수업을 한번 들은 적이 있으며, 수학은 대학에서 통계학, 선형대수학, 이산수학, 대학수학 등을 배웠지만... 자주 사용을 안하다보니 모두 까먹은 상태입니다.";

        Parallels.run(10, new Runnable() {
            @Override
            public void run() {
                AutoStopwatch stopwatch = new AutoStopwatch();
                KeywordList kl = ke.extractKeyword(str, false);
                for (int i = 0; i < kl.size(); i++) {
                    Keyword kwd = kl.get(i);
                    log.debug("Keyword=[{}] => [{}]", kwd.getString(), kwd.getCnt());
                }
                stopwatch.close();
            }
        });
    }
}
