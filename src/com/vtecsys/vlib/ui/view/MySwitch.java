package com.vtecsys.vlib.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

public class MySwitch extends Switch {
	
	public MySwitch(Context context) {
		super(context);
	}
	
	public MySwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
    public void requestLayout() {
        try {
            java.lang.reflect.Field mOnLayout = Switch.class.getDeclaredField("mOnLayout");
            mOnLayout.setAccessible(true);
            mOnLayout.set(this, null);
            java.lang.reflect.Field mOffLayout = Switch.class.getDeclaredField("mOffLayout");
            mOffLayout.setAccessible(true);
            mOffLayout.set(this, null);
        } catch (Exception e) {}
        super.requestLayout();
    }


}
