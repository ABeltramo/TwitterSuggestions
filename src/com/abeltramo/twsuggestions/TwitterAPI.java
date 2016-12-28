package com.abeltramo.twsuggestions;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ale on 28/12/16.
 */
public class TwitterAPI {

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

    public static List<Status> getUserStats(String user) {
        return getUserStats(user,1,200);
    }

    public static List<Status> getUserStats(String user,int pageNumber, int resultperpage){
        Twitter tw = configureTwitter();
        //First param of Paging() is the page number, second is the number per page (this is capped around 200 I think.
        Paging paging = new Paging(pageNumber, resultperpage);
        List<Status> statuses = null;
        try {
            statuses = tw.getUserTimeline(user,paging);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statuses;
    }

    // Friends are those that a user follows and by whom she is followed back.
    // todo: Controllare se è giusta la definizione di friend
    public static IDs getUserFriends(String user){
        Twitter tw = configureTwitter();
        long cursor = -1;
        long lCursor = -1;
        IDs friendsIDs = null;
        try {
            friendsIDs = tw.getFriendsIDs(user, lCursor);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return friendsIDs;
    }

}
