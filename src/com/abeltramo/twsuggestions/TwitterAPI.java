package com.abeltramo.twsuggestions;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * twitter-suggestions
 * Created by ABeltramo on 28/12/16.
 */
public class TwitterAPI {
    private static final int maxTweet       = 200;        // This should not be changed
    private static final int maxPages       = 10;         // max 2000 tweet per user
    private static final int maxUserPerPage = 200;        // This value is the maximum possible

    public static Twitter configureTwitter(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("kGCmUSjwceYb3HJXu8AlcrX0O")
                .setOAuthConsumerSecret("z4IIwAsIQbw4CNOuN6hANB0GJ941Klzt7pQlowyxiumEp23tTy")
                .setOAuthAccessToken("423542101-xl7YFhTKNytPAYVHXd8xKT38Qv7XOjYSTAEoEmm1")
                .setOAuthAccessTokenSecret("DwM3SCMffkIUW1Pz6tUcjyDlJFxFN7Tk8WYdQgmv03dgZ");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twIstance = tf.getInstance();
        twIstance.addRateLimitStatusListener( new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus( RateLimitStatusEvent event ) {
                //System.out.println("Limit["+event.getRateLimitStatus().getLimit() + "], Remaining[" +event.getRateLimitStatus().getRemaining()+"]");
            }

            @Override
            public void onRateLimitReached( RateLimitStatusEvent event ) {
                MainForm.msgbox("Twitter API limit reached\nWaiting "+ event.getRateLimitStatus().getSecondsUntilReset() +" sec","Twitter API limit reached");
                try {
                    TimeUnit.SECONDS.sleep(event.getRateLimitStatus().getSecondsUntilReset()+15);
                    System.out.println("Restarting...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } );
        return twIstance;
    }

    public static List<Status> getUserStats(String user){
        Twitter tw = configureTwitter();
        List<Status> statuses = new ArrayList<>();
        try {
            int prevStatLength;
            for(int i=1;i<=maxPages;i++) {
                prevStatLength = statuses.size();
                statuses.addAll(tw.getUserTimeline(user, new Paging(i, maxTweet)));
                if(statuses.size() == prevStatLength)                           // if there is no more tweet to crawl
                    break;                                                      // exit
            }
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
            ArrayList<Long> following = getAllFriendsIDs(user,true);             // Get the following list
            ArrayList<Long> followers = getAllFriendsIDs(user,false);            // Get the followers list
            for(Long id : following){                                                   // For each following user
                if(followers.contains(id)){                                             // Check if is also a follower
                    friendsIDs.add(tw.showUser(id).getScreenName());                    // Add to the result
                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return friendsIDs;
    }

    private static ArrayList<Long> getAllFriendsIDs(String user, boolean friend){
        Twitter tw = configureTwitter();
        ArrayList<Long> tmp = new ArrayList<>();
        Long cursor = -1L;
        try{
            IDs ids = null;
            do{
                if(friend)
                    ids = tw.getFriendsIDs(user, cursor, maxUserPerPage);
                else
                    ids = tw.getFollowersIDs(user, cursor, maxUserPerPage);
                for(Long id : ids.getIDs()){
                    tmp.add(id);
                }
                cursor = ids.getNextCursor();
            } while(ids.hasNext());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return tmp;
    }

}
