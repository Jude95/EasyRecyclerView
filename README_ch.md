# EasyRecyclerView
[中文](https://github.com/Jude95/EasyRecyclerView/blob/master/README_ch.md) ｜ [English](https://github.com/Jude95/EasyRecyclerView/blob/master/README.md)

将开发中常用的RecyclerView的各种需求封装进库。提升开发效率。  
重点在Adapter与viewholder的封装。他们之间彻底解耦。  
adapter工作更少，仅负责业务逻辑。所以如果你使用mvp架构可以放进presenter层。  
viewholder负责View展示与Adapter没有任何耦合，将可以到处复用。并不会影响运行效率。  
并且adapter支持数据管理，Header与Footer添加，加载更多。没有更多。加载错误  
使用了部分[Malinskiy/SuperRecyclerView](https://github.com/Malinskiy/SuperRecyclerView)的代码，将更多功能交给了adapter实现。    


## 依赖
```groovy
compile 'com.jude:easyrecyclerview:4.4.2'
```

## 示例
![recycler.gif](recycler3.gif)
## EasyRecyclerView的使用

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
所有属性都不是必须。
注意EasyRecyclerView本质并不是一个RecyclerView

**设置空白View&加载View&错误View**  
xml中  
```xml
app:layout_empty="@layout/view_empty"
app:layout_progress="@layout/view_progress"
app:layout_error="@layout/view_error"
```

代码中  
```java
void setEmptyView(View emptyView)
void setProgressView(View progressView)
void setErrorView(View errorView)
```
然后可以随时显示他们  
```java
void showEmpty()
void showProgress()  
void showError()  
void showRecycler()
```

**跳到固定位置**  
```java
void scrollToPosition(int position); 
```
比如回到顶部什么的

**下拉刷新的控制**  
```java
void setRefreshing(boolean isRefreshing);
void setRefreshing(final boolean isRefreshing, final boolean isCallback); //第二个参数控制是否回调更新数据方法
```
##RecyclerArrayAdapter<T>  
这个Adapter与本RecyclerView没有任何耦合。你可以使用其他adapter。也可以把本adapter用于其他RecyclerView  

**整合了数据增删的功能**  
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

**整合的Header与Footer的实现**  
```java
void addHeader(ItemView view)
void addFooter(ItemView view)  
```
ItemView不是view而是view生成器  
对应Adapter的onCreate与onBind方法,所以onCreate后会多次onBind。  
建议数据加载完毕后再add。onCreate里初始化UI。不使用onBind。  

```java
public interface ItemView {
     View onCreateView(ViewGroup parent);
     void onBindView(View itemView);
}
```

Header与Footer完美适配`LinearLayoutManager`,`GridLayoutManager`,`StaggeredGridLayoutManager`  
在GridLayoutManager模式中需额外加一句
        
```java         
//make adapter obtain a LookUp for LayoutManager，param is maxSpan。
gridLayoutManager.setSpanSizeLookup(adapter.obtainGridSpanSizeLookUp(2));
```

**整合OnItemClickListener与OnItemLongClickListener**  

```java
adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(int position) {
        //position不包含Header
    }
});

adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(int position) {
        return true;
    }
});
```

与在ViewHolder中给itemView设置OnClickListener等效。若重复设置ViewHolder中的设置会被替换。  
如果在RecyclerView布局以后再设置。需要再使用'notifyDataSetChange()'。

### 下面的功能是在adapter最后添加一个footer。来显示各种状态。

**加载更多**  
```java
void setMore(final int res,OnMoreListener listener);
void setMore(final View view,OnMoreListener listener);
```
注意一定当添加0条数据或null时,会结束加载更多,显示没有更多。  
也可以在最后一页手动调用`adapter.stopMore();`  
 
**加载错误**  
```java
void setError(final int res,OnErrorListener listener)
void setError(final View view,OnErrorListener listener)
```
`adapter.pauseMore()`暂停加载更多，显示错误View。  
暂停时如果再次添加数据。自动恢复加载更多。  
当错误View再次被显示时。会恢复成加载更多view。并回掉加载更多;  
`adapter.resumeMore()`继续加载更多，显示加载更多View，并立即回调加载更多。  
比如你可以给错误View设置点击重试。点击调用resumeMore。  

**没有更多**  
在adapter里设置，当停止加载后就会显示在最后一个。  
```java
void setNoMore(final int res,OnNoMoreListener listener)
void setNoMore(final View view,OnNoMoreListener listener)
```

## BaseViewHolder\<M\>
这个ViewHolder将每个item与adapter解耦。adapter只管实例化对应ViewHolder.每个Item的view生成,findviewbyid,UI修改都由viewHolder自己管理。  
列如:

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
这里提供了3种常用Decoration供大家使用。  
**DividerDecoration**  
通常用在LinearLayoutManager的情况下。在item之间添加分割线。  
```java
DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,72),0);//颜色 & 高度 & 左边距 & 右边距
itemDecoration.setDrawLastItem(true);//有时候你不想让最后一个item有分割线,默认true.
itemDecoration.setDrawHeaderFooter(false);//是否对Header于Footer有效,默认false.
recyclerView.addItemDecoration(itemDecoration);
```
这是效果:  
<image src="http://o84n5syhk.bkt.clouddn.com/divider.jpg?imageView2/2/w/300" width=300/>

**SpaceDecoration**  
通常用于GridLayoutManager和StaggeredGridLayoutManager。在View之间添加间距。  
```java
SpaceDecoration itemDecoration = new SpaceDecoration((int) Utils.convertDpToPixel(8,this));//参数是距离宽度
itemDecoration.setPaddingEdgeSide(true);//是否为左右2边添加padding.默认true.
itemDecoration.setPaddingStart(true);//是否在给第一行的item添加上padding(不包含header).默认true.
itemDecoration.setPaddingHeaderFooter(false);//是否对Header于Footer有效,默认false.
recyclerView.addItemDecoration(itemDecoration);
```
这是效果:  
<image src="http://o84n5syhk.bkt.clouddn.com/space.jpg?imageView2/2/w/300" width=300/> 

**StickHeaderDecoration**  
将Item分组，并添加每一组的Header，Header会悬浮在当前分组上。
StickyHeaderAdapter用法与RecyclerView.Adapter相同。
此部分代码修改自[edubarr/header-decor](https://github.com/edubarr/header-decor)
```java
StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(this));
decoration.setIncludeHeader(false);
recyclerView.addItemDecoration(decoration);
```
for example:
<image src="http://7xkr5d.com1.z0.glb.clouddn.com/recyclerview_sticky.png?imageView2/2/w/300" width=300/>


## 另外
虽然与我的库没什么关系，但很多人在问就写一下吧。item的**水波纹效果**  
在你item的View加上这一条属性：  
`android:foreground="?android:attr/selectableItemBackground"`  
就好了...


**详细用法请看demo**
------


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





