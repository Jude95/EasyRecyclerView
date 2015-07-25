package com.jude.dome;

import android.net.Uri;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class PersonViewHolder extends BaseViewHolder<Person> {
    private TextView mTv_name;
    private SimpleDraweeView mImg_face;
    private TextView mTv_sign;


    public PersonViewHolder(ViewGroup parent) {
        super(parent,R.layout.item_person);
        mTv_name = $(R.id.person_name);
        mTv_sign = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);
    }

    @Override
    public void setData(final Person person){
        mTv_name.setText(person.getName());
        mTv_sign.setText(person.getSign());
        mImg_face.setImageURI(Uri.parse(person.getFace()));
    }
}
