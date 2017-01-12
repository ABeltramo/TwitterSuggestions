package com.abeltramo.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * twitter-suggestions
 * Created by ABeltramo on 04/01/17.
 */
public class IndexTweet {
    private File luceneDir;
    private IndexWriter userWriter = null;
    private IndexWriter friendWriter = null;

    public IndexTweet(Directory userDir, Directory friendDir){
        try {
            TwitterAnalizer analyzer = new TwitterAnalizer();
            userWriter = new IndexWriter(userDir, new IndexWriterConfig(analyzer));
            friendWriter = new IndexWriter(friendDir, new IndexWriterConfig(analyzer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeIndex(ArrayList<String> tweets, boolean user){
        try {
            for (String tw : tweets) {
                if(user)
                    userWriter.addDocument(creaDoc(tw));
                else
                    friendWriter.addDocument(creaDoc(tw));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document creaDoc(String tweet){
        Document doc = new Document();
        Field Ftweet = new TextField("tweet", tweet, Field.Store.YES);
        doc.add(Ftweet);
        return doc;
    }

    public void closeWrite(){
        try{
            userWriter.commit();
            userWriter.close();
            friendWriter.commit();
            friendWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
