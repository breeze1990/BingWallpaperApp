package example.com.invisibili.bingwallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by invisibili on 2015/3/24.
 */
public abstract class ImageManager {
    private static Context context;
    private static ImageView imageView;
    private static Resources resources;
    private static int pos;
    private static int MAX_POSITION;
    public static final String URL_LIST_NOT_READY = "waiting";

    static{
        pos = 0;
        MAX_POSITION = 5;
    }

    public static void loadImage(int pos, ImageView view, TextView descView, View imgLayout, Configuration conf){
        if(BingAPI.getStoredList() == null) {
            UrlImageLoader loader = new UrlImageLoader(null, view, descView, imgLayout);
            loader.execute(URL_LIST_NOT_READY,String.valueOf(pos));
            Log.d("IMG","list is null, pos:"+pos);
            return;
        };
        Log.d("IMG","POSITION: "+pos+" loading.");
        String url = BingAPI.getStoredList()[pos];
        if(conf.orientation == conf.ORIENTATION_LANDSCAPE) {
            Log.d("IMG MG:","landscape: "+url);
            url = url.replace(BingAPI.resolution_normal, BingAPI.resolution_normal_landscape);
            Log.d("IMG MG URL", url);
        }
        ImageCache cache = ImageCache.getInstance();
        Bitmap bm = cache.getMemCache(url);
        if(bm != null) {
            view.setImageBitmap(bm);
            descView.setText(BingAPI.getCopyrightList()[pos]);
            imgLayout.setVisibility(View.GONE);
        }
        else {
            Log.d("IMG","POSITION: "+pos+" loader executing.");
            UrlImageLoader loader = new UrlImageLoader(null, view, descView, imgLayout);
            loader.execute(url,BingAPI.getCopyrightList()[pos]);
        }
    }
    public static void setResources(Resources r){
        resources = r;
    }
    public static void setImageView(ImageView v){
        imageView = v;
    }
    public static void setContext(Context c){
        context = c;
    }
    private static boolean cancelPotentialLoader(String u, ImageView v){
        UrlImageLoader loader = getLoaderFromView(v);
        if(loader != null){
            String cu = loader.getCurrentUrl();
            if(cu == null || !cu.equals(u)){
                loader.cancel(true);
            }
            else return false;
        }
        return true;
    }
    private static UrlImageLoader getLoaderFromView(ImageView v){
        if(v != null){
            Drawable d = v.getDrawable();
            if(d instanceof DefaultDrawable){
                return ((DefaultDrawable) d).getLoader();
            }
        }
        return null;
    }
    public static Bitmap loadFromFile(){
        // load pic from file
        Log.d("img:", "local file");
        FileInputStream in = null;
        try {
            in = context.openFileInput(MainActivity.Saved_Image_File);
            if(in == null) return null;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            Log.d("img:","file not exists");
            return null;
        }
        return BitmapFactory.decodeStream(in);
    }

    public static void setWallPaper(ImageView view){
        WallpaperManager wm = WallpaperManager.getInstance(context);
        Drawable d = view.getDrawable();
        Bitmap p;
        if(d instanceof BitmapDrawable) p = ((BitmapDrawable) d).getBitmap();
        else return;
        try {
            if(p != null) wm.setBitmap(p);
            Log.d("WP","Wallpaper set.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
