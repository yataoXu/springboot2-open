package cn.myframe.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.CRC32;

/**
 * @Author: ynz
 * @Date: 2019/2/22/022 16:34
 * @Version 1.0
 */
public class ConsistentHash {

    private static String[] serverNode = {"10.10.1.1","10.10.1.2","10.10.1.3","10.10.1.4"};

    //保持hash环数据
    private static TreeMap<Integer,String> serverMap = new TreeMap<>();

    //虚拟节点数
    private final static  int V_NODE_NUM = 10000;

    static{
        for (int i = 0; i < V_NODE_NUM; i++) {
            for (int j = 0; j < serverNode.length; j++) {
                String hashNode = serverNode[j]+"_"+i;
                serverMap.put(getFnv1_32_Hash(hashNode),serverNode[j]);
            }
        }

    }

    //使用Crc32算法计算服务器的Hash值
    public static int getCrc_32_Hash(String node){
        CRC32 crc32 = new CRC32();
        crc32.update(node.getBytes());
        int hash = (int)(crc32.getValue() & Integer.MAX_VALUE);
        return hash;
    }


    //使用FNV1_32_HASH算法计算服务器的Hash值
    private static int getFnv1_32_Hash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    private static String getNode(String data){
        Integer key = serverMap.ceilingKey(getFnv1_32_Hash(data));
        if(key == null){
            key =  serverMap.firstKey();
        }
        return serverMap.get(key);
    }


    public static void main(String[] args) {
        System.out.println(serverMap.size());
        Map<String,AtomicInteger> map = new HashMap<>();
        for (int i = 0; i < serverNode.length; i++) {
            map.put(serverNode[i],new AtomicInteger());
        }
        for (int i = 0; i < 1000000; i++) {
            //System.out.println(getNode("data"+i));
            String node = getNode("value"+i);
            if(map.containsKey(node)){
                map.get(node).getAndIncrement();
            }
        }
        for(Map.Entry<String,AtomicInteger> entry :map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}
