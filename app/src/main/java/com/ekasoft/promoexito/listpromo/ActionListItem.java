package com.ekasoft.promoexito.listpromo;

import android.content.Intent;
import android.view.View;

import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.detailpromo.DetailsActivity;

/**
 * Created by eka on 5/04/2016.
 */
public class ActionListItem implements View.OnClickListener {

   public Item item;

    ActionListItem (Item item) {
        this.item = item;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), DetailsActivity.class);
        intent.putExtra("id", item.getId());
        v.getContext().startActivity(intent);
    }
}
