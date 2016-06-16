package com.ekasoft.promoexito.detailpromo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.util.ByteArrayUtil;
import com.ekasoft.promoexito.util.Services;
import com.ekasoft.promoexito.webview.WebviewActivity;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DetailsActivity extends AppCompatActivity {

    public LinearLayout container;
    public TextView name;
    public TextView price;
    public TextView promo;
    public TextView saving;
    public TextView brand;
    public TextView before;
    public TextView other;
    public ImageView image;
    public ImageView share;
    public ImageView star;
    public ImageView tarjeta;
    public Button browser;

    public TextView isPaymentCard;
    public long dateCreated;

    PhotoViewAttacher mAttacher;
    Item item;
    private Button browserBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffe800")));
        ab.setDisplayUseLogoEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.bar);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Detalle de producto");


        name = (TextView) findViewById(R.id.txt_name);
        price = (TextView) findViewById(R.id.txt_price);
        promo = (TextView) findViewById(R.id.txt_promo);
        saving = (TextView) findViewById(R.id.saving);
        brand = (TextView) findViewById(R.id.txt_brand);
        before = (TextView) findViewById(R.id.txt_before);
        other = (TextView) findViewById(R.id.other);
        image = (ImageView) findViewById(R.id.image);
        share = (ImageView) findViewById(R.id.share);
        star = (ImageView) findViewById(R.id.star);
        tarjeta = (ImageView) findViewById(R.id.tarjeta);
        browser = (Button) findViewById(R.id.browser);
        browserBuy = (Button) findViewById(R.id.button);



        container = (LinearLayout) findViewById(R.id.container);



        long id;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                id= extras.getLong("id");

                item = Item.load(Item.class, id);

                name.setText(item.name);
                price.setText(item.price);
                brand.setText(item.brand);
                before.setText(item.before);
                promo.setText(item.offert);
                saving.setText("Ahorre: $" + item.saving);



                if (item.other.equals("")){
                    other.setVisibility(View.GONE);
                }else {
                    other.setText(item.other);
                    other.setVisibility(View.VISIBLE);
                }

                if (item.isPaymentCard.equals("false")){
                    tarjeta.setVisibility(View.GONE);
                }else {
                    tarjeta.setVisibility(View.VISIBLE);
                }


                image.setImageBitmap(ByteArrayUtil.getImage(item.image));

                if (item.offert.equals("")){
                    star.setVisibility(View.GONE);
                }else {
                    star.setVisibility(View.VISIBLE);
                }

                Glide.with(this)
                        .load(Services.BASE_URL + item.imageUrl.replace("_md_", "_xl_")).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mAttacher = new PhotoViewAttacher(image);
                        return false;
                    }
                }).into(image);


                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add data to the intent, the receiving app will decide
                        // what to do with it.
                        share.putExtra(Intent.EXTRA_SUBJECT, "Me gustó este producto");
                        share.putExtra(Intent.EXTRA_TEXT, Services.BASE_URL + item.link);

                        startActivity(Intent.createChooser(share, "Enviar oferta a un amigo :)"));
                    }
                });
                browser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(Services.BASE_URL + item.link));
                        startActivity(i);
                        */

                        Intent intent = new Intent(DetailsActivity.this, WebviewActivity.class);
                        intent.putExtra("url", item.link);
                        startActivity(intent);
                    }
                });

                browserBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(Services.BASE_URL + item.link));
                        startActivity(i);

                    }
                });

            }
        }

    }
}
