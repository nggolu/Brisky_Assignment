package com.example.goku.newapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by goku on 10/10/17.
 */

public class DataParser {
    private HashMap<String , String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String , String> googlePaceMap = new HashMap<>();
        String placeName = "";
        String vicinity = "";
        String latitute = "";
        String longitude ="";
        String rating ="";

        try{
            if(!googlePlaceJson.isNull("name"))
            {
                    placeName = googlePlaceJson.getString("name");

            }
            if(!googlePlaceJson.isNull("vicinity"))
            {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if(!googlePlaceJson.isNull("rating"))
            {
                rating = googlePlaceJson.getString("rating");
            }
            latitute = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
           // rating =googlePlaceJson.getString("rating");

            googlePaceMap.put("placeName" , placeName);
            googlePaceMap.put("vicinity" , vicinity);
            googlePaceMap.put("lng" , longitude);
            googlePaceMap.put("lat" , latitute);
            googlePaceMap.put("rating" , rating);
            Log.d("dataparse", "url = "+longitude);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return  googlePaceMap;
    }

    private List<HashMap<String , String> >getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String ,String>> placeList = new ArrayList<>();
        HashMap <String , String> placemap = null;
        for (int i=0;i<count;i++)
        {
            try {
                placemap=getPlace((JSONObject) jsonArray.get(i));
                placeList.add(placemap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placeList;
    }
    public  List<HashMap<String , String> > parse(String jsonData)
    {
        JSONArray jsonArray=null;
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray =jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  getPlaces(jsonArray);
    }
}
