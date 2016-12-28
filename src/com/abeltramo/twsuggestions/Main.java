package com.abeltramo.twsuggestions;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Getting first 200 tweets");
        for (Status status : TwitterAPI.getUserStats("ABeltradev")) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }
        System.out.println("Getting friends list...");
        Twitter tw = TwitterAPI.configureTwitter();
        for (Long friend : TwitterAPI.getUserFriends("ABeltradev").getIDs()){
            User usr = null;
            try {
                usr =  tw.showUser(friend);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            System.out.println("ID:"+friend+" @"+usr.getName());
        }
    }
}
