package com.abeltramo.lucene;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;

/**
 * twitter-suggestions
 * Created by ABeltramo on 09/01/17.
 */
public class TwitterFilter extends TokenFilter {

    /* The constructor for our custom token filter just calls the TokenFilter
     * constructor; that constructor saves the token stream in a variable named
     * this.input.
     */
    public TwitterFilter(TokenStream tokenStream) {
        super(tokenStream);
    }

    /* Local variable */
    protected CharTermAttribute charTermAttribute =
            addAttribute(CharTermAttribute.class);
    protected PositionIncrementAttribute positionIncrementAttribute =
            addAttribute(PositionIncrementAttribute.class);

    /*
     * Ovveride the function to iterate over the tokens
     */
    @Override
    public boolean incrementToken() throws IOException {
        // Loop over tokens in the token stream to find the next one
        String nextToken = null;
        while (nextToken == null) {

            // Reached the end of the token stream being processed
            if ( ! this.input.incrementToken()) {
                return false;
            }

            String currentTokenInStream = this.input.getAttribute(CharTermAttribute.class)
                                                    .toString();                        // Get current token string


            if(!currentTokenInStream.contains("@"))                                     // Check if it has a @ inside
                if(!currentTokenInStream.contains("http"))                              // Check if it is a URL
                    if(!currentTokenInStream.contains("&#"))                            // Remove HTML strings
                        nextToken = currentTokenInStream;                               // Save the token as valid

        }

        // Save the current token
        this.charTermAttribute.setEmpty().append(nextToken);
        this.positionIncrementAttribute.setPositionIncrement(1);
        return true;
    }
}