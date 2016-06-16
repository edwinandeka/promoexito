package com.ekasoft.promoexito.webview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.util.Services;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WebviewActivity extends AppCompatActivity {

    WebView mWebView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffe800")));
        ab.setDisplayUseLogoEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.bar);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("exito.com");

        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("4CBD3F38AF88E4EFA5FC4FB8B02D8D73")


                .build();
        mAdView.loadAd(adRequest);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String url = extras.getString("url");

                mWebView = (WebView) findViewById(R.id.webView);
                WebSettings settings = mWebView.getSettings();
                settings.setJavaScriptEnabled(true);
                mWebView.loadUrl(Services.BASE_URL + url);

            }
        }else {
            Toast.makeText(WebviewActivity.this, "No se encontró el producto", Toast.LENGTH_SHORT).show();
            finish();
        }
            

    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
