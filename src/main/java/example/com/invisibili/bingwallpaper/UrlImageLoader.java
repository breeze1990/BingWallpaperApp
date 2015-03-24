package example.com.invisibili.bingwallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by invisibili on 2015/3/23.
 */
public class UrlImageLoader extends AsyncTask<String,Void,Bitmap> {
    private final WeakReference<ImageView> wrefImg;
    private final Context context;
    private String cur_url;

    public String getCurrentUrl(){
        return cur_url;
    }
    public UrlImageLoader(Context context,ImageView iv){
        this.context = context;
        wrefImg = new WeakReference<ImageView>(iv);
        cur_url = new String("");
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String status = params[0];
        int count = 1;
        switch (status){
            case "dc":
                return ImageManager.loadFromFile();
            case "ok":
                count = Integer.parseInt(params[1]);
        }
        String[] sarr = BingAPI.getWallpaperUrls(count);
        if(sarr == null) return ImageManager.loadFromFile();
        String imgurl = null;
        if(params.length <3 ) imgurl = sarr[0];
        else imgurl = sarr[Integer.parseInt(params[2])];
        cur_url = imgurl;
        Log.d("url:",imgurl);
        SharedPreferences sp = context.getSharedPreferences(MainActivity.PREVURL_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String p_url = sp.getString(context.getString(R.string.previous_url_key),"");
        if(p_url.equals(imgurl)) {
            return ImageManager.loadFromFile();
        }
        try {
            Bitmap pic = BitmapFactory.decodeStream((InputStream)new URL(imgurl).getContent());
            FileOutputStream out = null;
            try {
                out = context.openFileOutput(MainActivity.Saved_Image_File,Context.MODE_PRIVATE);
                pic.compress(Bitmap.CompressFormat.PNG, 100, out);
                SharedPreferences.Editor se = sp.edit();
                se.putString(context.getString(R.string.previous_url_key),imgurl);
                se.commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return pic;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (wrefImg != null && bitmap != null) {
            final ImageView imageView = wrefImg.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }



}
