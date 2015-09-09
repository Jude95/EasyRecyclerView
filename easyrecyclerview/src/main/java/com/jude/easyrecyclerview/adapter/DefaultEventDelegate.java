package com.jude.easyrecyclerview.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Mr.Jude on 2015/8/18.
 */
public class DefaultEventDelegate implements EventDelegate {

    private EventFooter footer = new EventFooter();

    private RecyclerArrayAdapter.OnLoadMoreListener onLoadMoreListener;

    private boolean hasData = false;
    private boolean isLoadingMore = false;

    private int status = STATUS_INITIAL;
    private static final int STATUS_INITIAL = 291;
    private static final int STATUS_MORE = 260;
    private static final int STATUS_NOMORE = 408;
    private static final int STATUS_ERROR = 732;

    public DefaultEventDelegate(RecyclerArrayAdapter adapter) {
        adapter.addFooter(footer);
    }

    public void onMoreViewShowed() {
        Log.i("recycler", "onMoreViewShowed");
        if (!isLoadingMore&&onLoadMoreListener!=null)onLoadMoreListener.onLoadMore();
    }

    public void onErrorViewShowed() {
        resumeLoadMore();
    }

    //-------------------5个状态触发事件-------------------
    @Override
    public void addData(int length) {
        Log.i("recycler", "addData" + length);
        if (length == 0){
            //当添加0个时，认为已结束加载到底
            if (status==STATUS_INITIAL || status == STATUS_MORE){
                footer.showNoMore();
            }
        }else {
            //当Error时。再次添加则还原。
            if (status == STATUS_INITIAL || status == STATUS_ERROR){
                footer.showMore();
            }
            hasData = true;
        }
    }

    @Override
    public void clear() {
        Log.i("recycler","clear");
        hasData = false;
        status = STATUS_INITIAL;
        footer.hide();
    }

    @Override
    public void stopLoadMore() {
        Log.i("recycler", "stopLoadMore");
        footer.showNoMore();
        status = STATUS_NOMORE;
    }

    @Override
    public void pauseLoadMore() {
        Log.i("recycler", "pauseLoadMore");
        footer.showError();
        status = STATUS_ERROR;
    }

    @Override
    public void resumeLoadMore() {
        footer.showMore();
        onMoreViewShowed();
    }

    //-------------------3种View设置-------------------

    @Override
    public void setMore(View view, RecyclerArrayAdapter.OnLoadMoreListener listener) {
        this.footer.setMoreView(view);
        this.onLoadMoreListener = listener;
        Log.i("recycler","setMore");
    }

    @Override
    public void setNoMore(View view) {
        this.footer.setNoMoreView(view);
        Log.i("recycler", "setNoMore");
    }

    @Override
    public void setErrorMore(View view) {
        this.footer.setErrorView(view);
        Log.i("recycler","setErrorMore");
    }


    private class EventFooter implements RecyclerArrayAdapter.ItemView {
        private FrameLayout container;
        private View moreView;
        private View noMoreView;
        private View errorView;

        private int flag = 0;


        @Override
        public View onCreateView(ViewGroup parent) {
            Log.i("recycler","onCreateView");
            container = new FrameLayout(parent.getContext());
            container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return container;
        }

        @Override
        public void onBindView(View headerView) {
            Log.i("recycler","onBindView");
            switch (flag){
                case 1:
                    onMoreViewShowed();
                    break;
                case 2:
                    onErrorViewShowed();
                    break;

            }
        }

        private void showView(View view){
            if (view!=null){
                if (container.getVisibility() != View.VISIBLE)container.setVisibility(View.VISIBLE);
                if (view.getParent()==null)container.addView(view);

                for (int i = 0; i < container.getChildCount(); i++) {
                    if (container.getChildAt(i) == view)view.setVisibility(View.VISIBLE);
                    else container.getChildAt(i).setVisibility(View.GONE);
                }
            }else {
                container.setVisibility(View.GONE);
            }
        }

        public void showError(){
            showView(errorView);
            flag = 2;
        }
        public void showMore(){
            showView(moreView);
            flag = 1;
        }
        public void showNoMore(){
            showView(noMoreView);
            flag = 3;
        }

        public void hide(){
            container.setVisibility(View.GONE);
        }

        public void setMoreView(View moreView) {
            this.moreView = moreView;
        }

        public void setNoMoreView(View noMoreView) {
            this.noMoreView = noMoreView;
        }

        public void setErrorView(View errorView) {
            this.errorView = errorView;
        }
    }

}
