package com.jude.dome.viewholder;

import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.dome.entites.Picture;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by zhuchenxi on 16/6/2.
 */

public class ImageViewHolder extends BaseViewHolder<Picture> {
    ImageView imgPicture;

    public ImageViewHolder(ViewGroup parent) {
        super(new ImageView(parent.getContext()));
        imgPicture = (ImageView) itemView;
        imgPicture.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void setData(Picture data) {
        ViewGroup.LayoutParams params = imgPicture.getLayoutParams();

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels/2;//宽度为屏幕宽度一半
        int height = data.getHeight()*width/data.getWidth();//计算View的高度

        params.height = height;
        imgPicture.setLayoutParams(params);
        Glide.with(getContext())
                .load(data.getSrc()+"?imageView2/0/w/"+ width)
                .into(imgPicture);
    }
}
