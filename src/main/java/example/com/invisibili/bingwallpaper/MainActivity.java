package example.com.invisibili.bingwallpaper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
    public static final String PREVURL_PREFERENCE_FILE = "example.com.invisibili.bingwallpaper.PREVURL_PREFERENCE_FILE";
    public static final String Saved_Image_File = "test.jpg";
    public static final int MAX_CNT = 6;
    private static final long TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private static String dataFragmentTag = "RetainedData";
    private long mBackPressed;
    Handler mHandler;
    private RetainedFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag(dataFragmentTag);

        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment,dataFragmentTag).commit();
        }
//        ImageView nav_drawer_logo = (ImageView)findViewById(R.id.nav_drawer_logo);
//        nav_drawer_logo.setLayoutParams(new LinearLayout.LayoutParams(nav_drawer_logo.getMeasuredWidth(), nav_drawer_logo.getMeasuredWidth()));
//        Log.d("logo",""+findViewById(R.id.nav_drawer).getMeasuredWidth());
        ViewPager pager = (ViewPager)findViewById(R.id.img_display);
        ImageAdapter adapter = new ImageAdapter(getSupportFragmentManager(), pager, this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);
        Button but = (Button)findViewById(R.id.tip_button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TipCalc.class));
            }
        });
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                Log.d("Msg what",""+inputMessage.what);
            }
        };
        int[] drawerListIcons = {R.drawable.list_calc, R.drawable.list_calc};
        String[] drawerListLabels = {"TipCalculator","dummy"};
        DrawerAdapter dAdapter = new DrawerAdapter(this,drawerListIcons,drawerListLabels);
        ListView listView = (ListView)findViewById(R.id.drawer_lists);
        listView.setAdapter(dAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position ==0 ) {
                    startActivity(new Intent(MainActivity.this,TipCalc.class));
                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        ImageManager.setContext(this);
        ImageManager.setResources(getResources());
        if(dataFragment.getBingAPI()!=null && dataFragment.getImageCache()!=null) return;
        ImageCache.init(MAX_CNT-1,getResources());
        BingAPI bApi = new BingAPI(MAX_CNT-1);
        dataFragment.setBingAPI(bApi);
        dataFragment.setImageCache(ImageCache.getInstance());
        bApi.execute();
    }
    private static Toast tst;
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            if(tst != null) tst.cancel();
            super.onBackPressed();
            return;
        }
        else {
            tst = Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT);
            tst.show();
        }
        mBackPressed = System.currentTimeMillis();
        Message.obtain(mHandler,123456).sendToTarget();
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
        public ImageAdapter(android.support.v4.app.FragmentManager fm,ViewPager pager,Context context) {
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    public static class ImageFragment extends Fragment{
        int pos;
        View view;
        TextView descView;
        View imgLayout;

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
        @TargetApi(17)
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = null;
            switch (pos){
                case 0:
                    v = inflater.inflate(R.layout.intro_fragment, container, false);
                    break;
                default:
                    v = inflater.inflate(R.layout.image_fragment,container,false);
                    ImageView iv = (ImageView)v.findViewById(R.id.img_viewer);
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    int height = size.y;

                    int statusBarHeight=0;
                    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                    }
                    iv.getLayoutParams().height = height-statusBarHeight;
                    iv.getLayoutParams().width = width;
                    final GestureDetector gd = new GestureDetector(getActivity(),new ImageNavGestureListener(iv));

                    iv.setClickable(true);
                    iv.setFocusable(true);
                    iv.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            gd.onTouchEvent(event);
                            //Log.d("frag","onTouch event caught on ScrollView");
                            return false;
                        }
                    });
                    view = iv;
                    TextView tv = (TextView)v.findViewById(R.id.desc);
                    descView = tv;
                    imgLayout = v.findViewById(R.id.loadingPanel);
                    imgLayout.getLayoutParams().height = height-statusBarHeight;
                    break;
            }
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
        @Override
        public void onStart(){
            super.onStart();
            if(pos>0) ImageManager.loadImage(pos-1,(ImageView)view, descView, imgLayout, getResources().getConfiguration());
        }
    }
}
