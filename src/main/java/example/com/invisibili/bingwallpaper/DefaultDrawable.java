package example.com.invisibili.bingwallpaper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by invisibili on 2015/3/24.
 */
public class DefaultDrawable extends BitmapDrawable {
    private final WeakReference<UrlImageLoader> loader;
    DefaultDrawable(Resources r,Bitmap bm,UrlImageLoader uil){
        super(r,bm);
        loader = new WeakReference<UrlImageLoader>(uil);
    }

    UrlImageLoader getLoader(){
        return loader.get();
    }
}
