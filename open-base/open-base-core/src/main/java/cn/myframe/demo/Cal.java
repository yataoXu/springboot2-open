package cn.myframe.demo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/7/15/015 8:29
 * @Version 1.0
 */
public class Cal {

    static int finalResult = 100;

    //活动集合
    static List<Activity> actList = new ArrayList<Activity>();

    static{
        Activity activity3 = new Activity(){
            @Override
            public void increaseCount() {
                super.increaseCount();
                this.perResult = 10;
                this.result = this.count * perResult;
                this.cost = 100*this.count;
            }
        };
        actList.add(activity3);


        Activity activity1 = new Activity(){
            @Override
            public void increaseCount() {
                super.increaseCount();
                this.result = this.count * 2;
                this.cost = this.cost+ this.count * 5;
            }
        };
        actList.add(activity1);

        Activity activity2 = new Activity(){
            @Override
            public void increaseCount() {
                super.increaseCount();
                this.result = this.count * 2;
                this.cost = this.cost+((this.count-1)/2+1) * 5;
            }
        };
        actList.add(activity2);



    }

    public static void cal(){
        List<Activity> list = new ArrayList<>();
        calculate(0,0,0);
    }

    //组合集合
    static List<Activity> composeList = new ArrayList<Activity>();

    static List<Activity> minList = new ArrayList<Activity>();
    static int minCost = Integer.MAX_VALUE ;

    public static void calculate(int costed, int resluted,int level){
        Activity activity = actList.get(level);
        composeList.add(level,activity);
        if(level != actList.size() -1){
            int residue = finalResult-resluted;
            for (int j = 0; j < residue/activity.getPerResult(); j++) {
                activity.increaseCount();
                if(residue > activity.getResult()){
                    calculate( costed+activity.getCost(),
                            resluted+activity.getResult(), level+1);
                }
            }

        }
        int lastCose = compute( costed,  resluted, activity);
        if(lastCose < minCost){
            minList = deepCopy(composeList);
            System.out.println(JSON.toJSONString(minList));
            minCost = lastCose;
          //  composeList.clear();
        }
    }

    public static int compute(int costed, int resluted,Activity activity){
        activity.clear();
        //剩余任务
        int residue = finalResult-resluted;
        for (int j = 0; j < residue/activity.getPerResult(); j++) {
            activity.increaseCount();
        }
        return costed+activity.getCost();
    }



    public static void test(){

        Activity activity1 = new Activity(){
            @Override
            public void increaseCount() {
                super.increaseCount();
                this.result = this.count * 2;
                this.cost = this.count * 5;
            }
        };
        activity1.increaseCount();

        System.out.println(JSON.toJSONString(activity1));

    }

    public static void main(String[] args) {
        cal();
        System.out.println(JSON.toJSONString(minList));
    }

    public static <T> List<T> deepCopy(List<T> src)  {
        try{
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



}

@Data
class Activity implements Serializable {

    //活动编码
    public int actCode;

    //次数
    public int count = 0;

    //每次收获
    public int perResult = 2;

    //收获
    public int result = 0;

    //花费
    public int cost = 0;

    public void increaseCount(){
        count = count + 1;
    }

    public void clear(){
        this.count = 0;
        this.cost = 0;
        this.result = 0;
    }



}


