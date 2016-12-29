package com.abeltramo.twsuggestions;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String user = "ABeltradev";

        CacheManager cm = new CacheManager();
        cm.set("test","prova",true);
        System.out.println(cm.get("test"));

        /*
        System.out.println("Getting first 200 tweets");
        for (Status status : TwitterAPI.getUserStats(user)) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }
        System.out.println();
        System.out.println("Getting friends list...");
        Twitter tw = TwitterAPI.configureTwitter();
        for (Long friend : TwitterAPI.getUserFriends(user)){
            User usr = null;
            try {
                usr =  tw.showUser(friend);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            System.out.println("ID:"+friend+" @"+usr.getScreenName());
        }

        System.out.println();
        System.out.println("Getting friends tweet...");
        for (Long friend : TwitterAPI.getUserFriends(user)){
            for (Status status : TwitterAPI.getUserStats(friend)) {
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            }
            System.out.println();
        }
        */
    }
}
