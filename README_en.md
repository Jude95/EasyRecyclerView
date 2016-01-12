# EasyRecyclerView
[中文](https://github.com/Jude95/EasyRecyclerView/blob/master/README.md) ｜ [English](https://github.com/Jude95/EasyRecyclerView/blob/master/README_en.md)

Encapsulate many API about RecyclerView into the library,such as arrayAdapter,pull to refresh,auto load more,no more and error in the end,header&footer.  
The library uses a new usage of ViewHolder,decoupling the ViewHolder and Adapter.  
Adapter will do less work,adapter only direct the ViewHolder,if you use MVP,you can put adapter into presenter.ViewHolder only show the item,then you can use one ViewHolder for many Adapter.   
Part of the code modified from [Malinskiy/SuperRecyclerView](https://github.com/Malinskiy/SuperRecyclerView),make more functions handed by Adapter.    


#Denpendency
`compile 'com.jude:easyrecyclerview:3.3.1'`


#ScreenShot
![recycler.gif](recycler3.gif)
#Usage
##EasyRecyclerView

        <com.jude.easyrecyclerview.EasyRecyclerView
          android:id="@+id/recyclerView"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          app:layout_empty="@layout/view_empty"
          app:layout_progress="@layout/view_progress"
          />

the custom attr:

          <attr name="layout_empty" format="reference" />
          <attr name="layout_progress" format="reference" />
          <attr name="recyclerClipToPadding" format="boolean" />
          <attr name="recyclerPadding" format="dimension" />
          <attr name="recyclerPaddingTop" format="dimension" />
          <attr name="recyclerPaddingBottom" format="dimension" />
          <attr name="recyclerPaddingLeft" format="dimension" />
          <attr name="recyclerPaddingRight" format="dimension" />
          <attr name="scrollbarStyle">
              <flag name="insideOverlay" value="0x0" />
              <flag name="insideInset" value="0x01000000" />
              <flag name="outsideOverlay" value="0x02000000" />
              <flag name="outsideInset" value="0x03000000" />
          </attr>

**Attention** EasyRecyclerView is not a RecyclerView just contain a RecyclerView.use 'getRecyclerView()' to get the RecyclerView;

**EmptyView&LoadingView**  
xml:  
`app:layout_empty="@layout/view_empty"`  
`app:layout_progress="@layout/view_progress"`  
code:  
`void setEmptyView(View emptyView)`  
`void setProgressView(View progressView)`  
then you can show it by this whenever:  
`void showEmpty()`  
`void showProgress()`  
`void showRecycler() `  

**scrollToPosition**  
`void scrollToPosition(int position)`  
such as scroll to top;  

**control the pullToRefresh**  
`void setRefreshing(boolean isRefreshing)`   
`void setRefreshing(final boolean isRefreshing, final boolean isCallback)`//second params is callback immediately


##RecyclerArrayAdapter<T>  
there is no relation between RecyclerArrayAdapter and EasyRecyclerView.you can user any Adapter for the EasyRecyclerView,and use the RecyclerArrayAdapter for any RecyclerView.

**Data Manage**  
`void add(T object) `  
`void addAll(Collection<? extends T> collection)`  
`void addAll(T ... items) `  
`void insert(T object, int index)`  
`void remove(T object)`  
`void clear()`  
`void sort(Comparator<? super T> comparator)`  

**Header&Footer**  
`void addHeader(ItemView view)`  
`void addFooter(ItemView view)`  
ItemView is not a view but a view creator;  

        public interface ItemView {
             View onCreateView(ViewGroup parent);
             void onBindView(View itemView);
        }
        
the onCreateView and onBindView corresponding the callback in RecyclerView's Adapter,so one onCreateView with multiple onBindView;  
it recommend that add the ItemView to Adapter after the data is loaded,initialization View in onCreateView and nothing in onBindView.  
after change the Header and Footer ,call `notifyDataSetChanged();` to refresh view;  
 
 Header and Footer support `LinearLayoutManager`,`GridLayoutManager`,`StaggeredGridLayoutManager`  
 in `GridLayoutManager` you must add this:
         
         //make adapter obtain a LookUp for LayoutManager，param is maxSpan。
          gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(2));

###the API below realized by add a Footer。

**LoadMore**  
`void setMore(final int res,OnLoadMoreListener listener)`  
`void setMore(final View view,OnLoadMoreListener listener)`  
Attention when you add null or the length of data you add is 0 ,it will finish LoadMore and show NoMore;  
also you can show NoMore manually `adapter.stopMore();`  
 
**LoadError**  
`View setError(final int res)`  
`View setError(final View view)`  
use `adapter.pauseMore()` to show Error,when your loading throw an error;  
if you add data when showing Error.it will resume to load more;  
when the ErrorView display to screen again,it will resume to load more too,and callback the OnLoadMoreListener(retry).  
`adapter.resumeMore()`you can resume to load more manually,it will callback the OnLoadMoreListener immediately.   
you can put resumeMore() into the OnClickListener of ErrorView to realize click to retry.  

**NoMore**  
when loading is finished(add null or empty or stop manually),it while show in the end.  
`void setNoMore(final int res)`   
`void setNoMore(final View view)`  

##BaseViewHolder\<M\>
decoupling the ViewHolder and Adapter,new ViewHolder in Adapter and inflate view in ViewHolder.  
Example:

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
        
        -----------------------------------------------------------------------
        
        public class PersonAdapter extends RecyclerArrayAdapter<Person> {
            public PersonAdapter(Context context) {
                super(context);
            }
        
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new PersonViewHolder(parent);
            }
        }


**for detail,see the demo**






