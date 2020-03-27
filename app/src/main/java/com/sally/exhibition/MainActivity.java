package com.sally.exhibition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView=findViewById(R.id.webView);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        //javascriptInterface()메소드를 콜 하면서 WebAppInterface객체를 전달해준다.
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webView.loadUrl("http://192.168.0.26:8888/exhibition/");

        /*

            특정 시점에  WebView 의 javascript 실행환경에 있는 특정 함수를 호출하고 싶다면

            - WebView 에 로딩된 javascript

            <script>
                function fromAndroid(arg){

                }
            </script>

            - Android navive code
             [문자열 전달]
             String myName="kimgura";
             webView.loadUrl("javascript:fromAndroid("+myName+");");

             [object 전달]
             String myName="kimgura";
             webView.loadUrl("javascript:fromAndroid({num:1, name:\"kimgura\"});");
         */
    }

    public class MyWebViewClient extends WebChromeClient{

    }

    //bridge 역할을 할 innerclass
    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        /*
            서버단에서 받아온 문자열을 toast라는 변수에 담아서 받는다.
            여러가지 데이터를 받아올 때는 JSON 문자열로 많이 받아온다.
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Intent intent=new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("detailHref", toast);
            startActivity(intent);
        }
    } //class WebAppInterface end
}
