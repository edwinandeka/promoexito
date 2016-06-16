package com.ekasoft.promoexito.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekasoft.promoexito.util.Settings;

/**
 * Created by eka on 3/04/2016.
 */
public class ServiceWithWebView extends Service {

    public WebView mWebView;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mWebView = new WebView(this);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(ServiceWithWebView.this, "Buscando ofertas", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("javascript:" +
                                "var products = $('.product'); var arrProducts = []; $.each(products, function(index, product) { product = $(product); var link = product.find('a').attr('href'); var price = product.find('.price').text().trim(); var name = product.find('.name').text().trim(); var offert = product.find('.supply').text().trim(); var imageUrl = product.find('.img-responsive').attr('src').trim(); var id = product.find('.product-row').attr('data-skuid').trim(); var brand = product.find('.brand').text().trim(); var before = product.find('.before').text().trim(); var other = product.find('.other').text().trim(); var iconPaymentCard = product.find('.iconPaymentCard').length; var category = product.find('.product-row').attr('data-categoryparent').trim(); var available = product.find('.available').length; arrProducts.push({ link: link, price: price, name: name, offert: offert, imageUrl: imageUrl, brand: brand, before: before, other: other, id: id, category:category, isPaymentCard: (iconPaymentCard)? true : false, available: (available)? false : true, }); }); Android.notify(JSON.stringify( arrProducts));"
                );
                super.onPageFinished(view, url);
            }
        });

        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        int count = 0;
        if (Settings.isKey(ServiceWithWebView.this, "promo_count_page")){
            count = Integer.parseInt(Settings.get(ServiceWithWebView.this, "promo_count_page"));

            if (count>800){
                count = 0;
            } else {
                count+=80;
            }
        }
        Settings.set(ServiceWithWebView.this, "promo_count_page", "" + count);

        mWebView.loadUrl("http://www.exito.com/browse?No=" + count + "&Nrpp=80&Ns=product.priceSortExito%7C1&Ntt=0f3r7a2_d3l_d1a");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }


}