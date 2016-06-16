package com.ekasoft.promoexito.listpromo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.activeandroid.query.Select;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.util.Settings;

import java.util.List;

/**
 * Created by eka on 5/04/2016.
 */
public class ActionNoLike implements View.OnClickListener {

   public Item item;
   public String mCheckedItem;
    PromoArrayAdapter promoArrayAdapter;

    ActionNoLike(Item item, PromoArrayAdapter promoArrayAdapter) {
        this.item = item;
        this.promoArrayAdapter = promoArrayAdapter;
    }

    View view;
    int selected = 0;

    @Override
    public void onClick(View v) {
        view = v;
        final String[] list = new String[]{"Este producto", "todos los productos de "+ item.category.name};
        int checkedItemIndex = 0;
        selected = 0;
        mCheckedItem = list[checkedItemIndex];

        new AlertDialog.Builder(v.getContext())
                .setTitle("Qué no te interesa ?")
                .setSingleChoiceItems(list,
                        checkedItemIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected = which;
                                mCheckedItem = list[which];
                            }
                        })
                .setNegativeButton("Cancelar", new ButtonClickedListener(0))
                .setPositiveButton( "Aceptar", new ButtonClickedListener(1) )
                .show();
    }

    private class ButtonClickedListener implements DialogInterface.OnClickListener {

        int TYPE_CANCEL = 0;
        int TYPE_ACCEPT = 1;

        int type = 0;

        public ButtonClickedListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (type == TYPE_ACCEPT){

                List<Item> items;
                String order = "offert desc";
                if (Settings.isKey(view.getContext(), "promo_order_list")){
                    order = Settings.get(view.getContext(), "promo_order_list");
                }

                if (selected == 0){
                   // item.visible = false;
                    item.save();


                    items = new Select().from(Item.class).where("visible = ?", true).orderBy(order).execute();
                    promoArrayAdapter.products = items;
                    promoArrayAdapter.notifyDataSetChanged();

                }

                if (selected == 1){
                    //item.visible = false;
                    item.save();


                    items = new Select().from(Item.class).where("category = ?", item.category.getId()).execute();
                   // item.category.visible = false;
                    item.category.save();

                    for (Item t: items ) {
                       // t.visible = false;
                        t.save();
                    }

                    items = new Select().from(Item.class).where("visible = ?", true).orderBy(order).execute();
                    promoArrayAdapter.products = items;
                    promoArrayAdapter.notifyDataSetChanged();

                }
            }
        }
    }


}
