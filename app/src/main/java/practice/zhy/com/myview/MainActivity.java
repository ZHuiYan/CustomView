package practice.zhy.com.myview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private VCylinderView dazhang;
    private HCylinderView hCylinderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        dazhang = (VCylinderView) findViewById(R.id.dazhang);
        hCylinderView = (HCylinderView) findViewById(R.id.hc);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dazhang.setChange(getResources().getColor(R.color.public_red), true, 0.98f);
                hCylinderView.setChange(true, 0.01f, 0.95f, 0.04f);
            }
        });
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dazhang.setChange(getResources().getColor(R.color.public_red), true, 0.04f);
                hCylinderView.setChange(true, 0.01f, 0.99f, 0.0f);
            }
        });
    }


}
