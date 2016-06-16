/*
 * Copyright (C) 2013 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ekasoft.promoexito;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ekasoft.promoexito.about.AboutActivity;
import com.ekasoft.promoexito.category.CategorysFragment;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.install.InstallActivity;
import com.ekasoft.promoexito.install.InstallPromoActivity;
import com.ekasoft.promoexito.listpromo.FragmentListPromo;
import com.ekasoft.promoexito.service.ServiceWithWebView;
import com.ekasoft.promoexito.settings.SettingsActivity;
import com.ekasoft.promoexito.util.Settings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ekasoft.promoexito.R;

import java.util.Calendar;
import java.util.List;

/**
 * Main Activity. Inflates main activity xml and child fragments.
 */
public class MyActivity extends AppCompatActivity {

    private AdView mAdView;
    private WebView mWebView;
    private RelativeLayout progress;
    FragmentListPromo listPromo;
    CategorysFragment listCategories;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ekasoft.promoexito.R.layout.activity_my);

        Settings.set(this, "promo_order", "false");

        if (!Settings.isKey(this, "install")){
            Calendar cal = Calendar.getInstance();

            Intent intent = new Intent(this, ServiceWithWebView.class);
            PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
            AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24 * 60 * 60 * 1000, pintent);
            Intent intent2 = new Intent(MyActivity.this, InstallPromoActivity.class);
            startActivity(intent2);
            finish();
        }

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffe800")));
        ab.setIcon(com.ekasoft.promoexito.R.drawable.bar);
        ab.setLogo(com.ekasoft.promoexito.R.drawable.bar);
        ab.setDisplayUseLogoEnabled(true);
        ab.setHomeAsUpIndicator(com.ekasoft.promoexito.R.drawable.bar);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("");

        ActiveAndroid.initialize(this);




        mAdView = (AdView) findViewById(com.ekasoft.promoexito.R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("4CBD3F38AF88E4EFA5FC4FB8B02D8D73")
                .build();
        mAdView.loadAd(adRequest);

        listPromo = new FragmentListPromo();
        listCategories = new CategorysFragment();

        getSupportFragmentManager().beginTransaction()
                .add(com.ekasoft.promoexito.R.id.fragment_container, listPromo).commit();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.ekasoft.promoexito.R.menu.my, menu);
        this.menu = menu;
        listPromo.onCreateOptionsMenu(menu);
       // listCategories.onCreateOptionsMenu(menu);

        return super.onCreateOptionsMenu(menu);

    }

    private class SearchViewExpandListener implements MenuItemCompat.OnActionExpandListener {

        private Context context;

        public SearchViewExpandListener (Context c) {
            context = c;
        }

        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
            return false;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(false);
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {

            case R.id.action_find_promo:
                intent = new Intent(MyActivity.this, ServiceWithWebView.class);
                startService(intent);

                break;

            case R.id.action_delete_promo:
                List<Item> items = new Select().from(Item.class).where("saving = ? ", "0") .execute();
                if (items.size()>0){
                    for (Item t: items ) {
                        t.delete();
                    }
                    Toast.makeText(MyActivity.this, items.size()+" ofertas borradas", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MyActivity.this, "No hay ofertas que borrar", Toast.LENGTH_SHORT).show();
                }

                break;

           case R.id.action_about:
                intent = new Intent(MyActivity.this, AboutActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                //intent = new Intent(MyActivity.this, InstallActivity.class);
                //startActivity(intent);

                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void onGroupItemClick(MenuItem item) {
        int id = item.getItemId();

        MenuItem searchItem = menu.findItem(com.ekasoft.promoexito.R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.clearFocus();


        String order = "";
        switch (id) {

            case com.ekasoft.promoexito.R.id.submenu_by_more_recent:
                order = "dateCreated desc";
                break;


            case com.ekasoft.promoexito.R.id.submenu_by_saving_min:
                order = "saving_int asc";
                break;

            case com.ekasoft.promoexito.R.id.submenu_by_saving_max:
                order = "saving_int desc";
                break;

            case com.ekasoft.promoexito.R.id.submenu_by_price_min:
                order = "price_int asc";
                break;

            case com.ekasoft.promoexito.R.id.submenu_by_price_max:
                order = "price_int desc";
                break;

            case com.ekasoft.promoexito.R.id.submenu_by_desc_min:
                order = "offert_int desc";
                break;

            case com.ekasoft.promoexito.R.id.submenu_by_desc_max:
                order = "offert_int asc";
                break;

            case com.ekasoft.promoexito.R.id.submenu_a_z:
                order = "name asc";
                break;

            case com.ekasoft.promoexito.R.id.submenu_z_a:
                order = "name desc";
                break;




            case android.R.id.home:
                break;
        }

        invalidateOptionsMenu();
        Settings.set(this, "promo_order_list", order);
        Settings.set(this, "promo_order", "true");

        listPromo = new FragmentListPromo();
        getSupportFragmentManager().beginTransaction()
                .replace(com.ekasoft.promoexito.R.id.fragment_container, listPromo).commit();

        Toast.makeText(MyActivity.this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
    }


}

