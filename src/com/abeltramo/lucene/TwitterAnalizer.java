package com.abeltramo.lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.StandardFilter;

/**
 * twitter-suggestions
 * Created by ABeltramo on 08/01/17.
 */
public class TwitterAnalizer extends Analyzer {
    public TwitterAnalizer(){

    }

    public TwitterAnalizer(ReuseStrategy reuseStrategy){
        super(reuseStrategy);
    }

    protected TokenStreamComponents createComponents(String field) {
        final Tokenizer tokenizer = new WhitespaceTokenizer();
        TokenStream result = new StandardFilter(tokenizer);
        result = new StopFilter(result,StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        result = new LengthFilter(result, 4, Integer.MAX_VALUE); 	//Accept only tokens which have the length more than 4 characters.
        result = new LowerCaseFilter(result);
        return new TokenStreamComponents(tokenizer, result);
    }
}
