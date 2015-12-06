package ru.fizteh.fivt.students.ermolmak.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.TwitterException;

import java.util.List;

public class TwitterStream {
    public static void main(String[] args) throws TwitterException {
        Parser parser = new Parser();
        JCommander jCommander = new JCommander(parser, args);
        String stringQuery = parser.getQuery();

        if (parser.isHelp()) {
            jCommander.usage();
            return;
        }

        SearchTweets search = new SearchTweets();

        List<String> tweets = search.searchTweets(
                stringQuery,
                parser.getPlace(),
                parser.isHideRetweets(),
                parser.getLimit());

        for (String tweet : tweets) {
            System.out.println(tweet);
            System.out.println();
        }
    }
}
