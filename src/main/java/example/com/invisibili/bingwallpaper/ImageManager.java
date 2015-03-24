package example.com.invisibili.bingwallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by invisibili on 2015/3/24.
 */
public abstract class ImageManager {
    private static Context context;
    private static ImageView imageView;
    private static Resources resources;
    private static int pos;
    private static int MAX_POSITION;

    static{
        pos = 0;
        MAX_POSITION = 5;
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

    public static void showAbout(){
        pos = -1;
        imageView.setImageBitmap(ImageCache.getLoadingPic());
    }

    public static void navPrev(){
        UrlImageLoader work = new UrlImageLoader(context,imageView);
        DefaultDrawable drawable = new DefaultDrawable(resources,ImageCache.getLoadingPic(),work);
        imageView.setImageDrawable(drawable);
        if(pos == -1){
            pos = 0;
            work.execute("ok","1");
        }else{
            if(pos>=MAX_POSITION) return;
            pos = pos + 1;
            work.execute("ok",String.valueOf(pos+1),String.valueOf(pos));
        }
    }
}
