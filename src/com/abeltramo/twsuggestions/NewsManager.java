package com.abeltramo.twsuggestions;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.util.ArrayList;

/**
 * twitter-suggestions
 * Created by ABeltramo on 29/12/16.
 */
public class NewsManager {

    private String _APIKey = "8307ef1a9d594effa19b7b099bca45b7";
    private CacheManager _cm;
    private boolean _useCache;
    public NewsManager(boolean useCache){
        _useCache = useCache;
        _cm = new CacheManager();
    }

    public ArrayList<JSONObject> getAllNews(){
        ArrayList<JSONObject> news = new ArrayList<>();
        try {
            JSONArray sources = getRemote("https://newsapi.org/v1/sources?language=en","newsapi.sources")
                                        .getJSONArray("sources");
            for(int i=0;i<sources.length();++i){
                String id = sources.getJSONObject(i).getString("id");
                JSONObject sourceContent = getRemote("https://newsapi.org/v1/articles?apiKey=" + _APIKey + "&source=" + id,"newsapi."+id);
                news.add(sourceContent);
            }
            return news;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    private JSONObject getRemote(String url,String file){
        JSONObject result = null;
        try{
            if(_cm.exists(file) && _useCache){
                result = new JSONObject(_cm.get(file));
            }
            else {
                result = RESTClient.getUrl(url);
                _cm.set(file,result.toString(),true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


}
