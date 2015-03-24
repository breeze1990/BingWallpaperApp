package example.com.invisibili.bingwallpaper;

import android.graphics.Bitmap;

/**
 * Created by invisibili on 2015/3/24.
 */
public class ImageCache {
    private static Bitmap loadingPic;

    public static void setLoadingPic(Bitmap p){
        loadingPic = p;
    }

    public static Bitmap getLoadingPic(){
        return loadingPic;
    }
}
