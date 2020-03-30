package com.sally.exhibition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int seq;
    private String title, startDate, endDate, place, realmName, area, subTitle, thumbNail,
             price, contents1, contents2, url, phone, gpsX, gpsY, imgUrl, placeUrl, placeAddr;
    private  ImageView contentImageView;

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
        contentImageView=findViewById(R.id.contentImageView);

        titleTextView.setText(Html.fromHtml(title));
        Glide.with(this).load(imgUrl).into(thumbNail);
        placeTextView.setText(place);
        startDateTextView.setText(startDate);
        endDateTextView.setText(endDate);
        priceTextView.setText(price);
        phonetextView.setText(phone);

        mapFragment.getMapAsync(this);
        Log.d("content", contents1);



        if(contents1!=null){
            setContent(contents1);
        }

        if(contents2!=null){
            setContent(contents2);
        }


    }

    // 공연/전시 내용 출력하는 메소드
    public void setContent(String contents){
        StringBuilder sb=new StringBuilder();
        //정규식을
        String regEx = "\\b(https?):\\/\\/[A-Za-z0-9-+&@#\\/%?=~_|!:,.;]*";
        //패턴으로 만들고
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=null;
        //패턴을 스트링과 매치시킨다.
        matcher = pattern.matcher(contents1);

        while(matcher.find()){
            sb.append(matcher.group());
        }


        Glide.with(this).load(sb.toString()).into(contentImageView);
        System.out.println("hello"+sb.toString());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng place = new LatLng(Double.parseDouble(gpsX), Double.parseDouble(gpsY));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));
        mMap.addMarker(new MarkerOptions().position(place).title(this.place));
    }

}
