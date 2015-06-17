package example.com.invisibili.bingwallpaper;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by invisibili on 2015/6/9.
 */
public class RetainedFragment extends Fragment {
    // data object we want to retain
    private ImageCache imageCache;
    private BingAPI bingAPI;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setImageCache(ImageCache cache) {
        this.imageCache = cache;
    }

    public ImageCache getImageCache() {
        return imageCache;
    }

    public void setBingAPI(BingAPI api) {
        this.bingAPI = api;
    }

    public BingAPI getBingAPI() {
        return bingAPI;
    }
}
