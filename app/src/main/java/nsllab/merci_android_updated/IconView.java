package nsllab.merci_android_updated;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Moallim on 12/11/2017.
 */

public class IconView extends AppCompatTextView {
    public IconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public IconView(Context context) {
        super(context);
    }
    public void setTypeface(Typeface tf) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf"));
    }
}
