package example.com.invisibili.bingwallpaper;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by invisibili on 2015/5/20.
 */
public class TipCalc extends Activity {
    public static String intToPrice(int num) {
        return num/100 + "." + num%100;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_layout);
        TextView input = (TextView)findViewById(R.id.in_price);
        final TextView out10 = (TextView)findViewById(R.id.out_10);
        final TextView out15 = (TextView)findViewById(R.id.out_15);
        final TextView out20 = (TextView)findViewById(R.id.out_20);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String num = s.toString();
                try {
                    double a = Double.valueOf(num);
                    int a10 = (int) (a * 110);
                    int a15 = (int) (a * 115);
                    int a20 = (int) (a * 120);
                    out10.setText(intToPrice(a10));
                    out15.setText(intToPrice(a15));
                    out20.setText(intToPrice(a20));
                } catch (NumberFormatException e){
                    // empty string or invalid number
                    out10.setText("Please");
                    out15.setText("Input");
                    out20.setText("Number");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
