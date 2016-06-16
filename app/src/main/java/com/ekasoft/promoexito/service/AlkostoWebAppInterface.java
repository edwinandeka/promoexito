package com.ekasoft.promoexito.service;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ekasoft.promoexito.database.Category;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.util.ByteArrayUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by eka on 3/04/2016.
 */
public class AlkostoWebAppInterface {
    Context mContext;

    public static String BASE_URL = "http://www.exito.com";

    /** Instantiate the interface and set the context */
    AlkostoWebAppInterface(Context c) {
        mContext = c;
        ActiveAndroid.initialize(mContext);
    }

    @JavascriptInterface   // must be added for API 17 or higher
    public void notify(String items) {
        Log.d("eka WebAppInterface", "WebAppInterface ...");
        Calendar cal = Calendar.getInstance();
        long date = cal.getTimeInMillis();
        try {
        JSONArray jsonItems = new JSONArray(items);
        for(int i = 0; i < jsonItems.length(); i++){
            JSONObject it = jsonItems.getJSONObject(i);

            Category category = new Select().from(Category.class).where("name = ?", it.getString("category")).executeSingle();
            if (category==null){
                category = new Category();
                category.name = it.getString("category");
                category.save();
            }

            Item item = new Select().from(Item.class).where("id_item = ?", it.getString("id")).executeSingle();
            if (item == null){
                item = new Item();
                item.idItem = it.getString("id");
                item.link = it.getString("link");
                item.price = it.getString("price");

                String price = it.getString("price");
                price = price.replace("$","");
                price = price.replace(".", "");
                price = price.replace(".", "");
                price = price.replace(".", "");
                price = price.replace(".", "");

                if (price.equals("")){
                    continue;
                }
                item.priceInt = Integer.parseInt(price);
                item.name = it.getString("name");
                item.imageUrl = it.getString("imageUrl");
                item.image = ByteArrayUtil.getBytesURL(BASE_URL + item.imageUrl);
                item.brand = it.getString("brand");
                item.before = it.getString("before");
                item.offert = it.getString("offert");



                String before = it.getString("before");
                before = before.replace("$", "");
                before = before.replace(".", "");
                before = before.replace(".", "");
                before = before.replace("Antes:  ", "");

                int beforeInt = Integer.parseInt(before);
                if (item.offert.equals("")){
                    item.offert = ""+(100-((int)(item.priceInt*100)/beforeInt)) + "%";
                }

                String offert = item.offert.trim();

                offert = offert.replace("%", "");
                item.offertInt =  Integer.parseInt(offert);

                Log.d("eka offert", "offert: " +  offert  + '\n');

                item.savingInt = beforeInt - item.priceInt;


                DecimalFormat formato = new DecimalFormat("###,###,###");
                String saving = formato.format(item.savingInt);


                item.other = it.getString("other");
                saving = saving.replace(",", ".");
                saving = saving.replace(",", ".");
                saving = saving.replace(",", ".");

                item.saving = saving;
                item.isPaymentCard = it.getString("isPaymentCard");
                item.category = category;
                item.dateCreated = date;

                item.save();

            }
            String name = jsonItems.getJSONObject(i).getString("name");
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        Intent resultIntent = new Intent(mContext, MyActivity.class);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Prepare intent which is triggered if the  notification button is pressed

            NotificationManager  mNotificationManager =
                    (NotificationManager) mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intent = new Intent(mContext, MyActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

            // Building the notifcation
            android.support.v4.app.NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.icono) // notification icon
                    .setContentTitle(name) // notification title
                    .setContentText(price) // content text
                    .setTicker("Promoción exito.com"); // status bar message

            mNotificationManager.notify(1001, nBuilder.build());

        } else {
            Toast.makeText(mContext, "You need a higher version", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(mContext, "" + name + " " + price + " " +link, Toast.LENGTH_SHORT).show();*/
    }
}