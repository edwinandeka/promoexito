package com.ekasoft.promoexito.listpromo;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.activeandroid.ActiveAndroid;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.util.JSON;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by eka on 6/04/2016.
 */
public class PromoListInterface {

    Context mContext;

    /** Instantiate the interface and set the context */
    PromoListInterface(Context c) {
        mContext = c;
        ActiveAndroid.initialize(mContext);
    }
    @JavascriptInterface   // must be added for API 17 or higher
    public void notify(String data, String id) {

        Log.d("eka PromoListInterface", "dataJson ..." + data + " id: " + id);
        Map<String, String> dataJson = JSON.parse(data);

        Log.d("eka ", "dataJson ..." + "id >> "  + dataJson.get("id"));
        Log.d("eka ", "dataJson ..." + "size >> "  + dataJson.get("size"));
        Log.d("eka ", "dataJson ..." + "stockLevel >> "  + dataJson.get("stockLevel"));
        Log.d("eka ", "dataJson ..." + "sellerName >> "  + dataJson.get("sellerName"));
        Log.d("eka ", "dataJson ..." + "sellerCarrier >> "  + dataJson.get("sellerCarrier"));
        Log.d("eka ", "dataJson ..." + "sellerId >> "  + dataJson.get("sellerId"));
        Log.d("eka ", "dataJson ..." + "salePrice >> "  + dataJson.get("salePrice"));
        Log.d("eka ", "dataJson ..." + "listPrice >> "  + dataJson.get("listPrice"));
        Log.d("eka ", "dataJson ..." + "salePriceText >> "  + dataJson.get("salePriceText"));
        Log.d("eka ", "dataJson ..." + "listPriceText >> "  + dataJson.get("listPriceText"));
        Log.d("eka ", "dataJson ..." + "otherPriceText >> "  + dataJson.get("otherPriceText"));
        Log.d("eka ", "dataJson ..." + "otherPrice >> "  + dataJson.get("otherPrice"));
        Log.d("eka ", "dataJson ..." + "cardName >> "  + dataJson.get("cardName"));
        Log.d("eka ", "dataJson ..." + "startValue >> "  + dataJson.get("startValue"));
        Log.d("eka ", "dataJson ..." + "tatalOffert >> "  + dataJson.get("tatalOffert"));

        Item item = Item.load(Item.class, Long.parseLong(id));
        item.idItem = ""  + dataJson.get("id");
        item.stock = ""  + dataJson.get("stockLevel");
        item.sellerName = ""  + dataJson.get("sellerName");
        item.sellerCarrier = ""  + dataJson.get("sellerCarrier");

        String price = dataJson.get("salePrice");
        if (price != null){
            item.priceInt = Integer.parseInt(price);
            item.price = "$"  + dataJson.get("salePriceText");
        }

        String priceBefore = dataJson.get("listPrice");
        int  priceBeforeInt = Integer.parseInt(priceBefore);
            item.before = "Antes: $"  + dataJson.get("listPriceText");

        String otherPricePrice = dataJson.get("otherPriceText");
        if (otherPricePrice != null){
            item.other = "Otros medio: $"  + dataJson.get("otherPriceText");
            item.isPaymentCard = "true";
        }else{
            item.other = "";
            item.isPaymentCard = "false";
        }

        String oferta = dataJson.get("startValue");
        if (oferta != null){
            int ofertaInt = Integer.parseInt(oferta);

            item.offert =  oferta + "%";
            item.offertInt = ofertaInt;
        }else{
            item.offert = "";
        }

        item.savingInt = priceBeforeInt - item.priceInt;
        DecimalFormat formato = new DecimalFormat("###,###,###");
        String saving = formato.format(item.savingInt);
        item.saving = "" + saving;

        item.save();


    }
}
