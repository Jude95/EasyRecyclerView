package com.jude.dome;

import android.net.Uri;
import android.view.LayoutInflater;
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
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false));
        mTv_name = (TextView) itemView.findViewById(R.id.person_name);
        mTv_sign = (TextView) itemView.findViewById(R.id.person_sign);
        mImg_face = (SimpleDraweeView) itemView.findViewById(R.id.person_face);
        itemView.setTag(this);
    }

    public void setData(final Person person){
        mTv_name.setText(person.getName());
        mTv_sign.setText(person.getSign());
        mImg_face.setImageURI(Uri.parse(person.getFace()));
    }
}
