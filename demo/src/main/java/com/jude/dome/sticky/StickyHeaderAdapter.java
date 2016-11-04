

package com.jude.dome.sticky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.dome.R;
import com.jude.dome.entites.Person;
import com.jude.dome.viewholder.PersonViewHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.adapter.StickyHeaderAdapterImp;

/**
 * 当前类注释：悬浮headerAdapter
 * PackageName：com.jude.dome.sticky
 * Created by Qyang on 16/11/4
 * Email: yczx27@163.com
 */
public class StickyHeaderAdapter extends RecyclerArrayAdapter<Person> implements StickyHeaderAdapterImp<StickyHeaderAdapter.HeaderHolder> {

    private LayoutInflater mInflater;

    public StickyHeaderAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonViewHolder(parent);
    }


    @Override
    public long getHeaderId(int position) {
        return position / 3;
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.header_item, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        viewholder.header.setText("Header " + getHeaderId(position));
    }

    class HeaderHolder extends BaseViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView;
        }
    }
}
