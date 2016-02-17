/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jude.easyrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A concrete BaseAdapter that is backed by an array of arbitrary
 * objects.  By default this class expects that the provided resource id references
 * a single TextView.  If you want to use a more complex layout, use the constructors that
 * also takes a field id.  That field id should reference a TextView in the larger layout
 * resource.
 *
 * <p>However the TextView is referenced, it will be filled with the toString() of each object in
 * the array. You can add lists or arrays of custom objects. Override the toString() method
 * of your objects to determine what text will be displayed for the item in the list.
 *
 * <p>To use something other than TextViews for the array display, for instance, ImageViews,
 * or to have some of data besides toString() results fill the views,
 */
abstract public class RecyclerArrayAdapter<T> extends RecyclerView.Adapter<BaseViewHolder>   {
    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;
    private EventDelegate mEventDelegate;
    ArrayList<ItemView> headers = new ArrayList<>();
    ArrayList<ItemView> footers = new ArrayList<>();

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public interface ItemView {
         View onCreateView(ViewGroup parent);
         void onBindView(View headerView);
    }
    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{
        private int mMaxCount;
        public GridSpanSizeLookup(int maxCount){
            this.mMaxCount = maxCount;
        }
        @Override
        public int getSpanSize(int position) {
            if (headers.size()!=0){
                if (position<headers.size())return mMaxCount;
            }
            if (footers.size()!=0) {
                int i = position - headers.size() - mObjects.size();
                if (i >= 0) {
                    return mMaxCount;
                }
            }
            return 1;
        }
    }

    public GridSpanSizeLookup obtainGridSpanSizeLookUp(int maxCount){
        return new GridSpanSizeLookup(maxCount);
    }

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    private final Object mLock = new Object();


    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;


    /**
     * Constructor
     *
     * @param context The current context.
     */
    public RecyclerArrayAdapter(Context context) {
        init(context,  new ArrayList<T>());
    }


    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public RecyclerArrayAdapter(Context context, T[] objects) {
        init(context, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public RecyclerArrayAdapter(Context context, List<T> objects) {
        init(context, objects);
    }


    private void init(Context context , List<T> objects) {
        mContext = context;
        mObjects = objects;
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        if (mEventDelegate!=null)mEventDelegate.addData(object == null ? 0 : 1);
        if (object!=null){
            synchronized (mLock) {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }
    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        if (mEventDelegate!=null)mEventDelegate.addData(collection == null ? 0 : collection.size());
        if (collection!=null&&collection.size()!=0){
            synchronized (mLock) {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        if (mEventDelegate!=null)mEventDelegate.addData(items==null?0:items.length);
        if (items!=null&&items.length!=0) {
            synchronized (mLock) {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void stopMore(){
        if (mEventDelegate == null)throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDelegate.stopLoadMore();
    }

    public void pauseMore(){
        if (mEventDelegate == null)throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDelegate.pauseLoadMore();
    }

    public void resumeMore(){
        if (mEventDelegate == null)throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDelegate.resumeLoadMore();
    }


    public void addHeader(ItemView view){
        if (view==null)throw new NullPointerException("ItemView can't be null");
        headers.add(view);
    }

    public void addFooter(ItemView view){
        if (view==null)throw new NullPointerException("ItemView can't be null");
        footers.add(view);
    }

    public void removeAllHeader(){
        headers.clear();
    }

    public void removeAllFooter(){
        footers.clear();
    }

    public void getHeader(int index){
        headers.get(index);
    }

    public void getFooter(int index){
        footers.get(index);
    }

    public int getHeaderCount(){return headers.size();}

    public int getFooterCount(){return footers.size();}

    public void removeHeader(ItemView view){
        headers.remove(view);
    }

    public void removeFooter(ItemView view){
        footers.remove(view);
    }


    EventDelegate getEventDelegate(){
        if (mEventDelegate == null)mEventDelegate  = new DefaultEventDelegate(this);
        return mEventDelegate;
    }

    public View setMore(final int res, final OnLoadMoreListener listener){
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getEventDelegate().setMore(container, listener);
        return container;
    }

    public View setMore(final View view,OnLoadMoreListener listener){
        getEventDelegate().setMore(view, listener);
        return view;
    }

    public View setNoMore(final int res) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getEventDelegate().setNoMore(container);
        return container;
    }

    public View setNoMore(final View view) {
        getEventDelegate().setNoMore(view);
        return view;
    }

    public View setError(final int res) {
        FrameLayout container = new FrameLayout(getContext());
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(res, container);
        getEventDelegate().setErrorMore(container);
        return container;
    }

    public View setError(final View view) {
        getEventDelegate().setErrorMore(view);
        return view;
    }

    /**
     * 插入，不会触发任何事情
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        synchronized (mLock) {
            mObjects.add(index, object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * 删除，不会触发任何事情
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        synchronized (mLock) {
            mObjects.remove(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * 删除，不会触发任何事情
     *
     * @param position The position of the object to remove.
     */
    public void remove(int position) {
        synchronized (mLock) {
            mObjects.remove(position);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    /**
     * 触发清空
     */
    public void clear() {
        if (mEventDelegate!=null)mEventDelegate.clear();
        synchronized (mLock) {
            mObjects.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *        in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            Collections.sort(mObjects, comparator);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }




    /**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return mContext;
    }


    /**
     * 这个函数包含了头部和尾部view的个数，不是真正的item个数。
     * @return
     */
    @Deprecated
    @Override
    public final int getItemCount() {
        return mObjects.size()+headers.size()+footers.size();
    }

    /**
     * 应该使用这个获取item个数
     * @return
     */
    public int getCount(){
        return mObjects.size();
    }

    private View createSpViewByType(ViewGroup parent, int viewType){
        for (ItemView headerView:headers){
            if (headerView.hashCode() == viewType){
                View view = headerView.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }
        }
        for (ItemView footerview:footers){
            if (footerview.hashCode() == viewType){
                View view = footerview.onCreateView(parent);
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                layoutParams.setFullSpan(true);
                view.setLayoutParams(layoutParams);
                return view;
            }
        }
        return null;
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createSpViewByType(parent, viewType);
        if (view!=null){
            return new StateViewHolder(view);
        }

        final BaseViewHolder viewHolder = OnCreateViewHolder(parent, viewType);

        //itemView 的点击事件
        if (mItemClickListener!=null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(viewHolder.getAdapterPosition()-headers.size());
                }
            });
        }

        if (mItemLongClickListener!=null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemClick(viewHolder.getAdapterPosition()-headers.size());
                }
            });
        }
        return viewHolder;
    }

    abstract public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType);


    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setId(position);
        if (headers.size()!=0 && position<headers.size()){
            headers.get(position).onBindView(holder.itemView);
            return ;
        }

        int i = position - headers.size() - mObjects.size();
        if (footers.size()!=0 && i>=0){
            footers.get(i).onBindView(holder.itemView);
            return ;
        }
        OnBindViewHolder(holder,position-headers.size());
    }


    public void OnBindViewHolder(BaseViewHolder holder, final int position){
        holder.setData(getItem(position));
    }


    @Deprecated
    @Override
    public final int getItemViewType(int position) {
        if (headers.size()!=0){
            if (position<headers.size())return headers.get(position).hashCode();
        }
        if (footers.size()!=0){
            /*
            eg:
            0:header1
            1:header2   2
            2:object1
            3:object2
            4:object3
            5:object4
            6:footer1   6(position) - 2 - 4 = 0
            7:footer2
             */
            int i = position - headers.size() - mObjects.size();
            if (i >= 0){
                return footers.get(i).hashCode();
            }
        }
        return getViewType(position-headers.size());
    }

    public int getViewType(int position){
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }

    private class StateViewHolder extends BaseViewHolder{

        public StateViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }
}
