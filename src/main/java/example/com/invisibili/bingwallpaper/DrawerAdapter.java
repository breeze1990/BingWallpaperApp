package example.com.invisibili.bingwallpaper;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by invisibili on 2015/5/25.
 */
public class DrawerAdapter extends BaseAdapter {
    private int[] icons;
    private String[] labels;
    Context context;

    DrawerAdapter(Context c, int[] icons, String[] labels) {
        this.icons = icons;
        this.labels = labels;
        context = c;
        assert icons.length == labels.length;
    }
    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @TargetApi(21)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView !=null) return convertView;
        Log.d("drawer","convertView null");
        LayoutInflater li = LayoutInflater.from(context);
        View row  = li.inflate(R.layout.drawer_list_item_main, parent, false);
        ImageView ic = (ImageView)row.findViewById(R.id.list_icon);
        ic.setImageDrawable(context.getResources().getDrawable(icons[position],null));
        TextView tv = (TextView)row.findViewById(R.id.list_label);
        tv.setText(labels[position]);
        Log.d("drawer","getView ended");
        return row;
    }
}
