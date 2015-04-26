package example.com.invisibili.bingwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String desc;
    private final WeakReference<TextView> wrefDesc;

    public String getCurrentUrl(){
        return cur_url;
    }
    public UrlImageLoader(Context context,ImageView iv, TextView descView){
        this.context = context;
        wrefImg = new WeakReference<ImageView>(iv);
        cur_url = new String("");
        wrefDesc = new WeakReference<TextView>(descView);
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        String imgurl = params[0];
        String desc = params[1];
        if(imgurl.equals(ImageManager.URL_LIST_NOT_READY)){
            int pos = Integer.parseInt(params[1]);
            while(BingAPI.getStoredList()==null){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            imgurl = BingAPI.getStoredList()[pos];
            desc = BingAPI.getCopyrightList()[pos];
        }
        cur_url = imgurl;
        this.desc = desc;
        Log.d("url:", imgurl);
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
        if (wrefDesc != null) {
            final TextView tv = wrefDesc.get();
            if(tv != null) {
                tv.setText(desc);
            }
        }
    }

}
