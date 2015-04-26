package example.com.invisibili.bingwallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by invisibili on 2015/3/25.
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(MainActivity.isNetworkAvailable(context) && BingAPI.getStoredList()==null){
            Log.d("RECV","Connectivity change detected, start downloading url list");
            new BingAPI(MainActivity.MAX_CNT-1).execute();
        }
    }
}
