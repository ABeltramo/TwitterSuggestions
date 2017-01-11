package com.abeltramo.lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.miscellaneous.EmptyTokenStream;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * twitter-suggestions
 * Created by ABeltramo on 08/01/17.
 */
public class TwitterAnalizer extends Analyzer {
    public TwitterAnalizer(){}

    public TwitterAnalizer(ReuseStrategy reuseStrategy){
        super(reuseStrategy);
    }

    protected TokenStreamComponents createComponents(String field) {
        final Tokenizer tokenizer = new StandardTokenizer();
        TokenStream result = new StandardFilter(tokenizer);


        String[] TopEnglishWords = {"would","there","their","about","which",    // with more than 5 characters
                                    "people","could","other","think","after",   // https://en.wikipedia.org/wiki/Most_common_words_in_English
                                    "first","because","these","thank","don't",  // incremented with result from popular person
                                    "never","please","can't","should","still",  // like JKRowling and list of friends
                                    "thanks","that's","you're","where","someone",
                                    "something"};
        CharArraySet stopwords = new CharArraySet(Arrays.asList(TopEnglishWords),true);
        stopwords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        result = new StopFilter(result,stopwords);
        result = new LengthFilter(result, 5, Integer.MAX_VALUE); 	                            // Accept only tokens which have the length more than 6 characters.
        result = new PatternReplaceFilter(result,Pattern.compile("[#]"),"",true);   // Remove # and take the remaining text
        result = new TwitterFilter(result);
        result = new LowerCaseFilter(result);
        return new TokenStreamComponents(tokenizer, result);
    }
}
