package com.example.goku.newapp;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by goku on 10/10/17.
 */

public class GetNearByPlaceData extends AsyncTask<Object, String , String> {
    String googlePlaceData;
    GoogleMap mMap;
    String url;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlaceData =downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        List<HashMap<String , String> > nearbyPlaceList =null;
        DataParser parser = new DataParser();
        nearbyPlaceList  = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }
    private  void showNearbyPlaces(List<HashMap<String , String> >nearbyPlacesList)
    {

        if(nearbyPlacesList.size()!=0)
        {
            for(int i=0;i<nearbyPlacesList.size();i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String , String> googlePlaces = nearbyPlacesList.get(i);

                String placename = googlePlaces.get("placeName");

                String vicinity = googlePlaces.get("vicinity");
                String rating = googlePlaces.get("rating");

                double lat = Double.parseDouble(googlePlaces.get("lat"));
                double lng = Double.parseDouble(googlePlaces.get("lng"));
                Log.d("getnearPlace", "url = "+lat +" " +lng + " \n" );
                LatLng latLng = new LatLng(lat,lng);
                markerOptions.position(latLng);
                if(rating=="")
                    rating="NA";
                markerOptions.title(placename + ":Rating :" + rating );
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                markerOptions.snippet(vicinity);
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        }
        else
        {
            Log.d("GetNearByPlaces", "size = "+nearbyPlacesList.size());
        }



    }
}
