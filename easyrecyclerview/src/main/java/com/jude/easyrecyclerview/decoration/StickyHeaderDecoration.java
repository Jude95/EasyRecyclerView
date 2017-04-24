/*
 * Copyright 2014 Eduardo Barrenechea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jude.easyrecyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * A sticky header decoration for android's RecyclerView.
 */
public class StickyHeaderDecoration extends RecyclerView.ItemDecoration {
    /**
     * The adapter to assist the {@link StickyHeaderDecoration} in creating and binding the header views.
     *
     * @param <T> the header view holder
     */
    public interface IStickyHeaderAdapter<T extends RecyclerView.ViewHolder> {

        /**
         * Returns the header id for the item at the given position.
         * The item in one group should return the same HeaderId.
         *
         * @param position the item position
         * @return the header id
         */
        long getHeaderId(int position);

        /**
         * Creates a new header ViewHolder.
         *
         * @param parent the header's view parent
         * @return a view holder for the created view
         */
        T onCreateHeaderViewHolder(ViewGroup parent);

        /**
         * Updates the header view to reflect the header data for the given position
         * @param viewholder the header view holder
         * @param position the header's item position
         */
        void onBindHeaderViewHolder(T viewholder, int position);
    }


    public static final long NO_HEADER_ID = -1L;

    private Map<Long, RecyclerView.ViewHolder> mHeaderCache;

    private IStickyHeaderAdapter mAdapter;

    private boolean mRenderInline;

    private boolean mIncludeHeader = false;

    /**
     * @param adapter
     *         the sticky header adapter to use
     */
    public StickyHeaderDecoration(IStickyHeaderAdapter adapter) {
        this(adapter, false);
    }

    /**
     * @param adapter
     *         the sticky header adapter to use
     */
    public StickyHeaderDecoration(IStickyHeaderAdapter adapter, boolean renderInline) {
        mAdapter = adapter;
        mHeaderCache = new HashMap<>();
        mRenderInline = renderInline;
    }

    public void setIncludeHeader(boolean mIncludeHeader) {
        this.mIncludeHeader = mIncludeHeader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int headerHeight = 0;

        if (!mIncludeHeader){
            if (parent.getAdapter() instanceof RecyclerArrayAdapter){
                int headerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getHeaderCount();
                int footerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getFooterCount();
                int dataCount = ((RecyclerArrayAdapter) parent.getAdapter()).getCount();
                if (position<headerCount){
                    return;
                }
                if (position>=headerCount+dataCount){
                    return ;
                }
                if (position>=headerCount){
                    position-=headerCount;
                }

            }
        }

        if (position != RecyclerView.NO_POSITION
                && hasHeader(position)
                && showHeaderAboveItem(position)) {

            View header = getHeader(parent, position).itemView;
            headerHeight = getHeaderHeightForLayout(header);
        }

        outRect.set(0, headerHeight, 0, 0);
    }

    private boolean showHeaderAboveItem(int itemAdapterPosition) {
        if (itemAdapterPosition == 0) {
            return true;
        }
        return mAdapter.getHeaderId(itemAdapterPosition - 1) != mAdapter.getHeaderId(itemAdapterPosition);
    }

    /**
     * Clears the header view cache. Headers will be recreated and
     * rebound on list scroll after this method has been called.
     */
    public void clearHeaderCache() {
        mHeaderCache.clear();
    }

    public View findHeaderViewUnder(float x, float y) {
        for (RecyclerView.ViewHolder holder : mHeaderCache.values()) {
            final View child = holder.itemView;
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);

            if (x >= child.getLeft() + translationX &&
                    x <= child.getRight() + translationX &&
                    y >= child.getTop() + translationY &&
                    y <= child.getBottom() + translationY) {
                return child;
            }
        }

        return null;
    }

    private boolean hasHeader(int position) {
        return mAdapter.getHeaderId(position) != NO_HEADER_ID;
    }

    private RecyclerView.ViewHolder getHeader(RecyclerView parent, int position) {
        final long key = mAdapter.getHeaderId(position);

        if (mHeaderCache.containsKey(key)) {
            return mHeaderCache.get(key);
        } else {
            final RecyclerView.ViewHolder holder = mAdapter.onCreateHeaderViewHolder(parent);
            final View header = holder.itemView;

            //noinspection unchecked
            mAdapter.onBindHeaderViewHolder(holder, position);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.UNSPECIFIED);

            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(), header.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(), header.getLayoutParams().height);

            header.measure(childWidth, childHeight);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());

            mHeaderCache.put(key, holder);

            return holder;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() == null){
            return;
        }

        final int count = parent.getChildCount();
        long previousHeaderId = -1;

        for (int layoutPos = 0; layoutPos < count; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            int adapterPos = parent.getChildAdapterPosition(child);

            if (!mIncludeHeader){
                if (parent.getAdapter() instanceof RecyclerArrayAdapter){
                    int headerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getHeaderCount();
                    int footerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getFooterCount();
                    int dataCount = ((RecyclerArrayAdapter) parent.getAdapter()).getCount();
                    if (adapterPos<headerCount){
                        continue;
                    }
                    if (adapterPos>=headerCount+dataCount){
                        continue ;
                    }
                    if (adapterPos>=headerCount){
                        adapterPos-=headerCount;
                    }

                }
            }

            if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                long headerId = mAdapter.getHeaderId(adapterPos);

                if (headerId != previousHeaderId) {
                    previousHeaderId = headerId;
                    View header = getHeader(parent, adapterPos).itemView;
                    canvas.save();

                    final int left = child.getLeft();
                    final int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
                    canvas.translate(left, top);

                    header.setTranslationX(left);
                    header.setTranslationY(top);
                    header.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    private int getHeaderTop(RecyclerView parent, View child, View header, int adapterPos, int layoutPos) {
        int headerHeight = getHeaderHeightForLayout(header);
        int top = ((int) child.getY()) - headerHeight;
        if (layoutPos == 0) {
            final int count = parent.getChildCount();
            final long currentId = mAdapter.getHeaderId(adapterPos);
            // find next view with header and compute the offscreen push if needed
            for (int i = 1; i < count; i++) {
                int adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i));
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    long nextId = mAdapter.getHeaderId(adapterPosHere);
                    if (nextId != currentId) {
                        final View next = parent.getChildAt(i);
                        final int offset = ((int) next.getY()) - (headerHeight + getHeader(parent, adapterPosHere).itemView.getHeight());
                        if (offset < 0) {
                            return offset;
                        } else {
                            break;
                        }
                    }
                }
            }

            top = Math.max(0, top);
        }

        return top;
    }

    private int getHeaderHeightForLayout(View header) {
        return mRenderInline ? 0 : header.getHeight();
    }
}
