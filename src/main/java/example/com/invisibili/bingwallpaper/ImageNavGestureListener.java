package example.com.invisibili.bingwallpaper;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by invisibili on 2015/3/24.
 */
public class ImageNavGestureListener extends GestureDetector.SimpleOnGestureListener{
    private String TAG = "Listener";
    private ImageView view;
    ImageNavGestureListener(ImageView iv){
        view = iv;
    }
    @Override
    public boolean onDoubleTap (MotionEvent e){
        Log.d("GestureListener","Double Tap");
        ImageManager.setWallPaper(view);
        return true;
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // Up motion completing a single tap occurred.
        Log.i(TAG, "Single Tap Up");
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        // Touch has been long enough to indicate a long press.
        // Does not indicate motion is complete yet (no up event necessarily)
        Log.i(TAG, "Long Press");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // User attempted to scroll
        //Log.i(TAG, "Scroll");
        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // Fling event occurred.  Notification of this one happens after an "up" event.
        return true;
    }
    @Override
    public void onShowPress(MotionEvent e) {
        // User performed a down event, and hasn't moved yet.
        Log.i(TAG, "Show Press");
    }
    @Override
    public boolean onDown(MotionEvent e) {
        // "Down" event - User touched the screen.
        Log.i(TAG, "Down");
        return true;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Since double-tap is actually several events which are considered one aggregate
        // gesture, there's a separate callback for an individual event within the doubletap
        // occurring.  This occurs for down, up, and move.
        Log.i(TAG, "Event within double tap");
        return true;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // A confirmed single-tap event has occurred.  Only called when the detector has
        // determined that the first tap stands alone, and is not part of a double tap.
        Log.i(TAG, "Single tap confirmed");
        return true;
    }
    /*
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
    */
}
