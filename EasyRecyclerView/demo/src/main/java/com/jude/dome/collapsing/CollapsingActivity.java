package com.jude.dome.collapsing;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.dome.DataProvider;
import com.jude.dome.R;
import com.jude.dome.entites.Ad;
import com.jude.dome.loadmore.PersonAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.List;

/**
 * Created by Mr.Jude on 2016/3/20.
 */
public class CollapsingActivity extends AppCompatActivity {
    EasyRecyclerView recyclerView;
    PersonAdapter adapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new PersonAdapter(this));
        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(DataProvider.getPersonList(0));
                    }
                }, 1000);
            }
        });
        adapter.addAll(DataProvider.getPersonList(0));
        RollPagerView rollPagerView = (RollPagerView) findViewById(R.id.rollPagerView);
        rollPagerView.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.GRAY));
        rollPagerView.setAdapter(new BannerAdapter());
    }

    private class BannerAdapter extends StaticPagerAdapter {
        private List<Ad> list;
        public BannerAdapter(){
            list = DataProvider.getAdList();
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(CollapsingActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //加载图片
            Glide.with(CollapsingActivity.this)
                    .load(list.get(position).getImage())
                    .placeholder(R.drawable.default_image)
                    .into(imageView);
            //点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getUrl())));
                }
            });
            return imageView;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
