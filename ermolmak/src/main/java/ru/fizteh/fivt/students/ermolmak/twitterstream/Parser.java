package ru.fizteh.fivt.students.ermolmak.twitterstream;

import com.beust.jcommander.Parameter;

public class Parser {
    @Parameter(names = {"--query", "-q"}, description = "Keywords")
    private String query = "";

    @Parameter(names = {"--place", "-p"}, description = "Places for search")
    private String place = "";

    @Parameter(names = {"--stream", "-s"}, description = "Stream mode")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Hide Retweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"--limit", "-l"})
    private int limit = 10;

    @Parameter(names = {"--help", "-h"})
    private boolean help = false;

    public String getQuery() {
        return query;
    }

    public String getPlace() {
        return place;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean isHideRetweets() {
        return hideRetweets;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }
}
