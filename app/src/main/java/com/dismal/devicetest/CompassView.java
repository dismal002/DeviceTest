package com.dismal.devicetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CompassView extends ImageView {
    private Drawable compass = null;
    private float mDirection = 0.0f;

    public CompassView(Context context) {
        super(context);
    }

    public CompassView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CompassView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.compass == null) {
            this.compass = getDrawable();
            this.compass.setBounds(0, 0, getWidth(), getHeight());
        }
        canvas.save();
        canvas.rotate(this.mDirection, (float) (getWidth() / 2), (float) (getHeight() / 2));
        this.compass.draw(canvas);
        canvas.restore();
    }

    public void updateDirection(float f) {
        this.mDirection = f;
        invalidate();
    }
}
