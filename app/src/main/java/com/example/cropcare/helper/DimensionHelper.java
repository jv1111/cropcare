package com.example.cropcare.helper;

import android.content.Context;

public class DimensionHelper {

    public static int convertToDp(Context context, int valueInDp) {
        return (int) (valueInDp * context.getResources().getDisplayMetrics().density);
    }

}
