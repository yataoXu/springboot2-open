package cn.myframe.demo;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.CRC32;

/**
 * @Author: ynz
 * @Date: 2019/2/22/022 13:24
 * @Version 1.0
 */
public class D1 {

    public static void main(String[] args) {


        CRC32 crc32 = new CRC32();
        crc32.update("1".getBytes());
        System.out.println(crc32.getValue());
        System.out.println(crc32.getValue()&Integer.MAX_VALUE);
        crc32.update("10.11.1.55".getBytes());
        System.out.println(crc32.getValue());
        System.out.println("abc".hashCode()); ;
        System.out.println("10.11.1.56".hashCode());
        System.out.println("10.11.1.55".hashCode()); ;
        System.out.println(1 & Integer.MAX_VALUE);

        TreeMap<Integer,String > treemap = new TreeMap<>();
        treemap.put(2, "two");
        treemap.put(1, "one");
        treemap.put(3, "three");
        treemap.put(6, "six");
        treemap.put(5, "five");

        System.out.println(treemap.ceilingKey(4));
    }
}
