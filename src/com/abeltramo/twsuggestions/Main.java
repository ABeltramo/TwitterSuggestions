package com.abeltramo.twsuggestions;


import com.abeltramo.lucene.IndexNews;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * twitter-suggestions
 * Created by ABeltramo on 29/12/16.
 */
public class Main {

    public static void main(String[] args) {
        String user = "ABeltradev";

        TwManager twmanager = new TwManager();
        for(String tweet : twmanager.getUserPost(user)){
            //System.out.println("@" + user + ": " + tweet);
        }

        //System.out.println("\n* Getting friend list *\n");

        for(String friend : twmanager.getUserFriend(user)){
            //System.out.println("\n* Getting @" + friend + " tweet *\n");
            for(String tweet : twmanager.getUserPost(friend)){
                //System.out.println("@" + friend + ": " + tweet);
            }
        }

        System.out.println("\n* News *\n");
        NewsManager nwmanager = new NewsManager();
        IndexNews.makeIndex(nwmanager.getAllNews());


    }
}