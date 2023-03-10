package com.redhat;

import javax.enterprise.context.ApplicationScoped;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TwitterTelegramRoute extends RouteBuilder {

    @ConfigProperty(name="searchterm", defaultValue = "@kevindubois")
    String searchTerm;
        
    @ConfigProperty(name = "twitter.apikey")
    String consumerApiKey;

    @ConfigProperty(name = "twitter.secret")
    String consumerSecret;

    @ConfigProperty(name = "twitter.accesstoken")
    String accessToken;

    @ConfigProperty(name = "twitter.accesstokensecret")
    String accessTokenSecret;

    @ConfigProperty(name = "telegram.token")
    String telegramToken;

    @ConfigProperty(name = "telegram.chatid")
    String telegramChatId;

    int count = 4;

    @Override
    public void configure() throws Exception {

        fromF("twitter-search:%s?count=%s&accessToken=%s&accessTokenSecret=%s&consumerKey=%s&consumerSecret=%s", 
                        searchTerm, count, accessToken, accessTokenSecret, consumerApiKey, consumerSecret)
                .log(LoggingLevel.INFO, "Twitter Search Result: ${body}")
                .process(new TweetInfoProcessor())
                .to("telegram:bots?authorizationToken=" 
                    + telegramToken + "&chatId=" + telegramChatId);
    }

}
