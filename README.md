# EasyRecyclerView
[中文](https://github.com/Jude95/EasyRecyclerView/blob/master/README_ch.md) ｜ [English](https://github.com/Jude95/EasyRecyclerView/blob/master/README.md)

Encapsulate many API about RecyclerView into the library,such as arrayAdapter,pull to refresh,auto load more,no more and error in the end,header&footer.  
The library uses a new usage of ViewHolder,decoupling the ViewHolder and Adapter.  
Adapter will do less work,adapter only direct the ViewHolder,if you use MVP,you can put adapter into presenter.ViewHolder only show the item,then you can use one ViewHolder for many Adapter.   
Part of the code modified from [Malinskiy/SuperRecyclerView](https://github.com/Malinskiy/SuperRecyclerView),make more functions handed by Adapter.    


# Dependency
```groovy
compile 'com.jude:easyrecyclerview:4.4.2'
```

# ScreenShot
![recycler.gif](recycler3.gif)
# Usage
## EasyRecyclerView
```xml
<com.jude.easyrecyclerview.EasyRecyclerView
  android:id="@+id/recyclerView"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:layout_empty="@layout/view_empty"
  app:layout_progress="@layout/view_progress"
  app:layout_error="@layout/view_error"
  app:recyclerClipToPadding="true"
  app:recyclerPadding="8dp"
  app:recyclerPaddingTop="8dp"
  app:recyclerPaddingBottom="8dp"
  app:recyclerPaddingLeft="8dp"
  app:recyclerPaddingRight="8dp"
  app:scrollbarStyle="insideOverlay"//insideOverlay or insideInset or outsideOverlay or outsideInset
  app:scrollbars="none"//none or vertical or horizontal
  />
```

**Attention** EasyRecyclerView is not a RecyclerView just contain a RecyclerView.use 'getRecyclerView()' to get the RecyclerView;

**EmptyView&LoadingView&ErrorView**  
xml:  
```xml
app:layout_empty="@layout/view_empty"
app:layout_progress="@layout/view_progress"
app:layout_error="@layout/view_error"
```

code:  
```java
void setEmptyView(View emptyView)
void setProgressView(View progressView)
void setErrorView(View errorView)
```

then you can show it by this whenever:  

```java
void showEmpty()
void showProgress()  
void showError()  
void showRecycler()
```

**scrollToPosition**  
```java
void scrollToPosition(int position); // such as scroll to top
```

**control the pullToRefresh**  
```java
void setRefreshing(boolean isRefreshing);
void setRefreshing(final boolean isRefreshing, final boolean isCallback); //second params is callback immediately
```


##RecyclerArrayAdapter<T>  
there is no relation between RecyclerArrayAdapter and EasyRecyclerView.you can user any Adapter for the EasyRecyclerView,and use the RecyclerArrayAdapter for any RecyclerView.

**Data Manage**
```java
void add(T object);
void addAll(Collection<? extends T> collection);
void addAll(T ... items);
void insert(T object, int index);
void update(T object, int index);
void remove(T object);
void clear();
void sort(Comparator<? super T> comparator);
```

**Header&Footer**
```java
void addHeader(ItemView view)
void addFooter(ItemView view)  
```

ItemView is not a view but a view creator;  

```java
public interface ItemView {
     View onCreateView(ViewGroup parent);
     void onBindView(View itemView);
}
```

The onCreateView and onBindView correspond the callback in RecyclerView's Adapter,so adapter will call `onCreateView` once and `onBindView` more than once;  
It recommend that add the ItemView to Adapter after the data is loaded,initialization View in onCreateView and nothing in onBindView. 
 
 Header and Footer support `LinearLayoutManager`,`GridLayoutManager`,`StaggeredGridLayoutManager`.  
 In `GridLayoutManager` you must add this:
```java         
//make adapter obtain a LookUp for LayoutManager，param is maxSpan。
gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(2));
```

**OnItemClickListener&OnItemLongClickListener**  
```java
adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(int position) {
        //position not contain Header
    }
});

adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(int position) {
        return true;
    }
});
```
equal 'itemview.setOnClickListener()' in ViewHolder.  
if you set listener after RecyclerView has layout.you should use 'notifyDataSetChange()';

###the API below realized by add a Footer。

**LoadMore**  
```java
void setMore(final int res,OnMoreListener listener);
void setMore(final View view,OnMoreListener listener);
```
Attention when you add null or the length of data you add is 0 ,it will finish LoadMore and show NoMore;  
also you can show NoMore manually `adapter.stopMore();`  
 
**LoadError**  
```java
void setError(final int res,OnErrorListener listener)
void setError(final View view,OnErrorListener listener)
```
use `adapter.pauseMore()` to show Error,when your loading throw an error;  
if you add data when showing Error.it will resume to load more;  
when the ErrorView display to screen again,it will resume to load more too,and callback the OnLoadMoreListener(retry).  
`adapter.resumeMore()`you can resume to load more manually,it will callback the OnLoadMoreListener immediately.   
you can put resumeMore() into the OnClickListener of ErrorView to realize click to retry.  

**NoMore**  
```java
void setNoMore(final int res,OnNoMoreListener listener)
void setNoMore(final View view,OnNoMoreListener listener)
```
when loading is finished(add null or empty or stop manually),it while show in the end.  

## BaseViewHolder\<M\>
decoupling the ViewHolder and Adapter,new ViewHolder in Adapter and inflate view in ViewHolder.  
Example:

```java
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
```

## Decoration
Now there are three commonly used decoration provide for you.  
**DividerDecoration**  
Usually used in LinearLayoutManager.add divider between items.
```java
DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,72),0);//color & height & paddingLeft & paddingRight
itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
recyclerView.addItemDecoration(itemDecoration);
```
this is the demo:  
<image src="http://o84n5syhk.bkt.clouddn.com/divider.jpg?imageView2/2/w/300" width=300/>


**SpaceDecoration**  
Usually used in GridLayoutManager and StaggeredGridLayoutManager.add space between items.  
```java
SpaceDecoration itemDecoration = new SpaceDecoration((int) Utils.convertDpToPixel(8,this));//params is height
itemDecoration.setPaddingEdgeSide(true);//whether add space for left and right adge.default is true.
itemDecoration.setPaddingStart(true);//whether add top space for the first line item(exclude header).default is true.
itemDecoration.setPaddingHeaderFooter(false);//whether add space for header and footer.default is false.
recyclerView.addItemDecoration(itemDecoration);
```
this is the demo:  
<image src="http://o84n5syhk.bkt.clouddn.com/space.jpg?imageView2/2/w/300" width=300/>

**StickHeaderDecoration**  
Group the items,add a GroupHeaderView for each group.The usage of StickyHeaderAdapter is the same with RecyclerView.Adapter.
this part is modified from [edubarr/header-decor](https://github.com/edubarr/header-decor)
```java
StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(this));
decoration.setIncludeHeader(false);
recyclerView.addItemDecoration(decoration);
```
for example:   
<image src="http://7xkr5d.com1.z0.glb.clouddn.com/recyclerview_sticky.png?imageView2/2/w/300" width=300/>

**for detail,see the demo**

License
-------

    Copyright 2015 Jude

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.





