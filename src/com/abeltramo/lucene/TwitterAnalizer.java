package com.abeltramo.lucene;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.miscellaneous.EmptyTokenStream;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
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
        final Tokenizer tokenizer = new WhitespaceTokenizer();
        TokenStream result = new StandardFilter(tokenizer);


        String[] TopEnglishWords = {"would","there","their","about","which",    // with more than 5 characters
                                    "people","could","other","think","after",   // https://en.wikipedia.org/wiki/Most_common_words_in_English
                                    "first","because","these","thank","don't",  // incremented with result from person's who have a lot of friends
                                    "never","please","can't","should","still",  // like JKRowling
                                    "thanks","that's","you're","where","someone",
                                    "something", "great", "today", "tonight", "happy",
                                    "tomorrow"};
        CharArraySet stopwords = new CharArraySet(Arrays.asList(TopEnglishWords),true);
        stopwords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        result = new WordDelimiterFilter(result,                                                    //
                WordDelimiterFilter.STEM_ENGLISH_POSSESSIVE,
                null);
        result = new StopFilter(result,stopwords);
        result = new LengthFilter(result, 5, Integer.MAX_VALUE); 	                            // Accept only tokens which have the length more than 5 characters.
        result = new TwitterFilter(result);                                                         // Personal made filter
        result = new PatternReplaceFilter(result,Pattern.compile("[#]"),"",true);   // Remove # and take the remaining text
        result = new LowerCaseFilter(result);                                                       // Normalize lowercase

        return new TokenStreamComponents(tokenizer, result);
    }
}
