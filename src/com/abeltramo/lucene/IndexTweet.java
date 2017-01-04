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

    public IndexTweet(){
        String path = "Lucene" + File.separator + "Tweet";
        new File("Lucene").mkdir();                 // Create base directory if not exists
        luceneDir  = new File(path);
        luceneDir.mkdir();                                    // Create news directory if not exists
        try {
            Directory directory = FSDirectory.open(luceneDir.toPath());
            StandardAnalyzer analyzer = new StandardAnalyzer();
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
