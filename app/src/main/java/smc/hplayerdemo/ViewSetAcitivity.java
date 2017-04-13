package smc.hplayerdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ViewSetAcitivity extends AppCompatActivity {
    private LinearLayout.LayoutParams params;
    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_set_acitivity);
        int width=getScreenWidthPixels(ViewSetAcitivity.this);
        ImageView icon_view= (ImageView) findViewById(R.id.icon_iv);
        params=(LinearLayout.LayoutParams)icon_view.getLayoutParams();
        params.width=width/5;
        params.height=params.width;
        params.setMargins(0, 20,0, 0);
        TextView tx = (TextView) findViewById(R.id.tx);
        params=(LinearLayout.LayoutParams)tx.getLayoutParams();
        params.width=width/5;
        params.height=params.width;
        params.setMargins(20, 20,10, 0);
        tx.setText("jjisdjfi");
        tx.setTextSize(18);
    }
}
