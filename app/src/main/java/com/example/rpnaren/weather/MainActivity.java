package com.example.rpnaren.weather;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
LocationManager lm;
LocationListener l;
TextView t1,t2,t3,t4;
   public class asd extends AsyncTask<String,Integer,String>
   {
    String result="";
  URL url;
       HttpURLConnection con=null;

       @Override
       protected String doInBackground(String... strings) {
           try {
               url = new URL(strings[0]);
               con=(HttpURLConnection) url.openConnection();
               InputStream in=con.getInputStream();
               InputStreamReader i=new InputStreamReader(in);
               int data=i.read();
               while(data!=-1)
               {
                   char cur=(char)data;
                   result+=cur;
                   data=i.read();

               }return result;
           }
           catch(Exception e)
           {
               e.printStackTrace();
           }
       return null;}
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,l);
                    Location loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(loc!=null)
                        setText(loc);


                }
    }
public void setText(Location loc)
{  double lat=loc.getLatitude();
   double lon=loc.getLongitude();
    t1.setText("Latitude:"+Double.toString(lat));
    t2.setText("Longitude:"+Double.toString(lon));
    String s=" ";
    Geocoder g=new Geocoder(getApplicationContext(), Locale.getDefault());
    try {
        List<Address> ad=g.getFromLocation(lat,lon,1);

        if(ad.get(0).getThoroughfare()!=null)
            s+=ad.get(0).getFeatureName()+" ";
        if(ad.get(0).getAdminArea()!=null)
            s+=ad.get(0).getAdminArea()+" ";

            s+=ad.get(0).getPostalCode();

    } catch (IOException e) {
        e.printStackTrace();
    }
t3.setText(s);

    asd d=new asd();
    String h="";
    try {
        h = d.execute("https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=0051eaa9b2f74d08728415850669661c").get();
        JSONObject j=new JSONObject(h);
        JSONArray q= j.getJSONArray("weather");
        JSONObject p=q.getJSONObject(0);h=p.getString("description");
    }
    catch(Exception e)
    {e.printStackTrace();}

    t4.setText("Weather:"+h);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=(TextView) findViewById(R.id.t1);
        t2=(TextView) findViewById(R.id.t2);
        t3=(TextView) findViewById(R.id.t3);
        t4=(TextView) findViewById(R.id.t4);
        lm=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
          l=new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
       setText(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
};
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,l);
           Location loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc!=null)
                setText(loc);

        }
    }

}
