package com.example.kashif.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.kashif.android.R;

import androidx.appcompat.widget.AppCompatTextView;

public class UbuntuTextView extends AppCompatTextView {
    public UbuntuTextView(Context context) {
        super(context);
        init(null, context);
    }

    public UbuntuTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public UbuntuTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        if (attrs != null && context != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UBTextView);
            int type = a.getInt(R.styleable.UBTextView_viewtypeUB, 0);
            Typeface myTypeface;
            switch (type) {
                case 0://book
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu-regular.ttf");
                    break;
                case 1://light
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu-light.ttf");
                    break;
                case 2://medium
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu-medium.ttf");
                    break;
                case 3://bold
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu-bold.ttf");
                    break;
                default:
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu-regular.ttf");
                    break;
            }
            setTypeface(myTypeface);
            a.recycle();
        }
    }

}
