package cn.myframe.controller;

import cn.myframe.config.ZkClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.VersionedValue;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Api(tags="zookeeper基本操作")
@RequestMapping("/zk")
@RestController
@Slf4j
public class ZookeeperController {

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ZkClient zkClientTest;

    /**
     * 创建节点
     * @param type
     * @param znode
     * @return
     */
    @ApiOperation(value = "创建节点",notes = "在命名空间下创建节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="type",value = "节点类型:<br> 0 持久化节点<br> 1 临时节点<br>  2 持久顺序节点<br> 3 临时顺序节点",
                    allowableValues = "0,1,2,3",defaultValue="3",paramType = "path",required = true,dataType = "Long"),
            @ApiImplicitParam(name ="znode",value = "节点名称",paramType = "path",required = true,dataType = "String"),
            @ApiImplicitParam(name ="nodeData",value = "节点数据",paramType = "body",dataType = "String")
    })
    @RequestMapping(value = "/create/{type}/{znode}",method=RequestMethod.POST)
    private String create(@PathVariable Integer type,@PathVariable String znode,@RequestBody String nodeData){
        znode = "/" + znode;
        try {
            zkClient.createNode(CreateMode.fromFlag(type),znode,nodeData);
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return znode;
    }

    /**
     * 设置节点数据
     * @param znode
     * @return
     */
    @ApiOperation(value = "设置节点数据",notes = "设置节点数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="znode",value = "节点名称",paramType = "body",required = true,dataType = "String"),
            @ApiImplicitParam(name ="nodeData",value = "节点数据",paramType = "query",required = true,dataType = "String")
    })
    @RequestMapping(value = "/update",method=RequestMethod.POST)
    public String update(@RequestBody String znode,@RequestParam String nodeData){
        znode = "/" + znode;
        zkClient.setNodeData(znode,nodeData.getBytes());
        return "sucess";
    }

    @ApiOperation(value = "删除节点",notes = "删除节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="znode",value = "节点名称",paramType = "query",required = true,dataType = "String")
    })
    @RequestMapping(value = "/delete",method=RequestMethod.GET)
    public String delete(@RequestParam String znode){
        znode = "/" + znode;
        zkClient.deleteNode(znode);
        return "success";
    }

    @ApiOperation(value = "查找节点的内容",notes = "查找节点的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="znode",value = "节点名称",paramType = "body",required = true,dataType = "String")
    })
    @RequestMapping(value = "/find",method=RequestMethod.POST)
    public String find(@RequestBody String znode){
        znode = "/" + znode;
        byte[] b =  zkClient.getNodeData(znode);
        return new String(b);
    }

    /**
     * 给节点添加读写锁
     * @param znode
     * @return
     */
    @ApiOperation(value = "添加读写锁",notes = "写锁跟读锁互斥,读锁跟读锁共享")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="lockType",value = "锁类型:<br> 0 写锁<br> 1 读锁",
                    allowableValues = "0,1",defaultValue="0",paramType = "query",required = true,dataType = "Long"),
            @ApiImplicitParam(name ="znode",value = "节点名称(不需要节点存在)",paramType = "query",required = true,dataType = "String")
    })
    @RequestMapping(value = "/writeLock",method=RequestMethod.GET)
    public String readLock(@RequestParam Integer lockType,@RequestParam String znode){
        znode = "/" + znode;
        InterProcessReadWriteLock readWriteLock = zkClient.getReadWriteLock(znode);
        InterProcessMutex writeLock = readWriteLock.writeLock();
        InterProcessMutex readLock = readWriteLock.readLock();
        Runnable writeRunnable = ()->{
            try {
                log.info("------write lock-----------");
                writeLock.acquire();
                log.info("write acquire");
                Thread.sleep(10_000);
                log.info("write release");
                writeLock.release();

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Runnable readRunnable = ()->{
            try {
                log.info("-------read lock----------");
                readLock.acquire();
                log.info("read acquire");
                Thread.sleep(20_000);
                log.info("read release");
                readLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if(lockType == 0 ){
            new Thread(writeRunnable).start();
        }else if(lockType == 1){
            new Thread(readRunnable).start();
        }
        return "success";
    }

    /**
     * 监听节点
     * @param znode
     * @return
     */
    @ApiOperation(value = "监听节点",notes = "监控整个树上的所有节点")
    @ApiImplicitParams(
            @ApiImplicitParam(name ="znode",value = "节点名称",paramType = "body",required = true,dataType = "String")
    )
    @RequestMapping(value="/watchPath",method=RequestMethod.POST)
    public String watchPath(@RequestBody  String znode){
        znode = "/" + znode;
        zkClient.watchPath(znode,(client1, event) ->{
            log.info("event:" + event.getType() +
                    " |path:" + (null != event.getData() ? event.getData().getPath() : null));

            if(event.getData()!=null && event.getData().getData()!=null){
                log.info("发生变化的节点内容为：" + new String(event.getData().getData()));
            }
        });
        return "success";
    }

    /**
     * 测试计算器
     * 并发越高耗时越长
     * 要自己实现获取锁失败重试
     * @return
     */
    @ApiOperation(value = "模拟分布式计数器",notes = "模拟分布式计数器")
    @RequestMapping(value="/counter",method=RequestMethod.POST)
    public String counter(@RequestBody  String znode){
        SharedCount baseCount = new SharedCount(zkClientTest.getClient(), znode, 0);
        try {
            baseCount.start();
            //生成线程池
            ExecutorService executor = Executors.newCachedThreadPool();
            Consumer<SharedCount> consumer = (SharedCount count) -> {
                try {
                    List<Callable<Boolean>> callList = new ArrayList<>();
                    Callable<Boolean> call = () -> {
                        boolean result = false;
                        try {
                            Long time = System.currentTimeMillis();
                            while(!result){
                                VersionedValue<Integer> oldVersion = baseCount.getVersionedValue();
                                int newCnt = oldVersion.getValue() + 1;
                                result = baseCount.trySetCount(oldVersion, newCnt);
                                if(System.currentTimeMillis()-time>10_000||result){
                                    break;
                                }
                                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100)+1);
                            }
                        } catch (Exception e) {
                        }
                        return result;
                    };
                    //5个线程
                    for (int i = 0; i < 100; i++) {
                        callList.add(call);
                    }
                    List<Future<Boolean>> futures = executor.invokeAll(callList);
                } catch (Exception e) {

                }
            };
            //测试分布式int类型的计数器
            consumer.accept(baseCount);
            System.out.println("final cnt : " + baseCount.getCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success："+baseCount.getCount();
    }

    /**
     * DistributedAtomicLong计数器可以自己设置重试的次数与间隔
     * 并发越高耗时越长
     * 要自己实现获取锁失败重试
     */
    @ApiOperation(value = "模拟分布式计数器2",notes = "模拟分布式计数器2")
    @RequestMapping(value="/counter2",method=RequestMethod.POST)
    public String distributedCount(@RequestBody  String znode) throws Exception {
        DistributedAtomicLong distributedAtomicLong = new DistributedAtomicLong(
                zkClientTest.getClient(), znode, new RetryNTimes(10, 30));
        //生成线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        Consumer<DistributedAtomicLong> consumer = (DistributedAtomicLong count) -> {
            try {
                List<Callable<Boolean>> callList = new ArrayList<>();
                Callable<Boolean> call = () -> {
                    boolean result = false;
                    try {
                        AtomicValue<Long> val = count.increment();
                        System.out.println("old cnt: "+val.preValue()+"   new cnt : "+ val.postValue()+"  result:"+val.succeeded());
                        result = val.succeeded();
                    } catch (Exception e) {
                    } finally {
                    }
                    return result;
                };
                //5个线程
                for (int i = 0; i < 500; i++) {
                    callList.add(call);
                }
                List<Future<Boolean>> futures = executor.invokeAll(callList);
            } catch (Exception e) {

            }
        };
        consumer.accept(distributedAtomicLong);
        return "success："+distributedAtomicLong.get().postValue();
    }


    /**
     *
     * @return
     * @throws KeeperException
     */
    @ApiOperation(value = "模拟服务注册和随机获取服务",notes = "模拟服务注册和随机获取服务")
    @RequestMapping(value="/serviceRegistry",method=RequestMethod.POST)
    public String serviceRegistry() throws KeeperException {
        //服务注册
        zkClient.createNode(CreateMode.fromFlag(1),"/test/hello_service1","http://1270.0.1:8001/");
        zkClient.createNode(CreateMode.fromFlag(1),"/test/hello_service2","http://1270.0.1:8002/");
        zkClient.createNode(CreateMode.fromFlag(1),"/test/hello_service3","http://1270.0.1:8003/");
        return zkClient.getRandomData("/test");
    }

}
