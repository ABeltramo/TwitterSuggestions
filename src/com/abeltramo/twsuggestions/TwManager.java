package com.abeltramo.twsuggestions;

import twitter4j.Status;

import java.util.ArrayList;

/**
 * twitter-suggestions
 * Created by ABeltramo on 29/12/16.
 */
public class TwManager {

    private CacheManager _cm;
    private boolean _useCache;
    private TwitterAPI _twa;

    public TwManager(boolean useCache){
        _twa = new TwitterAPI();
        _useCache = useCache;
        _cm = new CacheManager();
    }

    public ArrayList<String> getUserPost(String user){
        ArrayList<String> result = new ArrayList<>();
        String filename = user+".tweet";
        if(_cm.exists(filename) && _useCache){
            for(String tweet : _cm.get(filename).split("\n")){
                result.add(tweet);
            }
        }
        else{
            boolean delete = true;                                  // Delete file only on first run
            for (Status status : _twa.getUserStats(user)) {
                result.add(status.getText());
                _cm.set(filename,status.getText()+"\n",delete);
                delete = false;
            }
        }
        return result;
    }

    public ArrayList<String> getUserFriend(String user){
        ArrayList<String> result = new ArrayList<>();
        String filename = user+".friends";
        if(_cm.exists(filename) && _useCache) {
            for(String friend : _cm.get(filename).split("\n")){
                result.add(friend);
            }
        }
        else{
            boolean delete = true;                                  // Delete file only on first run
            for (String friend : _twa.getUserFriends(user)) {
                result.add(friend);
                _cm.set(filename,friend+"\n",delete);
                delete = false;
            }
        }
        return result;
    }
}
