package com.abeltramo.twsuggestions;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Calendar;
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
    private TwitterFactory _tf;
    private Twitter _tw;

    public TwitterAPI() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("kGCmUSjwceYb3HJXu8AlcrX0O")
                .setOAuthConsumerSecret("z4IIwAsIQbw4CNOuN6hANB0GJ941Klzt7pQlowyxiumEp23tTy")
                .setOAuthAccessToken("423542101-xl7YFhTKNytPAYVHXd8xKT38Qv7XOjYSTAEoEmm1")
                .setOAuthAccessTokenSecret("DwM3SCMffkIUW1Pz6tUcjyDlJFxFN7Tk8WYdQgmv03dgZ");
        _tf = new TwitterFactory(cb.build());
        _tw = _tf.getInstance();
    }

    public List<Status> getUserStats(String user){
        List<Status> statuses = new ArrayList<>();
        try {
            int prevStatLength;
            for(int i=1;i<=maxPages;i++) {
                prevStatLength = statuses.size();
                statuses.addAll(_tw.getUserTimeline(user, new Paging(i, maxTweet)));
                if(statuses.size() == prevStatLength)                           // if there is no more tweet to crawl
                    break;                                                      // exit
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statuses;
    }

    // Friends are those that a user follows and by whom she is followed back.
    public ArrayList<String> getUserFriends(String user){
        ArrayList<String> friendsIDs = new ArrayList<>();
        try {
            ArrayList<Long> friends = getAllFriendsIDs(user);
            for(Long id : friends){                                                 // For each following user
                friendsIDs.add(_tw.showUser(id).getScreenName());                    // Add to the result
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return friendsIDs;
    }

    private ArrayList<Long> getAllFriendsIDs(String user) throws TwitterException{
        ArrayList<Long> tmp = new ArrayList<>();
        ArrayList<Long> friends = new ArrayList<>();
        Long cursor = -1L;
        IDs ids = null;

        User curUser = _tw.showUser(user);
        // Take the min number of user
        if(curUser.getFollowersCount() < curUser.getFriendsCount()){
            do {
                checkAPICall("/followers/ids");
                ids = _tw.getFollowersIDs(user, cursor, maxUserPerPage);
                for(Long id : ids.getIDs()){
                    tmp.add(id);
                }
                cursor = ids.getNextCursor();
            } while(ids.hasNext());
        }
        else{
            do {
                checkAPICall("/friends/ids");
                ids = _tw.getFriendsIDs(user, cursor, maxUserPerPage);
                for(Long id : ids.getIDs()){
                    tmp.add(id);
                }
                cursor = ids.getNextCursor();
            } while(ids.hasNext());
        }   // Finish this I have a list of follower or friends
            // I have to check if they are also on the other list
        for(Long id : ids.getIDs() ){
            checkAPICall("/friendships/show");
            Relationship rel = _tw.showFriendship(curUser.getId(),id);
            if(rel.isSourceFollowedByTarget() && rel.isSourceFollowingTarget()){
                friends.add(id);
            }
        }
        return friends;
    }

    private void checkAPICall(String windowName){
        try {
            RateLimitStatus st = _tw.getRateLimitStatus().get(windowName);

            System.out.println(windowName+": "+st.getRemaining());
            if(st.getRemaining() <= 0){
                int secondsRemaining = st.getSecondsUntilReset();
                System.out.println("Limit reached at "+ getCurTime() + " waiting: "+ secondsRemaining / 60 +" seconds");
                MainForm.msgbox("Twitter API limit reached for "+windowName+"\nWaiting "+ secondsRemaining +" sec","Twitter API limit reached");
                try {
                    TimeUnit.SECONDS.sleep(secondsRemaining/2);
                    System.out.println("Half the time! " + getCurTime());
                    TimeUnit.SECONDS.sleep(secondsRemaining/2 + 2);
                    System.out.println("Restarting...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private String getCurTime(){
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minutes = rightNow.get(Calendar.MINUTE);
        return hour+":"+minutes;
    }
}
