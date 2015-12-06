package ru.fizteh.fivt.students.ermolmak.twitterstream;

import twitter4j.*;

import java.util.*;

public class SearchTweets {
    private static final long MILLIS_IN_SECOND = 1000L;
    private static final long MILLIS_IN_MINUTE = 60_000L;
    private static final long MILLIS_IN_HOUR = 3_600_000L;
    private static final long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

    List<String> searchTweets(String stringQuery,
                              String place,
                              boolean hideRetweets,
                              int limit) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query(stringQuery);

        List<String> tweets = new ArrayList<>();
        while (tweets.size() < limit && query != null) {
            QueryResult queryResult = twitter.search(query);
            for (Status status : queryResult.getTweets()) {
                if (tweets.size() == limit) {
                    break;
                }

                if (status.isRetweet()) {
                    if (hideRetweets) {
                        continue;
                    }

                    tweets.add(getTweetTime(status.getCreatedAt())
                            + " @" + status.getUser().getName()
                            + ": ретвитнул @" + status.getRetweetedStatus().getUser().getName()
                            + ": " + status.getText());
                } else {
                    if (status.getRetweetCount() > 0) {
                        tweets.add(getTweetTime(status.getCreatedAt())
                                + " @" + status.getUser().getName()
                                + ": " + status.getText()
                                + (" (" + status.getRetweetCount() + " ретвитов)"));
                    } else {
                        tweets.add(getTweetTime(status.getCreatedAt())
                                + " @" + status.getUser().getName()
                                + ": " + status.getText());
                    }
                }
            }
            query = queryResult.nextQuery();
        }

        return tweets;
    }

    private String getTweetTime(Date date) {
        Calendar now = new GregorianCalendar();
        Calendar tweetTime = new GregorianCalendar.Builder().setInstant(date).build();

        long milliseconds = now.getTimeInMillis() - tweetTime.getTimeInMillis();

        boolean isToday = now.get(Calendar.DATE) == tweetTime.get(Calendar.DATE)
                && milliseconds < MILLIS_IN_DAY;

        String result;
        if (isToday) {
            if (milliseconds < 2 * MILLIS_IN_MINUTE) {
                result = "Только что";
            } else if (milliseconds < MILLIS_IN_HOUR) {
                result = milliseconds / MILLIS_IN_MINUTE + " минут назад";
            } else {
                result = milliseconds / MILLIS_IN_HOUR + " часов назад";
            }
        } else {
            tweetTime.set(Calendar.HOUR_OF_DAY, 0);
            tweetTime.set(Calendar.MINUTE, 0);
            tweetTime.set(Calendar.SECOND, 0);
            tweetTime.set(Calendar.MILLISECOND, 0);

            now.set(Calendar.HOUR_OF_DAY, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);

            long days = (now.getTimeInMillis() - tweetTime.getTimeInMillis()) / MILLIS_IN_DAY;

            if (days == 1) {
                result = "Вчера";
            } else {
                result = days + " дней назад";
            }
        }

        return result;
    }
}
