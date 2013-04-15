package kr.debop4j.data.mongodb.ogm.test.twitter;

import jodd.props.Props;
import kr.debop4j.data.mongodb.ogm.test.model.Twit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;

/**
 * 트윗터를 사용하기 위한 Helper Class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 4. 15. 오후 2:46
 */
@Slf4j
public class Twitters {

    private Twitters() {}

    protected static final String CONSUMER_KEY = "TM6B5JuM29SgAZJVKDCw";
    protected static final String CONSUMER_SECRET = "5bYa9RbRKAEeB57seIdOvbQSKayixrVjenaRm6H5Kk";
    protected static final String ACCESS_TOKEN = "94962170-tukzJswgnZke3DC19S2zJ0P0T6caLQyYDcpwmCL7X";
    protected static final String ACCESS_TOKEN_SECRET = "JWxsZv7Oa2Kbj8BzUw2R6kJlr3rqjd6pnDHBK9NtQ";

    private static ConfigurationBuilder cfgBuilder = null;
    private static Configuration twitterCfg = null;
    private static volatile Twitter twitter = null;
    private static volatile TwitterStream twitterStream = null;

    static {

        Props props = new Props();

        try {
            props.load(new ClassPathResource("twitter.props").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String consumerKey = props.getValue("oauth.consumerKey");
        String consumerSecret = props.getValue("oauth.consumerSecret");
        String accessToken = props.getValue("oauth.accessToken");
        String accessTokenSecret = props.getValue("oauth.accessTokenSecret");

        log.debug("key=[{}], secret=[{}], token=[{}], tokenSecret=[{}]",
                  consumerKey, consumerSecret, accessToken, accessTokenSecret);

        cfgBuilder = new ConfigurationBuilder();
        cfgBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        twitterCfg = cfgBuilder.build();
    }

    public static Configuration getConfiguration() {
        return twitterCfg;
    }

    public synchronized static Twitter getTwitter() {
        if (twitter == null) {
            twitter = new TwitterFactory(getConfiguration()).getInstance();
        }
        return twitter;
    }

    public synchronized static TwitterStream getTwitterStream() {
        if (twitterStream == null)
            twitterStream = new TwitterStreamFactory(getConfiguration()).getInstance();

        return twitterStream;
    }

    public static Twit createTwit(Status status) {
        Twit twit = new Twit();
        twit.setId(status.getId());
        twit.setUsername(status.getUser().getName());
        twit.setText(status.getText());
        twit.setCreatedAt(status.getCreatedAt());
        return twit;
    }

    protected static String getUserAsString(twitter4j.User user) {
        StringBuilder sb = new StringBuilder();
        return sb.append("id=").append(user.getId()).append(",")
                .append("name=").append(user.getName()).append(",")
                .append("screenName=").append(user.getScreenName()).append(",")
                .append("description=").append(user.getDescription())
                .toString();
    }
}
