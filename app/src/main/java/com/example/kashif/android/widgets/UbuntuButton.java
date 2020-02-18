package com.example.kashif.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.kashif.android.R;

import androidx.appcompat.widget.AppCompatButton;

public final class UbuntuButton extends AppCompatButton {
    public UbuntuButton(Context context) {
        super(context);
        init(null, context);
    }

    public UbuntuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public UbuntuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        if (attrs != null && context != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UBTextView);
            int type = a.getInt(R.styleable.UBTextView_viewtypeUB, 0);
            Typeface myTypeface;
            switch (type) {
                case 0://Regular
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu-regular.ttf");
                    break;
                case 1://light
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu-light.ttf");
                    break;
                case 2://SemiBold
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
