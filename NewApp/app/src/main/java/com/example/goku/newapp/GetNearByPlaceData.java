package com.example.goku.newapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.duration;
import static android.R.attr.factor;

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
//    public Bitmap getBitmapFromURL(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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
//                Bitmap bmImg = getBitmapFromURL("https://maps.gstatic.com/mapfiles/place_api/icons/bar-71.png");

                //https://maps.gstatic.com/mapfiles/place_api/icons/bar-71.png
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
               // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmImg));

//                BitmapDrawable bitmapdraw=(BitmapDrawable) ContextCompat.getDrawable(, R.drawable.bar);
//                ContextCompat.getDrawable(getActivity(), R.drawable.name);

//                Bitmap b=bitmapdraw.getBitmap();
//                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 10, 10, false);
//                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bar));
                markerOptions.snippet(vicinity);
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        }
        else
        {
            Log.d("GetNearByPlaces", "size = "+nearbyPlacesList.size());
//            Toast.makeText(MapsActivity.this,  "Zero Pubs search arround you \n Increase distance for more results", Toast.LENGTH_SHORT).show();,
        }



    }
}
