package com.ekasoft.promoexito.listpromo;

import android.content.Context;
import android.os.AsyncTask;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.util.JSON;
import com.ekasoft.promoexito.util.RequestHttp;
import com.ekasoft.promoexito.util.Services;
import com.ekasoft.promoexito.util.Settings;
import com.ekasoft.promoexito.util.ViewFind;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by eka on 4/04/2016.
 */
public class FragmentListPromo extends Fragment {

    public ListView listView;
    public List<Item> items;

    public TextView btnMoreProducts;
    public ProgressBar progressBar;
    public RelativeLayout footer;

    public List<Item> itemsTemp;
    public WebView mWebView;
    public TextView validarOfertas;
    public TextView findResult;
    public ProgressBar progressValidate;

    Item item;
    String order;
    Context context;
    PromoArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_promo_fragment, container, false);
        context = getContext();

        ViewFind find = new ViewFind(view);
        listView = find.listView(R.id.listView);
        findResult = find.textView(R.id.find_result);
        progressValidate = find.progressbar(R.id.progress_validate);
        validarOfertas = find.textView(R.id.validar_ofertas);

        findResult.setVisibility(View.GONE);
        validarOfertas.setVisibility(View.GONE);
        progressValidate.setVisibility(View.GONE);

        order = "offert desc";

        String category = Settings.get(context, "id_category");

        if (Settings.isKey(getContext(), "promo_order_list")) {
            order = Settings.get(getContext(), "promo_order_list");
        }

        //items = new Select().from(Item.class).where("category = ?", category).orderBy(order) .execute();
        items = new Select().from(Item.class).orderBy(order).execute();
        arrayAdapter = new PromoArrayAdapter(getActivity(), items);
        listView.setAdapter(arrayAdapter);

        // Toast.makeText(getContext(), "Encontré para tí " + items.size() + " ofertas", Toast.LENGTH_SHORT).show();

        if (Settings.isKey(getContext(), "promo_order")) {
            String inOrder = Settings.get(getContext(), "promo_order");
            if (inOrder.equals("false")) {
                //validarOfertas.setVisibility(View.VISIBLE);
                //progressValidate.setVisibility(View.VISIBLE);
                //progressValidate.setProgress(0);
                //refreshListPromo();
            }
        }

        View viewFoter = inflater.inflate(R.layout.list_footer_more_products, container, false);
        find = new ViewFind(viewFoter);

        listView.addFooterView(viewFoter);

        btnMoreProducts = find.textView(R.id.btnMore);
        progressBar = find.progressbar(R.id.progressBar);
        footer = find.relativeLayout(R.id.footer);


        btnMoreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreProducts();
            }
        });


        return view;
    }

    public void moreProducts() {
        progressBar.setVisibility(View.VISIBLE);
        btnMoreProducts.setText("Cargando ...");

        Settings.set(context, "promo_count_list", "" + items.size());

        mWebView = new WebView(context);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //Toast.makeText(context, "Buscando ofertas", Toast.LENGTH_SHORT).show();

        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript:" +
                                "var products = $('.product'); var arrProducts = []; $.each(products, function(index, product) { product = $(product); var link = product.find('a').attr('href'); var price = product.find('.price').text().trim(); var name = product.find('.name').text().trim(); var offert = product.find('.supply').text().trim(); var imageUrl = product.find('.img-responsive').attr('src').trim(); var id = product.find('.product-row').attr('data-skuid').trim(); var brand = product.find('.brand').text().trim(); var before = product.find('.before').text().trim(); var other = product.find('.other').text().trim(); var iconPaymentCard = product.find('.iconPaymentCard').length; var category = product.find('.product-row').attr('data-categoryparent').trim(); var available = product.find('.available').length; arrProducts.push({ link: link, price: price, name: name, offert: offert, imageUrl: imageUrl, brand: brand, before: before, other: other, id: id, category:category, isPaymentCard: (iconPaymentCard)? true : false, available: (available)? false : true, }); }); Android.notify(JSON.stringify( arrProducts));"
                );
                super.onPageFinished(view, url);
            }
        });

        mWebView.addJavascriptInterface(new WebAppInterfaceMoreProducts(context, this), "Android");

        int count = 0;
        if (Settings.isKey(context, "promo_count_page")) {
            count = Integer.parseInt(Settings.get(context, "promo_count_page"));

            if (count > 800) {
                count = 0;
            } else {
                count += 20;
            }
        }
        Settings.set(context, "promo_count_page", "" + count);

        mWebView.loadUrl("http://www.exito.com/browse?No=" + count + "&Nrpp=20&Ns=product.priceSortExito%7C1&Ntt=0f3r7a2_d3l_d1a");


    }


    public void refreshListPromo() {

        /*
        *
        * {
            "id": "0001999069505430",
            "skuInfos": [{
                "id": "0001999070727838",
                "size": "",
                "stockLevel": 46,
                "sellerName": "Exito",
                "sellerCarrier": "Coordinadora",
                "sellerId": "34",
                "salePrice": 22685.0,
                "listPrice": 34900.0,
                "salePriceText": "22.685",
                "listPriceText": "34.900",
                "otherPriceText": "26.175",
                "otherPrice": 26175.0,
                "cardName": "exito",
                "startValue": "35",
                "tatalOffert": 1,
                "colorInfos": [{}]
            }]
        }


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String ids = "";
            for (Item item: items ) {
                if (!ids.equals("")){
                    ids += ",";
                }

                ids += getProductId(item.link);
            }
            Log.d("eka ids", "" + ids);

            RequestHttp http = new RequestHttp(Services.BASE_URL_SERVICES, RequestHttp.RESQUEST_POST);
            http.addParam("route", Services.SERVICE_INFO_PRODUCT + ids);
            http.addParam("token_app", Services.TOKEN_APP);

            String s = http.send();
            Log.d("eka ids", "" + s);

        }*/

        new AsyncTaskGetInfo().execute();

      /*  itemsTemp = items;
        if (itemsTemp.size() > 0) {
            item = itemsTemp.get(0);
        }else{
            return;
        }

        mWebView = new WebView(getContext());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("eka PromoListInterface", "url ..." + url);
                mWebView.loadUrl("javascript:" +
                                "Android.notify(JSON.stringify( JSON.parse(document.getElementsByTagName('pre')[0].innerText).skuInfos[0] ), '" + item.getId() + "');"
                );
                if (itemsTemp.size() > 0)
                    itemsTemp.remove(0);

                if (itemsTemp.size() > 0) {
                    item = itemsTemp.get(0);
                    mWebView.loadUrl("http://www.exito.com/json/jsonData.jsp?action=productInfo&productId=" + getProductId(item.link));
                } else {
                    items = new Select().from(Item.class).orderBy(order) .execute();
                    listView.setAdapter(new PromoArrayAdapter(getActivity(), items));
                    validarOfertas.setVisibility(View.GONE);
                }


                super.onPageFinished(view, url);
            }
        });


        mWebView.addJavascriptInterface(new PromoListInterface(getContext()), "Android");
        mWebView.loadUrl("http://www.exito.com/json/jsonData.jsp?action=productInfo&productId=" + getProductId(item.link));*/

    }

    public String getProductId(String url) {
        // /products/0001400932211571/Folio+Mini+Ipad+IfolB-307+Negr?nocity
        url = url.replace("/products/", "");
        int index = url.indexOf("/");
        url = url.substring(0, index);
        return url;
    }

    class AsyncTaskGetInfo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            int size = items.size();
            int i = 0;

            for (Item item : items) {
                i++;
                int prog = (int) (100 * i) / size;
                publishProgress(prog);
                String id = getProductId(item.link);

                RequestHttp http = new RequestHttp(Services.BASE_URL_SERVICES, RequestHttp.RESQUEST_POST);
                http.addParam("route", Services.SERVICE_INFO_PRODUCT + id);
                http.addParam("token_app", Services.TOKEN_APP);
                http.setTimeOut(240000);

                String json = http.send();

                try {
                    if (json.contains("{")) {

                        JSONObject object = new JSONObject(json);

                        String string = object.toString();
                        Map<String, String> dataJson = JSON.parse(string);

                        Log.d("eka validando", "#####################################################" + item.name);

                        item.idItem = "" + dataJson.get("id");
                        item.stock = "" + dataJson.get("stockLevel");
                        if (item.stock.equals("0")) {
                            item.save();
                            continue;
                        }
                        item.sellerName = "" + dataJson.get("sellerName");
                        item.sellerCarrier = "" + dataJson.get("sellerCarrier");

                        String price = dataJson.get("salePrice");
                        if (price != null) {
                            item.priceInt = (int) Double.parseDouble(price);
                            item.price = "$" + dataJson.get("salePriceText");
                        }

                        String priceBefore = dataJson.get("listPrice");
                        int priceBeforeInt = (int) Double.parseDouble(priceBefore);
                        item.before = "Antes: $" + dataJson.get("listPriceText");

                        String otherPricePrice = dataJson.get("otherPriceText");
                        if (otherPricePrice != null) {
                            item.other = "Otros medio: $" + dataJson.get("otherPriceText");
                            item.isPaymentCard = "true";
                        } else {
                            item.other = "";
                            item.isPaymentCard = "false";
                        }

                        String oferta = dataJson.get("startValue");
                        if (oferta != null) {
                            int ofertaInt = (int) Double.parseDouble(oferta);

                            item.offert = oferta + "%";
                            item.offertInt = ofertaInt;
                        } else {
                            item.offert = "";
                        }

                        item.savingInt = priceBeforeInt - item.priceInt;
                        DecimalFormat formato = new DecimalFormat("###,###,###");
                        String saving = formato.format(item.savingInt);
                        item.saving = "" + saving;

                        item.save();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return "";

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressValidate.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String jsonArray) {
            super.onPostExecute(jsonArray);

            Toast.makeText(context, "Promociones actualizadas", Toast.LENGTH_SHORT).show();
            Settings.set(context, "promo_order", "false");
            validarOfertas.setVisibility(View.GONE);


        }
    }


    public void onCreateOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (context != null) {
                    Toast.makeText(context, "" + query, Toast.LENGTH_SHORT).show();
                    arrayAdapter.search(query, items);
                    if (arrayAdapter.products.size() > 0) {
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
            public boolean onQueryTextChange(String query) {
                Log.d("eka onQueryTextChange", "url ..." + query);
                if (arrayAdapter != null) {
                    if (TextUtils.isEmpty(query)) {
                        arrayAdapter.products = items;
                        arrayAdapter.notifyDataSetChanged();
                        footer.setVisibility(View.VISIBLE);
                    } else {
                        footer.setVisibility(View.GONE);
                        arrayAdapter.getFilter().filter(query.toString());
                        arrayAdapter.search(query, items);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    if (arrayAdapter.products.size() > 0) {
                        findResult.setVisibility(View.GONE);
                    } else {
                        findResult.setVisibility(View.VISIBLE);
                        findResult.setText("No se encontraron resultados para \"" + query + "\"");
                    }
                }


                return true;
            }
        });
    }

}