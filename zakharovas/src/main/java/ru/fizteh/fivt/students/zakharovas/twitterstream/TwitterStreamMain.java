package ru.fizteh.fivt.students.zakharovas.twitterstream;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;

public class TwitterStreamMain {

    private static GeoLocator geoLocator;

    public static void main(String[] args) {
        String[] separatedArgs = ArgumentSeparator.separateArguments(args);
        CommandLineArgs commandLineArgs = new CommandLineArgs();
        JCommander jCommander = null;
        try {
            jCommander = new JCommander(commandLineArgs, separatedArgs);
        } catch (ParameterException pe) {
            System.err.println("Problems with parametes " + pe.getMessage());
            System.exit(1);
        }
        try {
            checkArguments(commandLineArgs, jCommander);
        } catch (ParameterException pe) {
            System.err.println("Problems with parametes " + pe.getMessage());
            System.exit(1);
        }
        if (commandLineArgs.getHelp()) {
            helpMode(jCommander);
            System.exit(0);
        } else {
            try {
                geoLocator = new GeoLocator(commandLineArgs.getLocation());
            } catch (Exception e) {
                System.err.println("Problems with geolocation " + e.getMessage());
                System.exit(1);
            }
            if (commandLineArgs.getStringForQuery().isEmpty()) {
                System.err.println("Empty search");
                System.exit(1);
            }
            if (commandLineArgs.getStreamMode()) {
                streamMode(commandLineArgs);
            } else {
                try {
                    searchMode(commandLineArgs);
                } catch (TwitterException | IllegalStateException e) {
                    System.err.println("Problems with search mode " + e.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    private static void streamMode(CommandLineArgs commandLineArgs) {
        twitter4j.TwitterStream twitterStream =
                TwitterStreamFactory.getSingleton();
        new TwitterStreamMode(twitterStream, commandLineArgs, geoLocator).startStreaming();
    }

    private static void searchMode(CommandLineArgs commandLineArgs) throws TwitterException, IllegalStateException {
        Twitter twitter = TwitterFactory.getSingleton();
        new TwitterSearchMode(twitter, commandLineArgs, geoLocator).search();

    }

    private static void helpMode(JCommander jCommander) {
        jCommander.usage();
    }


    private static void checkArguments(CommandLineArgs commandLineArgs,
                                       JCommander jCommander) throws ParameterException {
        if (commandLineArgs.getStreamMode()
                && commandLineArgs.getLimit()
                != commandLineArgs.DEFAULT_LIMIT) {
            jCommander.usage();
            ParameterException parameterException = new ParameterException("Stream mode does not have limits");
            throw parameterException;
        }

    }
}
