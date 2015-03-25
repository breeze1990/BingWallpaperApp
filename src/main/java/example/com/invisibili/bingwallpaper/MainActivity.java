package example.com.invisibili.bingwallpaper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
    public static final String PREVURL_PREFERENCE_FILE = "example.com.invisibili.bingwallpaper.PREVURL_PREFERENCE_FILE";
    public static final String Saved_Image_File = "test.jpg";
    public static final int MAX_CNT = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageCache.init(MAX_CNT-1,getResources());
        BingAPI bApi = new BingAPI(MAX_CNT-1);
        bApi.execute();
        ImageManager.setContext(this);
        ImageManager.setResources(getResources());
        ViewPager pager = (ViewPager)findViewById(R.id.img_display);
        ImageAdapter adapter = new ImageAdapter(getSupportFragmentManager(), pager, this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static class ImageAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
        private ViewPager pager;
        private Context context;
        public ImageAdapter(FragmentManager fm,ViewPager pager,Context context) {
            super(fm);
            this.pager = pager;
            this.context = context;
        }
        @Override
        public int getCount() {
            return MAX_CNT;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("FRAG","GetItem called at "+position);
            return ImageFragment.getInstance(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(!isNetworkAvailable(context) && position>0) {
                pager.setCurrentItem(0);
                Toast.makeText(context,"No Internet.",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(context,String.valueOf(position),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public static class ImageFragment extends Fragment{
        int pos;
        View view;

        public static ImageFragment getInstance(int position){
            ImageFragment frag = new ImageFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pos = getArguments() != null ? getArguments().getInt("position") : 0;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = null;
            switch (pos){
                case 0:
                    v = inflater.inflate(R.layout.intro_fragment, container, false);
                    break;
                default:
                    v = inflater.inflate(R.layout.image_fragment,container,false);
                    final GestureDetector gd = new GestureDetector(getActivity(),new ImageNavGestureListener((ImageView)v));
                    v.setClickable(true);
                    v.setFocusable(true);
                    v.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            gd.onTouchEvent(event);
                            //Log.d("frag","onTouch event caught on ImageView");
                            return false;
                        }
                    });
                    break;
            }
            view = v;
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
        @Override
        public void onStart(){
            super.onStart();
            if(pos>0) ImageManager.loadImage(pos-1,(ImageView)view);
        }
    }
}
