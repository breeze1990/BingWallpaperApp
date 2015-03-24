package example.com.invisibili.bingwallpaper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    public static final String PREVURL_PREFERENCE_FILE = "example.com.invisibili.bingwallpaper.PREVURL_PREFERENCE_FILE";
    public static final String Saved_Image_File = "test.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 480*800
        // http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US
        setContentView(R.layout.activity_main);
        ImageView disp = (ImageView) findViewById(R.id.imgdisp);
        TextView tv = (TextView) findViewById(R.id.desc);
        disp.setScaleType(ImageView.ScaleType.FIT_XY);
        UrlImageLoader work = new UrlImageLoader(this,disp);
        if(isNetworkAvailable()){
            Toast.makeText(getApplicationContext(),"Internet connected.",Toast.LENGTH_LONG).show();
            work.execute("ok","1");
        }else{
            Toast.makeText(getApplicationContext(),"Internet not available.",Toast.LENGTH_LONG).show();
            work.execute("dc");
        }
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
