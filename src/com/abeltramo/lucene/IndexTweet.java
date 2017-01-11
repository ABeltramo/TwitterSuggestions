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
    private IndexWriter iwriter = null;

    public IndexTweet(RAMDirectory directory){
        try {
            TwitterAnalizer analyzer = new TwitterAnalizer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            iwriter = new IndexWriter(directory, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeIndex(ArrayList<String> tweets, float boost){
        try {
            for (String tw : tweets) {
                iwriter.addDocument(creaDoc(tw, boost));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document creaDoc(String tweet, float boost){
        Document doc = new Document();
        Field Ftweet = new TextField("tweet", tweet, Field.Store.YES);
        Ftweet.setBoost(boost);
        doc.add(Ftweet);
        return doc;
    }

    public void closeWrite(){
        try{
            iwriter.commit();
            iwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
