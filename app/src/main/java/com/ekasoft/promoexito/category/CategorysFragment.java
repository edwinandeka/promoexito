package com.ekasoft.promoexito.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.ekasoft.promoexito.MyActivity;
import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.database.Category;
import com.ekasoft.promoexito.service.ServiceWithWebView;
import com.ekasoft.promoexito.util.Settings;
import com.ekasoft.promoexito.util.ViewFind;

import java.util.List;

/**
 * Created by eka on 9/04/2016.
 */
public class CategorysFragment extends Fragment {


    ListView listView;
    List<Category> categories;
    TextView findResult;

    String order;
    Context context;
    CategoryArrayAdapter arrayAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_categories_fragment,container,false);
        context = getContext();

        Intent intent = new Intent(getContext(), ServiceWithWebView.class);
        context.startService(intent);

        ViewFind find = new ViewFind(view);
        listView = find.listView(R.id.listView);
        findResult = find.textView(R.id.find_result);
        findResult.setVisibility(View.GONE);

        order = "offert desc";

        if (Settings.isKey(getContext(), "promo_order_list")){
            order = Settings.get(getContext(), "promo_order_list");
        }

        categories = new Select().from(Category.class).orderBy("name asc") .execute();
        arrayAdapter = new CategoryArrayAdapter((MyActivity)getActivity(),categories);
        listView.setAdapter(arrayAdapter);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu ) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (context != null) {
                    Toast.makeText(context, "" + query, Toast.LENGTH_SHORT).show();
                    arrayAdapter.search(query);
                    if (arrayAdapter.categories.size() > 0) {
                        findResult.setVisibility(View.GONE);
                    } else {
                        findResult.setVisibility(View.VISIBLE);
                        findResult.setText("No se encontraron resultados para \"" + query + "\"");
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("eka Categories search ", "url ..." + newText);
                if (arrayAdapter != null) {
                    arrayAdapter.search(newText);
                    if (TextUtils.isEmpty(newText)) {
                        arrayAdapter.categories = categories;
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        //arrayAdapter.search(newText.toString());
                        //arrayAdapter.getFilter().filter(newText.toString());

                        // arrayAdapter.search(newText);
                        //arrayAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        });
    }
}
