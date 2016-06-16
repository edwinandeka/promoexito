package com.ekasoft.promoexito.category;

import android.content.Intent;
import android.view.View;

import com.ekasoft.promoexito.MyActivity;
import com.ekasoft.promoexito.database.Category;
import com.ekasoft.promoexito.listpromo.ActivityPromo;
import com.ekasoft.promoexito.util.Settings;

/**
 * Created by eka on 10/04/2016.
 */
public class ActionCategory implements View.OnClickListener {

    Category category;
    MyActivity context;

    public ActionCategory(Category category, MyActivity context) {
        this.category = category;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Settings.set(context, "id_category", "" + category.getId());
        Intent intent = new Intent(context, ActivityPromo.class);
        context.startActivity(intent);
    }
}
