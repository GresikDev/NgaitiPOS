package org.gresikdev.ngaitipos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private LatLng pos;
    ProgressDialog pd;
    float mindistance = 10000f; //10km
    TextView lokasi;
    float lastacuracy = mindistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

//        GPSTracker gps = new GPSTracker(this);

        final Button penjual = (Button) findViewById(R.id.bt_choose_penjual);
        final Button pembeli = (Button) findViewById(R.id.bt_choose_pembeli);
        lokasi = (TextView) findViewById(R.id.tv_choose_location);

//        penjual.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                pembeli.setEnabled(false);
//            }
//        });
//        pembeli.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                penjual.setEnabled(false);
//            }
//        });
        penjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keMain(1);
            }
        });
        pembeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keMain(2);
            }
        });

//        if (gps.canGetLocation()) {
//
//            double latitudeku = gps.getLatitude();
//            double longitudeku = gps.getLongitude();
//            String takelat = "" + latitudeku;
//            String takelng = "" + longitudeku;
//            Toast.makeText(getApplicationContext(),
//                    "Lokasi anda : " + takelat + "," + takelng,
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            gps.showSettingsAlert();
//        }
        cekMaps();

    }

    void cekMaps() {
        if (!pindah)
            return;
        pd = new ProgressDialog(this);
        pd.setMessage("Menandai Lokasi Anda");
        //pd.setCancelable(false);
        pd.setCancelable(true);
        pd.show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getBaseContext(), "tes if true", Toast.LENGTH_LONG).show();
            return;
        }
//        Toast.makeText(getBaseContext(), "tes if false", Toast.LENGTH_LONG).show();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,   // 1 sec
                mindistance, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (pd != null){
            if (pd.isShowing()){
                pd.cancel();
            }
        }
        //String str = "Provider: "+location.getProvider()+"\nLatitude: "+location.getLatitude()+" \nLongitude: "+location.getLongitude();
        //Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();

        if (location.getAccuracy() < lastacuracy){
            lastacuracy = location.getAccuracy();
            pos = new LatLng(location.getLatitude(), location.getLongitude());
            String teks = "Provider\t: " + location.getProvider()
                    + "\nLokasi\t: " + pos.latitude + "," + pos.longitude
                    + "\nAkurasi\t: " + location.getAccuracy();
            //Toast.makeText(getApplicationContext(), teks, Toast.LENGTH_SHORT).show();
            lokasi.setText(teks);
        }

        if (location.getAccuracy() > 10) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            double tenthPower = Math.floor(Math.log10(mindistance));
            float place = (float) (Math.pow(10, tenthPower));
            if (place > 10) {
                mindistance = place;
                //locationManager.removeUpdates(this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000,   // 1 sec
                        mindistance, this);
            }
        } else {
            locationManager.removeUpdates(this);
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(pos.latitude,
                        pos.longitude, 1);
                if (addresses.size() > 0) {
                    //System.out.println(addresses.get(0).getLocality());
                    String cityName = addresses.get(0).getLocality();
                    lokasi.append("\nNama : "+cityName);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Toast.makeText(getBaseContext(), provider + " | " + status, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getApplicationContext(),
//                "Tes Result : " + requestCode + "," + resultCode,
//                Toast.LENGTH_SHORT).show();
//    }

    boolean pindah = true;

    private void keMain(int kode){
        if (pindah){
            pindah = false;
            locationManager.removeUpdates(this);

            SharedPreferences sharedPreferences = getSharedPreferences("NgaitiPOS", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("firstview", kode);
            editor.commit();

            startActivity(new Intent(this, MainActivity.class));
            //startActivity(new Intent(this, MapsActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
