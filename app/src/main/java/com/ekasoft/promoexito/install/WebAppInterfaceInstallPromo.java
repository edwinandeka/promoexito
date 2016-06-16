package com.ekasoft.promoexito.install;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ekasoft.promoexito.MyActivity;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.util.ByteArrayUtil;
import com.ekasoft.promoexito.util.Services;
import com.ekasoft.promoexito.util.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by eka on 3/04/2016.
 */
public class WebAppInterfaceInstallPromo {
    InstallPromoActivity mContext;


    /**
     * Instantiate the interface and set the context
     */
    WebAppInterfaceInstallPromo(InstallPromoActivity c) {
        mContext = c;
        ActiveAndroid.initialize(mContext);
    }

    @JavascriptInterface   // must be added for API 17 or higher
    public void notify(String items) {
        new AsyncTaskSaveProducts().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, items);
    }



    class  AsyncTaskSaveProducts  extends  AsyncTask<String, String, String>{
        public  Item item;
        @Override
        protected String doInBackground(String... params) {

            Calendar cal = Calendar.getInstance();
            long date = cal.getTimeInMillis();
            try {
                JSONArray jsonItems = new JSONArray(params[0]);
                for (int i = 0; i < jsonItems.length(); i++) {
                    JSONObject it = jsonItems.getJSONObject(i);
                    Log.d("eka WebAppInterface", "category ..." +  it.getString("category").trim() + " " + it.getString("category").trim().length());

                    //SubCategory subcategory = new Select().from(SubCategory.class).where("name = ?", it.getString("category")).executeSingle();
                    //Log.d("eka WebAppInterface", "category ..." +  it.getString("category").trim() + " " + subcategory.name + " "+ (it.getString("category").trim().equals( subcategory.name)) + " ####   " + subcategory.category.name);

                    item = new Select().from(Item.class).where("sku_id = ?", it.getString("id")).executeSingle();
                    if (item == null) {
                        item = new Item();
                        item.skuID = it.getString("id");

                        item.link = it.getString("link");
                        item.idItem = getProductId(item.link);
                        item.price = it.getString("price");

                        String price = it.getString("price");
                        price = price.replace("$", "");
                        price = price.replace(".", "");
                        price = price.replace(".", "");
                        price = price.replace(".", "");
                        price = price.replace(".", "");

                        if (price.equals("")) {
                            continue;
                        }
                        item.priceInt = Integer.parseInt(price);
                        item.name = it.getString("name");

                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mContext.textCategories.setText("" + item.name);
                            }//public void run() {
                        });
                        item.imageUrl = it.getString("imageUrl");
                        item.image = ByteArrayUtil.getBytesURL(Services.BASE_URL + item.imageUrl);
                        item.brand = it.getString("brand");
                        item.before = it.getString("before");
                        item.offert = it.getString("offert");


                        String before = it.getString("before");
                        if (before.equals("")) {
                            continue;
                        }
                        before = before.replace("$", "");
                        before = before.replace(".", "");
                        before = before.replace(".", "");
                        before = before.replace("Antes:  ", "");

                        int beforeInt = Integer.parseInt(before);
                        if (item.offert.equals("")) {
                            item.offert = "" + (100 - ((int) (item.priceInt * 100) / beforeInt)) + "%";
                        }

                        String offert = item.offert.trim();

                        offert = offert.replace("%", "");
                        item.offertInt = Integer.parseInt(offert);

                        Log.d("eka offert", "offert: " + offert + '\n');

                        item.savingInt = beforeInt - item.priceInt;


                        DecimalFormat formato = new DecimalFormat("###,###,###");
                        String saving = formato.format(item.savingInt);


                        item.other = it.getString("other");
                        saving = saving.replace(",", ".");
                        saving = saving.replace(",", ".");
                        saving = saving.replace(",", ".");

                        item.saving = saving;
                        item.isPaymentCard = it.getString("isPaymentCard");
                        // item.subcategory = subcategory;
                        // item.category = subcategory.category;
                        item.dateCreated = date;

                        item.save();

                        Log.d("eka WebAppInterface", "item.name ..." + item.name);

                    }else{
                        item.imageUrl = it.getString("imageUrl");
                        if (item.imageUrl.contains("imgNotFound")){
                            item.image = ByteArrayUtil.getBytesURL(Services.BASE_URL + item.imageUrl);
                            item.save();
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Settings.set(mContext, "install", "true");
            Settings.set(mContext, "promo_count_page", "80");
            Intent intent = new Intent(mContext, MyActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        }

        public String getProductId(String url){
            // /products/0001400932211571/Folio+Mini+Ipad+IfolB-307+Negr?nocity
            url = url.replace("/products/", "");
            int index = url.indexOf("/");
            url = url.substring(0,index);
            return url;
        }
    }
}