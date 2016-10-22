package com.jude.dome;

import com.jude.dome.entites.Ad;
import com.jude.dome.entites.Person;
import com.jude.dome.entites.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class DataProvider {
    public static List<Person> getPersonList(int page){
        ArrayList<Person> arr = new ArrayList<>();
        if (page == 4)return arr;

        arr.add(new Person("http://i2.hdslb.com/52_52/user/61175/6117592/myface.jpg", "月の星く雪" + "————————第" + page + "页", "完结来补"));
        arr.add(new Person("http://i1.hdslb.com/52_52/user/6738/673856/myface.jpg", "影·蓝玉", "一看评论被***了一脸，伐开心。"));
        arr.add(new Person("http://i1.hdslb.com/account/face/1467772/e1afaf4a/myface.png", "i琳夏i", "(｀・ω・´)"));
        arr.add(new Person("http://i0.hdslb.com/52_52/user/18494/1849483/myface.jpg", "Minerva。", "为啥下载不能了？π_π"));
        arr.add(new Person("http://i0.hdslb.com/52_52/account/face/4613528/303f4f5a/myface.png", "如歌行极", "求生肉（/TДT)/"));
        arr.add(new Person("http://i0.hdslb.com/52_52/account/face/611203/76c02248/myface.png", "GERM", "第一次看 看弹幕那些说什么影帝模式啥的 感觉日了狗了 让我怎么往后看啊 艹"));
        arr.add(new Person("http://i2.hdslb.com/52_52/user/46230/4623018/myface.jpg", "じ★ve↘魅惑", "开头吾王裙子被撩起来怎么回事！→_→"));
        arr.add(new Person("http://i2.hdslb.com/52_52/user/66723/6672394/myface.jpg", "道尘一梦", "@伪 · 卫宫士郎"));
        arr.add(new Person("http://i1.hdslb.com/user/3039/303946/myface.jpg", "潘多哥斯拉", "朋友，听说过某R吗……..我呸，听说过虫群吗？(｀・ω・´)"));
        arr.add(new Person("http://i2.hdslb.com/account/face/9034989/aabbc52a/myface.png", "一只红发的猫", "道理我都懂，我就问，几楼开车←_←"));
        arr.add(new Person("http://i0.hdslb.com/account/face/1557783/8733bd7b/myface.png", "Mikuの草莓胖次", "扶..扶我起来,喝了最后这一瓶营养快线，让我撸死up"));
        arr.add(new Person("http://i2.hdslb.com/user/3716/371679/myface.jpg", "Absolute Field", "朋也，看过里番吗？"));
        arr.add(new Person("http://i1.hdslb.com/account/face/9045165/4b11d894/myface.png", "琪雅之约", "摩西摩西.警察局么？"));
        return arr;
    }

    public static List<Ad> getAdList(){
        ArrayList<Ad> arr = new ArrayList<>();
        arr.add(new Ad("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg","http://www.bilibili.com/topic/v2/1004.html"));
        arr.add(new Ad("http://i1.hdslb.com/u_user/80fcc32d0b5d3565377847bd9dd06dc3.jpg","http://www.bilibili.com/topic/1003.html"));
        arr.add(new Ad("http://i2.hdslb.com/u_user/f19f0e44328a4190a48acf503c737c50.png","http://yoo.bilibili.com/html/activity/cq2015/index.html"));
        arr.add(new Ad("http://i1.hdslb.com/u_user/7ee1aeadc8257f43fa6b806717c9c398.png","http://www.bilibili.com/html/activity-acsociety.html"));
        return arr;
    }

    public static List<Object> getPersonWithAds(int page){
        ArrayList<Object> arrAll = new ArrayList<>();
        List<Ad> arrAd = getAdList();
        int index = 0;
        for (Person person : getPersonList(page)) {
            arrAll.add(person);
            //按比例混合广告
            if (Math.random()<0.2){
                arrAll.add(arrAd.get(index%arrAd.size()));
                index++;
            }
        }

        return arrAll;
    }

    static final Picture[] VIRTUAL_PICTURE = {
            new Picture(566,800,"http://o84n5syhk.bkt.clouddn.com/57154327_p0.png"),
            new Picture(2126,1181,"http://o84n5syhk.bkt.clouddn.com/57180221_p0.jpg"),
            new Picture(1142,800,"http://o84n5syhk.bkt.clouddn.com/57174070_p0.jpg"),
            new Picture(550,778,"http://o84n5syhk.bkt.clouddn.com/57166531_p0.jpg"),
            new Picture(1085,755,"http://o84n5syhk.bkt.clouddn.com/57151022_p0.jpg"),
            new Picture(656,550,"http://o84n5syhk.bkt.clouddn.com/57172236_p0.jpg"),
            new Picture(1920,938,"http://o84n5syhk.bkt.clouddn.com/57174564_p0.jpg"),
            new Picture(1024,683,"http://o84n5syhk.bkt.clouddn.com/57156832_p0.jpg"),
            new Picture(723,1000,"http://o84n5syhk.bkt.clouddn.com/57151474_p0.png"),
            new Picture(2000,1667,"http://o84n5syhk.bkt.clouddn.com/57156623_p0.png"),
    };
    public static ArrayList<Picture> getPictures(int page){
        ArrayList<Picture> arrayList = new ArrayList<>();
        for (int i = 0; i < VIRTUAL_PICTURE.length; i++) {
            arrayList.add(VIRTUAL_PICTURE[i]);
        }
        return arrayList;
    }

    static final int[] NarrowImage = {
            R.drawable.yy01,
            R.drawable.yy02,
            R.drawable.yy03,
            R.drawable.yy04,
            R.drawable.yy05,
            R.drawable.yy06,
            R.drawable.yy07,
            R.drawable.yy08,
            R.drawable.yy09,
    };
    public static ArrayList<Integer> getNarrowImage(int page){
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (page == 4)return arrayList;

        for (int i = 0; i < NarrowImage.length; i++) {
            arrayList.add(NarrowImage[i]);
        }
        return arrayList;
    }
}
