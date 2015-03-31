package example.com.invisibili.bingwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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
        String imgurl = params[0];
        if(imgurl.equals(ImageManager.URL_LIST_NOT_READY)){
            int pos = Integer.parseInt(params[1]);
            while(BingAPI.storedList==null){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            imgurl = BingAPI.storedList[pos];
        }
        cur_url = imgurl;
        Log.d("url:",imgurl);
        try {
            Bitmap pic = BitmapFactory.decodeStream((InputStream) new URL(imgurl).getContent());
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
                ImageCache cache = ImageCache.getInstance();
                cache.addMemCache(cur_url,bitmap);
            }
        }
    }

}
