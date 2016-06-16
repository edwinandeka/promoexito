package com.ekasoft.promoexito.service;

import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekasoft.promoexito.database.Item;

/**
 * Created by eka on 3/04/2016.
 */
public class ActionCompareWebView implements View.OnClickListener {

    public  WebView mWebView;
    public Item item;
    public Context context;
            

    ActionCompareWebView(Item item) {
        this.item = item;
    }

    @Override
    public void onClick(View v) {
        context = v.getContext();

        int index = item.name.lastIndexOf(" ");
        String code = item.name.substring(index, item.name.length());
        Toast.makeText(context, "" + code, Toast.LENGTH_SHORT).show();

        mWebView = new WebView(context);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(context, "consultando", Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("javascript:" +
                        "var products = $('.product'); var arrProducts = []; $.each(products, function(index, product) { product = $(product); var link = product.find('a').attr('href'); var price = product.find('.price').text().trim(); var name = product.find('.name').text().trim(); var offert = product.find('.supply').text().trim(); var imageUrl = product.find('.img-responsive').attr('src').trim(); var id = product.find('.product-row').attr('data-skuid').trim(); var brand = product.find('.brand').text().trim(); var before = product.find('.before').text().trim(); var other = product.find('.other').text().trim(); var iconPaymentCard = product.find('.iconPaymentCard').length; var category = product.find('.product-row').attr('data-categoryparent').trim(); arrProducts.push({ link: link, price: price, name: name, offert: offert, imageUrl: imageUrl, brand: brand, before: before, other: other, id: id, category:category, isPaymentCard: (iconPaymentCard)? true : false, }); }); Android.notify(JSON.stringify( arrProducts));");
                super.onPageFinished(view, url);
            }
        });

        mWebView.addJavascriptInterface(new WebAppInterface(context), "Android");
        mWebView.loadUrl("http://www.alkosto.com/salesperson/result/?q="+code);
    }
}