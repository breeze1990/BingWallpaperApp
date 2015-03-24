package example.com.invisibili.bingwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    public static final String PREVURL_PREFERENCE_FILE = "example.com.invisibili.bingwallpaper.PREVURL_PREFERENCE_FILE";
    public static final String Saved_Image_File = "test.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageManager.setContext(this);
        ImageView disp = (ImageView) findViewById(R.id.imgdisp);
        ImageManager.setImageView(disp);
        ImageManager.setResources(getResources());
        disp.setScaleType(ImageView.ScaleType.FIT_XY);
        Bitmap loadingPic = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        ImageCache.setLoadingPic(loadingPic);

        UrlImageLoader work = new UrlImageLoader(this,disp);
        DefaultDrawable drawable = new DefaultDrawable(getResources(),loadingPic,work);
        disp.setImageDrawable(drawable);
        if(isNetworkAvailable()){
            Toast.makeText(getApplicationContext(),"Internet connected.",Toast.LENGTH_LONG).show();
            work.execute("ok","1");
        }else{
            Toast.makeText(getApplicationContext(),"Internet not available.",Toast.LENGTH_LONG).show();
            work.execute("dc");
        }

        final ImageNavGestureListener listener = new ImageNavGestureListener(disp);
        final GestureDetector gd = new GestureDetector(this,listener);
        disp.setClickable(true);
        disp.setFocusable(true);
        disp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gd.onTouchEvent(event);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
