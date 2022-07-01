package com.rubean.interviewGame.utils;

import android.content.Context;
import android.widget.Toast;

public class Utilities {

    public static void showToast(String msg, Context context){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
