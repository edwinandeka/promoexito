package com.ekasoft.promoexito.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by eka on 4/04/2016.
 */
@Table(name = "Items")
public class Item extends Model {

    @Column(name="id_item")
    public String idItem;

    @Column(name="sku_id")
    public String skuID;

    @Column(name="link")
    public String link;

    @Column(name="price_int")
    public int priceInt;

    @Column(name="saving_int")
    public int savingInt;

    @Column(name="saving")
    public String saving;

    @Column(name="price")
    public String price;


    @Column(name="name")
    public String name;

    @Column(name="offert")
    public String offert;

    @Column(name="offert_int")
    public int offertInt;

    @Column(name="imageUrl")
    public String imageUrl;

    @Column(name="brand")
    public String brand;

    @Column(name="before")
    public String before;

    @Column(name="other")
    public String other;

    @Column(name="stock")
    public String stock;

    @Column(name="sellerName")
    public String sellerName;

    @Column(name="sellerCarrier")
    public String sellerCarrier;

    @Column(name="image")
    public byte[] image;

    @Column(name="isPaymentCard")
    public String isPaymentCard;

    @Column(name="dateCreated")
    public long dateCreated;

    @Column(name = "Category")
    public Category category;

    @Column(name = "sub_category")
    public SubCategory subcategory;

    @Override
    public String toString() {
        return name + " "+ brand  ;
    }
}