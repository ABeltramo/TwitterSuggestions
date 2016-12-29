package com.abeltramo.twsuggestions;


/**
 * twitter-suggestions
 * Created by ABeltramo on 29/12/16.
 */
public class Main {

    public static void main(String[] args) {
        String user = "ABeltradev";

        TwManager twmanager = new TwManager();
        for(String tweet : twmanager.getUserPost(user)){
            System.out.println("@" + user + ": " + tweet);
        }

        System.out.println("\n* Getting friend list *\n");

        for(String friend : twmanager.getUserFriend(user)){
            System.out.println("\n* Getting @" + friend + " tweet *\n");
            for(String tweet : twmanager.getUserPost(friend)){
                System.out.println("@" + friend + ": " + tweet);
            }
        }
    }
}
