package com.jude.dome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jude.dome.collapsing.CollapsingActivity;
import com.jude.dome.header.HeaderFooterActivity;
import com.jude.dome.loadmore.RefreshAndMoreActivity;
import com.jude.dome.multistyle.MultiStyleActivity;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class MainActivity extends AppCompatActivity {
    Button refreshAndMore;
    Button multiStyle;
    Button headerAndFooter;
    Button collapsing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshAndMore = (Button) findViewById(R.id.refresh_and_more);
        refreshAndMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RefreshAndMoreActivity.class));
            }
        });
        multiStyle = (Button) findViewById(R.id.multi_style);
        multiStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MultiStyleActivity.class));
            }
        });
        headerAndFooter = (Button) findViewById(R.id.header_footer);
        headerAndFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HeaderFooterActivity.class));
            }
        });
        collapsing = (Button) findViewById(R.id.collapsing);
        collapsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CollapsingActivity.class));
            }
        });
    }
}
