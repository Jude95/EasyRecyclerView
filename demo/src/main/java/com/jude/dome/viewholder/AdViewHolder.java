package com.jude.dome.viewholder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.dome.R;
import com.jude.dome.Utils;
import com.jude.dome.entites.Ad;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class AdViewHolder extends BaseViewHolder<Ad> {
    public AdViewHolder(ViewGroup parent) {
        super(new ImageView(parent.getContext()));
        ImageView imageView = (ImageView) itemView;
        imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)Utils.convertDpToPixel(156,getContext())));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void setData(final Ad data) {
        ImageView imageView = (ImageView) itemView;
        Glide.with(getContext())
                .load(data.getImage())
                .placeholder(R.drawable.default_image)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl())));
            }
        });
    }

}
