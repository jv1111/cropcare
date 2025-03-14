package com.example.cropcare.helper;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.cropcare.R;

public class AnimationHelper {

    public static void popupLinear(LinearLayout layout, Context context){
        Animation scaleUp;
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
        layout.startAnimation(scaleUp);
    }

}
