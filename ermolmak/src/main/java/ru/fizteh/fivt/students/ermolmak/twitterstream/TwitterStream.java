package ru.fizteh.fivt.students.ermolmak.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.JSONException;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.List;

public class TwitterStream {
    public static void main(String[] args) throws TwitterException, IOException, JSONException {
        Parser parser = new Parser();
        JCommander jCommander = new JCommander(parser, args);
        String stringQuery = parser.getQuery();

        if (parser.isHelp()) {
            jCommander.usage();
            return;
        }

        String place = parser.getPlace();
        Area area = null;
        if (place != null) {
            GeoResolver geoResolver = new GeoResolver();
            area = geoResolver.findPlace(place);
        }

        SearchTweets search = new SearchTweets();
        if (!parser.isStream()) {

            List<String> tweets = search.searchTweets(
                    stringQuery,
                    area,
                    parser.isHideRetweets(),
                    parser.getLimit());

            for (String tweet : tweets) {
                System.out.println(tweet);
                System.out.println();
            }
//        } else {
//            search.stream(
//                    stringQuery,
//                    area,
//                    parser.isHideRetweets(),
//                    System.out::print,
//                    (new TwitterStreamFactory()).getInstance());
        }
    }
}
