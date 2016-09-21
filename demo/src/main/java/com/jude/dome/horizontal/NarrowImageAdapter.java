package com.jude.dome.horizontal;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.dome.Utils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by Mr.Jude on 2016/6/7.
 */
public class NarrowImageAdapter extends RecyclerArrayAdapter<Integer> {
    public NarrowImageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new NarrowImageViewHolder(parent);
    }

    private static class NarrowImageViewHolder extends BaseViewHolder<Integer>{
        ImageView imgPicture;

        public NarrowImageViewHolder(ViewGroup parent) {
            super(new ImageView(parent.getContext()));
            imgPicture = (ImageView) itemView;
            imgPicture.setLayoutParams(new ViewGroup.LayoutParams((int) Utils.convertDpToPixel(72f,getContext()), ViewGroup.LayoutParams.MATCH_PARENT));
            imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        @Override
        public void setData(Integer data) {
            imgPicture.setImageResource(data);
        }
    }
}
