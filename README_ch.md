# EasyRecyclerView
[中文](https://github.com/Jude95/EasyRecyclerView/blob/master/README_ch.md) ｜ [English](https://github.com/Jude95/EasyRecyclerView/blob/master/README.md)

将开发中常用的RecyclerView的各种需求封装进库。提升开发效率。  
重点在Adapter与viewholder的封装。他们之间彻底解耦。  
adapter工作更少，仅负责业务逻辑。所以如果你使用mvp架构可以放进presenter层。  
viewholder负责View展示与Adapter没有任何耦合，将可以到处复用。并不会影响运行效率。  
并且adapter支持数据管理，Header与Footer添加，加载更多。没有更多。加载错误  
使用了部分[Malinskiy/SuperRecyclerView](https://github.com/Malinskiy/SuperRecyclerView)的代码，将更多功能交给了adapter实现。    


##依赖
```groovy
compile 'com.jude:easyrecyclerview:3.5.8'
```

##示例
![recycler.gif](recycler3.gif)
##EasyRecyclerView的使用

```xml
<com.jude.easyrecyclerview.EasyRecyclerView
  android:id="@+id/recyclerView"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:layout_empty="@layout/view_empty"
  app:layout_progress="@layout/view_progress"
  />
```

平常这样就好。

属性列表

```xml
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
```
注意EasyRecyclerView本质并不是一个RecyclerView

**设置空白View与加载View**  
xml中  
```xml
app:layout_empty="@layout/view_empty"
app:layout_progress="@layout/view_progress"
```

代码中  
```java
void setEmptyView(View emptyView)
void setProgressView(View progressView)
```
然后可以随时显示他们  
```java
void showEmpty()
void showProgress()  
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
void setRefreshing(final boolean isRefreshing, final boolean isCallback); //第二个参数控制是否立即回调
```
##RecyclerArrayAdapter<T>  
这个Adapter与本RecyclerView没有任何耦合。你可以使用其他adapter。也可以把本adapter用于其他RecyclerView  

**整合了数据增删的功能**  
```java
void add(T object);
void addAll(Collection<? extends T> collection);
void addAll(T ... items);
void insert(T object, int index);
void remove(T object)
void clear()
void sort(Comparator<? super T> comparator)
```

**整合的Header与Footer的实现**  
```java
void addHeader(ItemView view)
void addFooter(ItemView view)  
```
ItemView不是view而是view生成器  
对应Adapter的onCreate与onBind方法,所以onCreate后会多次onBind。  
建议数据加载完毕后再add。onCreate里初始化UI。不使用onBind。  
添加删除过后都需手动`notifyDataSetChanged();`  

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
    public boolean onItemClick(int position) {
        return true;
    }
});
```

与在ViewHolder中给itemView设置OnClickListener等效。若重复设置ViewHolder中的设置会被替换。  
如果在RecyclerView布局以后再设置。需要再使用'notifyDataSetChange()'。

###下面的功能是在adapter最后添加一个footer。来显示各种状态。

**加载更多**  
```java
void setMore(final int res,OnLoadMoreListener listener);
void setMore(final View view,OnLoadMoreListener listener);
```
注意一定当添加0条数据或null时,会结束加载更多,显示没有更多。  
也可以在最后一页手动调用`adapter.stopMore();`  
 
**加载错误**  
```java
View setError(final int res)
View setError(final View view)
```
`adapter.pauseMore()`暂停加载更多，显示错误View。  
暂停时如果再次添加数据。自动恢复加载更多。  
当错误View再次被显示时。会恢复成加载更多view。并回掉加载更多;  
`adapter.resumeMore()`继续加载更多，显示加载更多View，并立即回调加载更多。  
比如你可以给错误View设置点击重试。点击调用resumeMore。  

**没有更多**  
在adapter里设置，当停止加载后就会显示在最后一个。  
```java
void setNoMore(final int res)
void setNoMore(final View view)
```

##BaseViewHolder\<M\>
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

## 另外
虽然与我的库没什么关系，但很多人在问就写一下吧。item的**水波纹效果**  
在你item的View加上这一条属性：  
`android:foreground="?android:attr/selectableItemBackground"`  
就好了...

**详细用法请看demo**

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





