package com.abeltramo.twsuggestions;


import com.abeltramo.lucene.IndexNews;
import com.abeltramo.lucene.IndexTweet;

/**
 * twitter-suggestions
 * Created by ABeltramo on 29/12/16.
 */
public class Main {

    public static void main(String[] args) {
        String user = "BillGates";
        IndexTweet itw = new IndexTweet();
        TwManager twmanager = new TwManager();

        System.out.println("* Indexing @" + user + " tweet *");
        itw.makeIndex(twmanager.getUserPost(user),1.5f);
        for(String friend : twmanager.getUserFriend(user)){
            System.out.println("* Indexing @" + friend + " tweet *");
            itw.makeIndex(twmanager.getUserPost(friend),1.0f);
        }
        itw.closeWrite();

        System.out.println("* Indexing News *");
        NewsManager nwmanager = new NewsManager();
        IndexNews inw = new IndexNews();
        inw.makeIndex(nwmanager.getAllNews());
    }
}