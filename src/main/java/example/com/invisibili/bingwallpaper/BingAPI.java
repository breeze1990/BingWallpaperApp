package example.com.invisibili.bingwallpaper;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by invisibili on 2015/3/23.
 */
public class BingAPI extends AsyncTask<Void,Void,Void> {
    public static final String bing_wp_api = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&mkt=en-US";
    public static final String bing_dn = "http://www.bing.com";
    public static final String resolution_normal = "720x1280";
    public static final String resolution_low = "480x800";
    private static String[] storedList = null;
    private static String[] crList = null;
    public int numUrls;
    BingAPI(int num){
        numUrls = num;
    }
    public static String[] getStoredList(){
        return storedList;
    }
    public static String[] getCopyrightList() { return crList; }
    public static void getWallpaperUrls(int num){
        if(num<1) return;
        String api = bing_wp_api + "&n=" + num;
        String body = null;
        URL obj = null;
        HttpURLConnection conn = null;
        try {
            obj = new URL(api);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent","AppWallpaperGetter/1.0");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            String inputLine;
            StringBuffer res = new StringBuffer();
            while((inputLine = br.readLine()) != null){
                res.append(inputLine);
            }
            br.close();
            body = res.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }
        // parse body
        if(body == null) return;
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(body);
        JsonArray ja = jo.getAsJsonArray("images");
        int len = ja.size();
        String[] urls = new String[len];
        crList = new String[len];
        for(int i=0;i<len;i++){
            JsonObject info = (JsonObject)ja.get(i);
            String tmp = bing_dn + info.get("url").getAsString();
            urls[i] = tmp.replace("1920x1080",resolution_normal);
            crList[i] = info.get("copyright").getAsString();
        }
        storedList = urls;
        Log.d("BingApi","Url List obtained");
    }

    @Override
    protected Void doInBackground(Void... params) {
        getWallpaperUrls(numUrls);
        return null;
    }
}
