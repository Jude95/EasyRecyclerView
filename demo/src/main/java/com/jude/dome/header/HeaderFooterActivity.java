package com.jude.dome.header;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.dome.DataProvider;
import com.jude.dome.R;
import com.jude.dome.Utils;
import com.jude.dome.horizontal.NarrowImageAdapter;
import com.jude.dome.loadmore.PersonAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.Util;
import com.jude.rollviewpager.hintview.ColorPointHintView;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class HeaderFooterActivity extends AppCompatActivity {
    private EasyRecyclerView recyclerView;
    private PersonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter = new PersonAdapter(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,72),0);
        itemDecoration.setDrawLastItem(true);
        itemDecoration.setDrawHeaderFooter(true);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        adapter.addAll(DataProvider.getPersonList(0));
                    }
                },1500);
            }
        });
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                RollPagerView header = new RollPagerView(HeaderFooterActivity.this);
                header.setHintView(new ColorPointHintView(HeaderFooterActivity.this, Color.YELLOW,Color.GRAY));
                header.setHintPadding(0, 0, 0, (int) Utils.convertDpToPixel(8, HeaderFooterActivity.this));
                header.setPlayDelay(2000);
                header.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.convertDpToPixel(200, HeaderFooterActivity.this)));
                header.setAdapter(new BannerAdapter(HeaderFooterActivity.this));
                return header;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                RecyclerView recyclerView = new RecyclerView(parent.getContext()){
                    //为了不打扰横向RecyclerView的滑动操作，可以这样处理
                    @Override
                    public boolean onTouchEvent(MotionEvent event) {
                        super.onTouchEvent(event);
                        return true;
                    }
                };
                recyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) Utils.convertDpToPixel(300, HeaderFooterActivity.this)));
                final NarrowImageAdapter adapter;
                recyclerView.setAdapter(adapter = new NarrowImageAdapter(parent.getContext()));
                recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext(),LinearLayoutManager.HORIZONTAL,false));
                recyclerView.addItemDecoration(new SpaceDecoration((int) Utils.convertDpToPixel(8,parent.getContext())));
                adapter.setMore(R.layout.view_more_horizontal, new RecyclerArrayAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addAll(DataProvider.getNarrowImage(0));
                            }
                        },1000);
                    }
                });
                adapter.addAll(DataProvider.getNarrowImage(0));
                return recyclerView;
            }

            @Override
            public void onBindView(View headerView) {
                //这里的处理别忘了
                ((ViewGroup)headerView).requestDisallowInterceptTouchEvent(true);
            }
        });
        adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                TextView tv = new TextView(HeaderFooterActivity.this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.convertDpToPixel(56,HeaderFooterActivity.this)));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                tv.setText("(-_-)/~~~死宅真是恶心");
                return tv;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        adapter.addAll(DataProvider.getPersonList(0));
    }


}
