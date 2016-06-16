package com.ekasoft.promoexito.category;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.listpromo.FragmentListPromo;

public class ActivityCategory extends AppCompatActivity {


    CategorysFragment categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);


        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffe800")));



        categories = new CategorysFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, categories).commit();
    }
}
