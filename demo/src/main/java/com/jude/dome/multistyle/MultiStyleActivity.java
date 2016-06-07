package com.jude.dome.multistyle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.jude.dome.DataProvider;
import com.jude.dome.R;
import com.jude.easyrecyclerview.EasyRecyclerView;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class MultiStyleActivity extends AppCompatActivity {
    private EasyRecyclerView recyclerView;
    private PersonWithAdAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setProgressView(R.layout.view_progress);
        adapter = new PersonWithAdAdapter(this);
        adapter.addAll(DataProvider.getPersonWithAds(0));
        recyclerView.setAdapterWithProgress(adapter);
    }
}
