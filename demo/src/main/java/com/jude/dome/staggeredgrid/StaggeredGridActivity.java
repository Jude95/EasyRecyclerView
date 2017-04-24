package com.jude.dome.staggeredgrid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.jude.dome.DataProvider;
import com.jude.dome.R;
import com.jude.dome.Utils;
import com.jude.dome.header.BannerAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

/**
 * Created by Mr.Jude on 2016/6/7.
 */
public class StaggeredGridActivity extends AppCompatActivity {
    private EasyRecyclerView recyclerView;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter = new ImageAdapter(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(4));
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                RollPagerView header = new RollPagerView(StaggeredGridActivity.this);
                header.setHintView(new ColorPointHintView(StaggeredGridActivity.this, Color.YELLOW,Color.GRAY));
                header.setHintPadding(0, 0, 0, (int) Utils.convertDpToPixel(8, StaggeredGridActivity.this));
                header.setPlayDelay(2000);
                header.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.convertDpToPixel(200, StaggeredGridActivity.this)));
                header.setAdapter(new BannerAdapter(StaggeredGridActivity.this));
                return header;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        SpaceDecoration itemDecoration = new SpaceDecoration((int) Utils.convertDpToPixel(8,this));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(true);
        recyclerView.addItemDecoration(itemDecoration);
        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                addData();
            }

            @Override
            public void onMoreClick() {

            }
        });
        adapter.setNoMore(R.layout.view_nomore);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        adapter.addAll(DataProvider.getPictures(0));
                    }
                },1000);
            }
        });
        addData();
    }

    private void addData(){
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addAll(DataProvider.getPictures(0));
            }
        },1000);
    }
}
