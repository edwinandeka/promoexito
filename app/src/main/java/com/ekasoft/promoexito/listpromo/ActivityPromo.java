package com.ekasoft.promoexito.listpromo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.ekasoft.promoexito.R;

public class ActivityPromo extends AppCompatActivity {


    FragmentListPromo listPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);


        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffe800")));



        listPromo = new FragmentListPromo();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listPromo).commit();
    }
}
