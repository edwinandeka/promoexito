package com.ekasoft.promoexito.install;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.service.WebAppInterface;

public class InstallPromoActivity extends AppCompatActivity {

    public WebView mWebView;
    public TextView textCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install);

        textCategories = (TextView)findViewById(R.id.text_categories);


        mWebView = new WebView(this);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript:" +
                                "var products = $('.product'); var arrProducts = []; $.each(products, function(index, product) { product = $(product); var link = product.find('a').attr('href'); var price = product.find('.price').text().trim(); var name = product.find('.name').text().trim(); var offert = product.find('.supply').text().trim(); var imageUrl = product.find('.img-responsive').attr('src').trim(); var id = product.find('.product-row').attr('data-skuid').trim(); var brand = product.find('.brand').text().trim(); var before = product.find('.before').text().trim(); var other = product.find('.other').text().trim(); var iconPaymentCard = product.find('.iconPaymentCard').length; var category = product.find('.product-row').attr('data-categoryparent').trim(); var available = product.find('.available').length; arrProducts.push({ link: link, price: price, name: name, offert: offert, imageUrl: imageUrl, brand: brand, before: before, other: other, id: id, category:category, isPaymentCard: (iconPaymentCard)? true : false, available: (available)? false : true, }); }); Android.notify(JSON.stringify( arrProducts));"
                );
                super.onPageFinished(view, url);
            }
        });

        mWebView.addJavascriptInterface(new WebAppInterfaceInstallPromo(this), "Android");
        mWebView.loadUrl("http://www.exito.com/browse?No=0&Nrpp=80&Ns=product.priceSortExito%7C1&Ntt=0f3r7a2_d3l_d1a");



    }
}
