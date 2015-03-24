package example.com.invisibili.bingwallpaper;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by invisibili on 2015/3/24.
 */
public class ImageNavGestureListener extends GestureDetector.SimpleOnGestureListener{
    private ImageView view;
    ImageNavGestureListener(ImageView iv){
        view = iv;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
        if(velocityX>0) {
            Log.d("Gesture","Swipe right");
            ImageManager.navPrev();
        }
        if(velocityX<0) {
            Log.d("Gesture","Swipe left");
            ImageManager.showAbout();
        }
        return true;
    }
}
