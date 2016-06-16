package com.ekasoft.promoexito.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by eka on 4/04/2016.
 */
@Table(name = "Categories")
public class Category extends Model {
    @Column(name = "Name")
    public String name;
}