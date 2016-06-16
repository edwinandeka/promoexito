package com.ekasoft.promoexito.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekasoft.promoexito.MyActivity;
import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.database.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eka on 4/04/2016.
 */
public class CategoryArrayAdapter extends ArrayAdapter<Category> {

    private final MyActivity context;
    public List<Category> categories;
    public List<Category> categoriesSearch;

    static class ViewHolder {
        public LinearLayout container;
        public TextView name;
    }

    public CategoryArrayAdapter(MyActivity context, List<Category> categories) {
        super(context, R.layout.list_promo_row, categories);
        this.context = context;
        this.categories = categories;
        categoriesSearch = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.categories_list_row, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.txt_name);
            rowView.setTag(viewHolder);
        }


        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Category item = categories.get(position);
        holder.name.setText(item.name);

        holder.name.setOnClickListener(new ActionCategory(item, context));

        return rowView;
    }


    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public int getPosition(Category category) {
        return categories.indexOf(category);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    public void search(String newText) {
        List<Category> results = new ArrayList<Category>();



        if (newText != null) {
            if (categoriesSearch != null && categories.size() > 0) {
                for (final Category g : categoriesSearch) {
                    if (g.toString().toLowerCase().contains(newText.toString().toLowerCase()))
                        results.add(g);
                }
            }
            categories = results;
        }
    }
}