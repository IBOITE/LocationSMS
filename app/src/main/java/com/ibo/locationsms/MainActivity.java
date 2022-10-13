package com.ibo.locationsms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView,textView1,textView2;
    EditText editText;
    Button button;



    LocationListener locationListener;
    LocationManager locationManager;
    String[]appPermission={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS};
    private static final int REQUEST_QUDE=2;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textID);
        textView1=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        editText=findViewById(R.id.editTextPhone);
        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sms=textView.getText().toString();
                String phoneNum=editText.getText().toString();
                if(!TextUtils.isEmpty(sms) && !TextUtils.isEmpty(phoneNum)){
                    if(checkAndRequestPermissions()){
                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNum,null,sms,null,null);
                    }
                }
            }
        });

        if(checkAndRequestPermissions()){
            
        }

        locationManager= (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String fulladress="";
                textView.setText(location.getLatitude()+" - "+location.getLongitude());
                Log.d("loction",location.toString());
                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(addressList!=null&&addressList.size()>0){
                        if(addressList.get(0).getSubAdminArea()!=null){
                            textView2.setText(addressList.get(0).getSubAdminArea()+"");
                            fulladress+=addressList.get(0).getSubAdminArea()+"";
                        }
                        if(addressList.get(0).getAddressLine(0)!=null){
                            textView1.setText(addressList.get(0).getAddressLine(0)+"");
                            fulladress+=addressList.get(0).getAddressLine(0)+"";
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

    }

    private boolean checkAndRequestPermissions() {
        List<String> listPermission=new ArrayList<>();
        for(String s:appPermission){
            if(ContextCompat.checkSelfPermission(this,s)!= PackageManager.PERMISSION_GRANTED){
                listPermission.add(s);
            }
        }
        if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermission.toArray(new String[listPermission.size()]),REQUEST_QUDE);
            return false;
        }
        return true;
    }
}