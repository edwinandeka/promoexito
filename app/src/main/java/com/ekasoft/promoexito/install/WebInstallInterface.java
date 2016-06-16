package com.ekasoft.promoexito.install;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.activeandroid.ActiveAndroid;
import com.ekasoft.promoexito.MyActivity;
import com.ekasoft.promoexito.database.Category;
import com.ekasoft.promoexito.database.SubCategory;
import com.ekasoft.promoexito.util.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eka on 3/04/2016.
 */
public class WebInstallInterface {
    InstallActivity mContext;

    /**
     * Instantiate the interface and set the context
     */
    WebInstallInterface(InstallActivity c) {
        mContext = c;
        ActiveAndroid.initialize(mContext);
    }

    Category category;

    @JavascriptInterface   // must be added for API 17 or higher
    public void notify(String categories) {
        Log.d("eka WebAppInterface", "categories interface $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ ..." + categories);

        try {
            JSONArray jsonItems = new JSONArray(categories);
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject it = jsonItems.getJSONObject(i);

                category = new Category();
                category.name = it.getString("name").trim();
                category.save();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContext.textCategories.setText(category.name);
                    }//public void run() {
                });

                JSONArray jsonSubcategory = it.getJSONArray("list");
                for (int j = 0; j < jsonItems.length(); j++) {
                    JSONObject jSub = jsonItems.getJSONObject(j);

                    SubCategory subcategory = new SubCategory();
                    subcategory.name = it.getString("name").trim();
                    subcategory.category = category;
                    subcategory.save();
                }
            }


            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContext.textCategories.setText("Finalizó con exito");
                    Settings.set(mContext, "install", "true");
                    Intent intent = new Intent(mContext, InstallPromoActivity.class);
                    mContext.startActivity(intent);
                    mContext.finish();
                }//public void run() {
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}