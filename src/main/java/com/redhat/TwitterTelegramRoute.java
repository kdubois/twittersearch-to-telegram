package com.redhat;

import javax.enterprise.context.ApplicationScoped;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.search.TwitterSearchComponent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TwitterTelegramRoute extends RouteBuilder {

    @ConfigProperty(name="searchterm", defaultValue = "#CamelQuarkus")
    String searchTerm;

    int count = 1;

    @Override
    public void configure() throws Exception {

        setTwitterConfig();
               
        fromF("twitter-search://%s?repeatCount=1&count=%s", searchTerm, count)
                .log(LoggingLevel.INFO, "Twitter Search Result: ${body}")
                //.process(new TweetInfoProcessor())
                .to("telegram:bots?authorizationToken=" + telegramToken + "&chatId=" + telegramChatId);
    }








    @ConfigProperty(name = "twitter.apikey")
    String twitterApiKey;

    @ConfigProperty(name = "twitter.secret")
    String twitterSecret;

    @ConfigProperty(name = "twitter.accesstoken")
    String twitterAccessToken;

    @ConfigProperty(name = "twitter.accesstokensecret")
    String twitterAccessTokenSecret;

    @ConfigProperty(name = "telegram.token")
    String telegramToken;

    @ConfigProperty(name = "telegram.chatid")
    String telegramChatId;

    private void setTwitterConfig() {
        // setup Twitter component
        TwitterSearchComponent tc = getContext().getComponent("twitter-search", TwitterSearchComponent.class);
        tc.setAccessToken(twitterAccessToken);
        tc.setAccessTokenSecret(twitterAccessTokenSecret);
        tc.setConsumerKey(twitterApiKey);
        tc.setConsumerSecret(twitterSecret);
    }

}
