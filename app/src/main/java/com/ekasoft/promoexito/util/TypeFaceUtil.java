package com.ekasoft.promoexito.util;

import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by eka on 28/03/2016.
 */
public class TypeFaceUtil {
    public static Typeface typeface = null;

    public static void setFont(TextView v){
        if(TypeFaceUtil.typeface == null){
            TypeFaceUtil.typeface = Typeface.createFromAsset(v.getContext().getAssets(), "RobotoCondensed-Light.ttf");
        }
        v.setTypeface(TypeFaceUtil.typeface);
    }


    public static void setFont(Button v){
        if(TypeFaceUtil.typeface == null){
            TypeFaceUtil.typeface = Typeface.createFromAsset(v.getContext().getAssets(), "RobotoCondensed-Light.ttf");
        }
        v.setTypeface(TypeFaceUtil.typeface);
    }
}
