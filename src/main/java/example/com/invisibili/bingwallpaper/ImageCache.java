package example.com.invisibili.bingwallpaper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;

/**
 * Created by invisibili on 2015/3/24.
 */
public class ImageCache {
    private static Bitmap loadingPic;
    private static ImageCache cacheObj;
    private static LruCache<String,Bitmap> memCache;

    public static void init(int num,final Resources resources){
        memCache = new LruCache<String,Bitmap>(num);
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                loadingPic = BitmapFactory.decodeResource(resources,R.drawable.loading);
                return null;
            }
        }.execute();
    }
    public static ImageCache getInstance(){
        if(cacheObj == null){
            cacheObj = new ImageCache();
        }
        return cacheObj;
    }

    public synchronized void addMemCache(String key, Bitmap bm){
        if(memCache!=null) memCache.put(key,bm);
    }
    public synchronized Bitmap getMemCache(String key){
        if(memCache!=null) return memCache.get(key);
        else return null;
    }
    public static Bitmap getLoadingPic(){
        return loadingPic;
    }
}
