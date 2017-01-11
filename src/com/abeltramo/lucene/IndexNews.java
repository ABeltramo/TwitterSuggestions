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
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * twitter-suggestions
 * Created by ABeltramo on 04/01/17.
 */
public class IndexNews {

    private RAMDirectory _newsDir;

    public IndexNews(RAMDirectory newsDir){
        _newsDir = newsDir;
    }

    public void makeIndex(ArrayList<JSONObject> docs){
        IndexWriter iwriter = null;
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            iwriter = new IndexWriter(_newsDir, config);

            for (JSONObject ObjSource : docs) {
                try {
                    JSONArray articles =  ObjSource.getJSONArray("articles");
                    for(int i=0; i<articles.length(); i++) {
                        JSONObject article = articles.getJSONObject(i);
                        iwriter.addDocument(creaDoc(
                                ObjSource.getString("source"),
                                article.getString("url"),
                                article.getString("title"),
                                article.getString("description")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            iwriter.commit();
            iwriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document creaDoc(String source, String url, String title, String description){
        Document doc = new Document();
        if(source != null) {
            Field Ftitle = new TextField("title", title, Field.Store.YES);
            Ftitle.setBoost(1.2f); // Added boost to title
            doc.add(Ftitle);
        }
        if(description != null) {
            Field Fdescription = new TextField("description", description, Field.Store.YES);
            doc.add(Fdescription);
        }
        if(url != null) {
            Field Furl = new StringField("url", url, Field.Store.YES);
            doc.add(Furl);
        }
        if(source != null) {
            Field Fsource = new StringField("source", source, Field.Store.YES);
            doc.add(Fsource);
        }
        return doc;
    }
}
