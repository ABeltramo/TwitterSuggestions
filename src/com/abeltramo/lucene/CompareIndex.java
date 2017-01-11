package com.abeltramo.lucene;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.index.*;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import twitter4j.RateLimitStatus;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * twitter-suggestions
 * Created by ABeltramo on 11/01/17.
 */
public class CompareIndex {
    private IndexReader _twReader;
    private IndexReader _nwReader;
    private final String _field = "tweet";

    public CompareIndex(){
        try{
            Directory directory = FSDirectory.open(new File("Lucene" + File.separator + "Tweet").toPath());
            _twReader = DirectoryReader.open(directory);
            directory = FSDirectory.open(new File("Lucene" + File.separator + "News").toPath());
            _nwReader = DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TermStats[] getTopTwitterTerms(int numTerms){
        TermStats[] terms = null;
        try {
            terms = HighFreqTerms.getHighFreqTerms(_twReader, numTerms, _field, new HighFreqTerms.TotalTermFreqComparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms;
    }

    public TopDocs queryNews(TermStats[] topTerms,int numResult){
        IndexSearcher newsSearcher = new IndexSearcher(_nwReader);
        String query = "";

        int numTerms = topTerms.length;
        for(int i=0;i<numTerms;i++){
            int curBoost = (numTerms-i);
            String curTerm = topTerms[i].termtext.utf8ToString();
            query = query.concat("(description:" + curTerm +
                                 ")^1." + curBoost +
                                 " (title:" + curTerm +
                                 ")^1." + curBoost + " ");
        }
        System.out.println("query:\n"+query+"\n");

        QueryParser qParser = new QueryParser("title", new EnglishAnalyzer());
        TopDocs result = null;
        try {
            Query q = qParser.parse(query);
            result = newsSearcher.search(q,numResult);

            ScoreDoc[] resultList = result.scoreDocs; //array of doc ids and their ranking
            for(int i = 0; i<resultList.length; i++){
                Document book = newsSearcher.doc(resultList[i].doc);
                String newsTitle = book.getField("title").stringValue();
                String newsDescription = book.getField("description").stringValue();
                System.out.println(newsTitle + ": " + newsDescription + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
