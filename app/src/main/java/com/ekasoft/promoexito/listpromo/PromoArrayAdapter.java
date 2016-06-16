package com.ekasoft.promoexito.listpromo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ekasoft.promoexito.R;
import com.ekasoft.promoexito.database.Item;
import com.ekasoft.promoexito.util.ByteArrayUtil;
import com.ekasoft.promoexito.util.Settings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eka on 4/04/2016.
 */
public class PromoArrayAdapter  extends ArrayAdapter<Item> {
    private final Activity context;
    public  List<Item> products;
    public  List<Item> productsSearch;

    static class ViewHolder {
        public LinearLayout container;
        public TextView name;
        public TextView count;
        public TextView price;
        public TextView promo;
        public TextView brand;
        public TextView before;
        public TextView saving;
        public TextView other;
        public TextView time;
        public ImageView image;
        public ImageView tarjeta;
        public ImageView star;
        public RelativeLayout noStock;
        public RelativeLayout noAvalibe;



        public TextView isPaymentCard;
        public long dateCreated;
    }

    public PromoArrayAdapter(Activity context, List<Item> products) {
        super(context, R.layout.list_promo_row, products);
        this.context = context;
        this.products = products;
        productsSearch = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_promo_row, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.txt_name);
            viewHolder.count = (TextView) rowView.findViewById(R.id.count);

            viewHolder.price = (TextView) rowView.findViewById(R.id.txt_price);
            viewHolder.promo = (TextView) rowView.findViewById(R.id.txt_promo);
            viewHolder.brand = (TextView) rowView.findViewById(R.id.txt_brand);
            viewHolder.before = (TextView) rowView.findViewById(R.id.txt_before);
            viewHolder.other = (TextView) rowView.findViewById(R.id.other);
            viewHolder.saving = (TextView) rowView.findViewById(R.id.saving);
            viewHolder.time = (TextView) rowView.findViewById(R.id.time);
            viewHolder.image = (ImageView) rowView.findViewById(R.id.image);
            viewHolder.tarjeta = (ImageView) rowView.findViewById(R.id.tarjeta);
            viewHolder.star = (ImageView) rowView.findViewById(R.id.star);
            viewHolder.container = (LinearLayout) rowView.findViewById(R.id.container);
            viewHolder.noStock = (RelativeLayout) rowView.findViewById(R.id.no_stock);
            viewHolder.noAvalibe = (RelativeLayout) rowView.findViewById(R.id.no_avalible);
            rowView.setTag(viewHolder);
        }


        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Item item = products.get(position);
        holder.noStock.setVisibility(View.GONE);

        if (item.stock != null)
            if (item.stock.equals("0")) {
                holder.noStock.setVisibility(View.VISIBLE);
            } else {
                holder.noStock.setVisibility(View.GONE);
            }


        if (item.saving != null)
            if (item.saving.equals("0")) {
                holder.noAvalibe.setVisibility(View.VISIBLE);
            } else {
                holder.noAvalibe.setVisibility(View.GONE);
            }

        holder.count.setText("" + (position +1) );

        holder.name.setText(item.name);
        holder.price.setText(item.price);
        holder.brand.setText(item.brand);
        holder.before.setText(item.before);
        holder.promo.setText(item.offert);
        holder.time.setText(getTime(item.dateCreated));
        holder.saving.setText("Ahorre: $" + item.saving);



        if (item.other.equals("")){
            holder.other.setVisibility(View.GONE);
        }else {
            holder.other.setText(item.other);
            holder.other.setVisibility(View.VISIBLE);
        }

        if (item.isPaymentCard.equals("false")){
            holder.tarjeta.setVisibility(View.GONE);
        }else {
            holder.tarjeta.setVisibility(View.VISIBLE);
        }


        holder.image.setImageBitmap(ByteArrayUtil.getImage(item.image));

        if (item.offert.equals("")){
            holder.star.setVisibility(View.GONE);
        }else {
            holder.star.setVisibility(View.VISIBLE);
        }

        holder.container.setOnClickListener(new ActionListItem(item));
        return rowView;
    }

    public String getTime(long date) {
        Date date1Current = new Date();
        Date dateT = new Date(date);

        long timeC = date1Current.getTime() - dateT.getTime();

        timeC /= 1000;

        if (timeC < 60) {
            return timeC + " segundo"+((timeC>1)? "s": "" );
        } else {
            //convierto en minutos
            timeC /= 60;
            if (timeC < 60) {
                return timeC + " mimuto"+((timeC>1)? "s": "" );
            } else {
                //convierto en en horas
                timeC /= 60;
                if (timeC < 24) {
                    return timeC + " hora"+((timeC>1)? "s": "" );
                } else {
                    //convierto en dias
                    timeC /= 24;
                    if (timeC < 7) {
                        return timeC + " día"+ ((timeC>1)? "s": "" );
                    } else {
                        timeC /= 7;
                        return timeC + " sememana"+((timeC>1)? "s": "" );
                    }
                }
            }
        }

    }

    @Override
    public Item getItem(int position) {
        return products.get(position);
    }

    @Override
    public int getPosition(Item item) {
        return products.indexOf(item);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    public void search(String newText, List<Item> items) {
        List<Item> results = new ArrayList<Item>();

        if (newText != null) {
            if (items != null && products.size() > 0) {
                for (final Item g : items) {
                    if (g.toString().toLowerCase().contains(newText.toString()))
                        results.add(g);
                }
            }
            products = results;
        }
    }
}