package com.sally.exhibition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int seq;
    private String title, startDate, endDate, place, realmName, area, subTitle, thumbNail,
             price, contents1, contents2, url, phone, gpsX, gpsY, imgUrl, placeUrl, placeAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String detailHref=getIntent().getStringExtra("detailHref");

        try {
            JSONObject detailObj=new JSONObject(detailHref);
            seq=detailObj.getInt("seq");
            title=detailObj.getString("title");
            startDate=detailObj.getString("startDate");
            endDate=detailObj.getString("endDate");
            place=detailObj.getString("place");
            realmName=detailObj.getString("realmName");
            area=detailObj.getString("area");
            subTitle=detailObj.getString("subTitle");
            thumbNail=detailObj.getString("thumbNail");
            price=detailObj.getString("price");
            contents1=detailObj.getString("contents1");
            contents2=detailObj.getString("contents2");
            url=detailObj.getString("url");
            phone=detailObj.getString("phone");
            gpsY=detailObj.getString("gpsX");//DB에 X,Y 좌표가 반대로 들어가 있음
            gpsX=detailObj.getString("gpsY");
            imgUrl=detailObj.getString("imgUrl");
            placeUrl=detailObj.getString("placeUrl");
            placeAddr=detailObj.getString("placeAddr");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView titleTextView=findViewById(R.id.titleTextView);
        ImageView thumbNail=findViewById(R.id.thumbNail);
        TextView placeTextView=findViewById(R.id.placeTextView);
        TextView startDateTextView=findViewById(R.id.startDateTextView);
        TextView endDateTextView=findViewById(R.id.endDateTextView);
        TextView priceTextView=findViewById(R.id.priceTextView);
        TextView phonetextView=findViewById(R.id.phonetextView);
        Button paymentBtn=findViewById(R.id.paymentBtn);
        Button lIkeBtn=findViewById(R.id.lIkeBtn);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        TextView contentsTextView=findViewById(R.id.contentsTextView);

        titleTextView.setText(Html.fromHtml(title));
        Glide.with(this).load(imgUrl).into(thumbNail);
        placeTextView.setText(place);
        startDateTextView.setText(startDate);
        endDateTextView.setText(endDate);
        priceTextView.setText(price);
        phonetextView.setText(phone);

        mapFragment.getMapAsync(this);
        contentsTextView.setText(Html.fromHtml(contents1));
        Log.d("content", contents1);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng place = new LatLng(Double.parseDouble(gpsX), Double.parseDouble(gpsY));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));
        mMap.addMarker(new MarkerOptions().position(place).title(this.place));
    }

}
