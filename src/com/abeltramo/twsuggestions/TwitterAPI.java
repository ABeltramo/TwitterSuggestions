package com.abeltramo.twsuggestions;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * twitter-suggestions
 * Created by ABeltramo on 28/12/16.
 */
public class TwitterAPI {
    private static final int maxTweet = 200;

    public static Twitter configureTwitter(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("kGCmUSjwceYb3HJXu8AlcrX0O")
                .setOAuthConsumerSecret("z4IIwAsIQbw4CNOuN6hANB0GJ941Klzt7pQlowyxiumEp23tTy")
                .setOAuthAccessToken("423542101-xl7YFhTKNytPAYVHXd8xKT38Qv7XOjYSTAEoEmm1")
                .setOAuthAccessTokenSecret("DwM3SCMffkIUW1Pz6tUcjyDlJFxFN7Tk8WYdQgmv03dgZ");
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    public static List<Status> getUserStats(String user){
        Twitter tw = configureTwitter();
        Paging paging = new Paging(1, maxTweet);
        List<Status> statuses = null;
        try {
            statuses = tw.getUserTimeline(user,paging);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statuses;
    }

    public static List<Status> getUserStats(Long user){
        Twitter tw = configureTwitter();
        Paging paging = new Paging(1, maxTweet);
        List<Status> statuses = null;
        try {
            statuses = tw.getUserTimeline(user,paging);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statuses;
    }

    // Friends are those that a user follows and by whom she is followed back.
    public static ArrayList<String> getUserFriends(String user){
        Twitter tw = configureTwitter();
        ArrayList<String> friendsIDs = new ArrayList<>();
        try {
            IDs following = tw.getFriendsIDs(user, -1);             // Get the following list
            IDs followers = tw.getFollowersIDs(user, -1);           // Get the followers list
            for(Long id : following.getIDs()){                         // For each following user
                if(Arrays.stream(followers.getIDs())
                                          .anyMatch(x -> x == id)){    // Check if is also a follower
                    friendsIDs.add(tw.showUser(id).getScreenName()); // Add to the result
                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return friendsIDs;
    }

}
