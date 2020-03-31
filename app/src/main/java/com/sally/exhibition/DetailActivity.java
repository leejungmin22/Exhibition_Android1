package com.sally.exhibition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
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

import java.util.HashMap;
import java.util.Map;
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
        //Log.d("content", contents1);

        new SetContentTask().execute(contents1, contents2);

    }


    public class SetContentTask extends AsyncTask<String, Integer, String>{

        @Override
        protected void onPostExecute(String contents) {
            super.onPostExecute(contents);
            Log.d("contents", contents);
            if(contents!=null){
                Glide.with(DetailActivity.this)
                        .load(contents)
                        .into(contentImageView);
            }
            if (contents==null){
                contentImageView.setImageResource(R.drawable.coming_soon);
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String contents1=strings[0];
            String contents2=strings[1];

            Map<String, String> map=new HashMap<>();

            if(contents1!=null){
                contents1=getContent(contents1);
                map.put("imageUrl", contents1);
            }

            /*if(contents2!=null){

            }*/


            return contents1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            contentImageView.setImageResource(R.drawable.loading);
        }


    }

    public String getContent(String contents){
        StringBuilder sb=new StringBuilder();
        //정규식을
        String imgUrlCheck = "\\b(https?):\\/\\/[A-Za-z0-9-+&@#\\/%?=~_|!:,.;]*";
        String tagCheck = "\\<\\p\\>\\<br\\s\\/\\>\\<\\/\\p\\>";
        //패턴으로 만들고
        Pattern imgUrlPattern=Pattern.compile(imgUrlCheck);
        Pattern tagPattern=Pattern.compile(tagCheck);
        Matcher imgUrlmatcher=imgUrlPattern.matcher(contents1);
        Matcher tagmatcher=tagPattern.matcher(contents1);

        if(imgUrlmatcher.matches()){
            while(imgUrlmatcher.find()){
                sb.append(imgUrlmatcher.group());
            }
            return sb.toString();
        }

        if (tagmatcher.matches()){
            return null;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng place = new LatLng(Double.parseDouble(gpsX), Double.parseDouble(gpsY));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));
        mMap.addMarker(new MarkerOptions().position(place).title(this.place));
    }

}
