package com.abeltramo.lucene;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.index.*;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.*;

/**
 * twitter-suggestions
 * Created by ABeltramo on 11/01/17.
 */
public class CompareIndex {
    private IndexReader _twReader;
    private IndexReader _frReader;
    private IndexReader _nwReader;
    private final String _field = "tweet";

    public CompareIndex(Directory tweetDir, Directory friendDir, Directory newsDir){
        try{
            _twReader = StandardDirectoryReader.open(tweetDir);
            _frReader = StandardDirectoryReader.open(friendDir);
            _nwReader = StandardDirectoryReader.open(newsDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTopTwitterTerms(int numTerms, int numFriend){
        Map<String,Integer> terms = new TreeMap<>();
        HighFreqTerms.TotalTermFreqComparator comparator = new HighFreqTerms.TotalTermFreqComparator();
        try {
            System.out.println("Top Terms user:");
            for(TermStats term : HighFreqTerms.getHighFreqTerms(_twReader, numTerms, _field, comparator)){              // First: get user top terms
                terms.put(term.termtext.utf8ToString(),term.docFreq);                                                   // terms[term] = docFreq * 2 (BOOST)
                System.out.print(term.termtext.utf8ToString()+" " + term.docFreq + " ");                                // * DEBUG
            }
            System.out.println("\nTop Terms friend:");
            if(numFriend > 0) {
                TermStats[] friendTerms = HighFreqTerms.getHighFreqTerms(_frReader, numTerms, _field, comparator);      // Second: get friend top terms
                for (TermStats term : friendTerms) {
                    String termText = term.termtext.utf8ToString();
                    int curFreq = terms.getOrDefault(termText, 0);
                    int newFreq = (term.docFreq / numFriend);                                                           // normalize the result
                    terms.put(termText, curFreq + newFreq);                                                             // terms[term] += docFreq
                    System.out.print(termText + " " + newFreq + " ");                                                   // * DEBUG
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> orderedResult = new ArrayList<>();                                                            // FASE 2: ordering result by docFreq
        for(int i=0;i<numTerms;i++){                                                                                    // Maybe a better sorting than bubble sort?
            int max = 0;
            String key = "";
            for(Map.Entry<String,Integer> term : terms.entrySet()){
                if(term.getValue() > max){
                    max = term.getValue();
                    key = term.getKey();
                }
            }
            orderedResult.add(key);                                                                                     // We have a winner, save it
            terms.remove(key);                                                                                          // Remove the winner from the list
        }

        return orderedResult;
    }

    public Document[] queryNews(ArrayList<String> topTerms,int numResult){
        IndexSearcher newsSearcher = new IndexSearcher(_nwReader);
        String query = "";

        int numTerms = topTerms.size();
        for(int i=0;i<numTerms;i++){
            int curBoost = (numTerms-i);
            String curTerm = topTerms.get(i);
            query = query.concat("(description:" + curTerm +
                                 ")^1." + curBoost +
                                 " (title:" + curTerm +
                                 ")^1." + curBoost + " ");
        }
        System.out.println("query:\n"+query+"\n");

        QueryParser qParser = new QueryParser("title", new EnglishAnalyzer());
        TopDocs result = null;
        Document[] docs = null;
        try {
            Query q = qParser.parse(query);
            result = newsSearcher.search(q,numResult);

            ScoreDoc[] resultList = result.scoreDocs; //array of doc ids and their ranking
            docs = new Document[resultList.length];
            for(int i = 0; i<resultList.length; i++){
                docs[i] = newsSearcher.doc(resultList[i].doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return docs;
    }
}
