package cn.myframe.proxy.config;

import cn.myframe.properties.NettyProxyServerProperties;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * server config
 *
 * @author fengfei
 *
 */
@Data
public class ProxyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 配置文件为config.json */
    public static final String CONFIG_FILE;

    private static Logger logger = LoggerFactory.getLogger(ProxyConfig.class);

    static {

        // 代理配置信息存放在用户根目录下
        String dataPath = System.getProperty("user.home") + "/" + ".lanproxy/";
        File file = new File(dataPath);
        if (!file.isDirectory()) {
            file.mkdir();
        }

        CONFIG_FILE = dataPath + "/config.json";
    }

    /** 代理服务器绑定主机host */
    private String serverBind;

    /** 代理服务器与代理客户端通信端口 */
    private Integer serverPort;

    /** 配置服务绑定主机host */
    private String configServerBind;

    /** 配置服务端口 */
    private Integer configServerPort;

    /** 配置服务管理员用户名 */
    private String configAdminUsername;

    /** 配置服务管理员密码 */
    private String configAdminPassword;

    /** 代理客户端，支持多个客户端 */
    private List<Client> clients;

    /** 更新配置后保证在其他线程即时生效 */
    private static ProxyConfig instance = new ProxyConfig();;

    /** 代理服务器为各个代理客户端（key）开启对应的端口列表（value） */
    private static volatile Map<String, List<Integer>> clientInetPortMapping = new HashMap<String, List<Integer>>();

    /** 代理服务器上的每个对外端口（key）对应的代理客户端背后的真实服务器信息（value） */
    private static volatile Map<Integer, String> inetPortLanInfoMapping = new HashMap<Integer, String>();

    /** 配置变化监听器 */
    private List<ConfigChangedListener> configChangedListeners = new ArrayList<ConfigChangedListener>();

    private ProxyConfig() {
        update(null);
    }

    public ProxyConfig(NettyProxyServerProperties nettyProxyProperties){
        Map<String, List<Integer>> clientInetPortMapping = new HashMap<String, List<Integer>>();
        Map<Integer, String> inetPortLanInfoMapping = new HashMap<Integer, String>();
        if(nettyProxyProperties.getClientServer() != null && nettyProxyProperties.getClientServer().size() >0){

            for(NettyProxyServerProperties.ClientServer clientSever : nettyProxyProperties.getClientServer()){
                String clientKey = clientSever.getKeys();
                if (clientInetPortMapping.containsKey(clientKey)) {
                    throw new IllegalArgumentException("密钥同时作为客户端标识，不能重复： " + clientKey);
                }
        //        List<ClientProxyMapping> mappings = client.getProxyMappings();
                List<Integer> ports = Arrays.asList(clientSever.getPort());
                clientInetPortMapping.put(clientKey, ports);

                inetPortLanInfoMapping.put(clientSever.getPort(), clientSever.getBind());
              /*  for (ClientProxyMapping mapping : mappings) {
                    Integer port = mapping.getInetPort();
                    ports.add(port);
                    if (inetPortLanInfoMapping.containsKey(port)) {
                        throw new IllegalArgumentException("一个公网端口只能映射一个后端信息，不能重复: " + port);
                    }

                    inetPortLanInfoMapping.put(port, mapping.getLan());
                }*/

                this.clientInetPortMapping = clientInetPortMapping;
                this.inetPortLanInfoMapping = inetPortLanInfoMapping;
            }

        }
    }



    /**
     * 解析配置文件
     */
    public void update(String proxyMappingConfigJson) {
        notifyconfigChangedListeners();
    }

    /**
     * 配置更新通知
     */
    private void notifyconfigChangedListeners() {
        List<ConfigChangedListener> changedListeners = new ArrayList<ConfigChangedListener>(configChangedListeners);
        for (ConfigChangedListener changedListener : changedListeners) {
            changedListener.onChanged();
        }
    }

    /**
     * 添加配置变化监听器
     *
     * @param configChangedListener
     */
    public void addConfigChangedListener(ConfigChangedListener configChangedListener) {
        configChangedListeners.add(configChangedListener);
    }

    /**
     * 移除配置变化监听器
     *
     * @param configChangedListener
     */
    public void removeConfigChangedListener(ConfigChangedListener configChangedListener) {
        configChangedListeners.remove(configChangedListener);
    }

    /**
     * 获取代理客户端对应的代理服务器端口
     *
     * @param clientKey
     * @return
     */
   /* public List<Integer> getClientInetPorts(String clientKey) {
        return clientInetPortMapping.get(clientKey);
    }*/

    /**
     * 获取所有的clientKey
     *
     * @return
     */
    public Set<String> getClientKeySet() {
        return clientInetPortMapping.keySet();
    }

    /**
     * 根据代理服务器端口获取后端服务器代理信息
     *
     * @param port
     * @return
     */
    public String getLanInfo(Integer port) {
        return inetPortLanInfoMapping.get(port);
    }

    /**
     * 返回需要绑定在代理服务器的端口（用于用户请求）
     *
     * @return
     */
    public List<Integer> getUserPorts() {
        List<Integer> ports = new ArrayList<Integer>();
        Iterator<Integer> ite = inetPortLanInfoMapping.keySet().iterator();
        while (ite.hasNext()) {
            ports.add(ite.next());
        }

        return ports;
    }

    public static ProxyConfig getInstance() {
        return instance;
    }

    /**
     * 代理客户端
     *
     * @author fengfei
     *
     */
    public static class Client implements Serializable {

        private static final long serialVersionUID = 1L;

        /** 客户端备注名称 */
        private String name;

        /** 代理客户端唯一标识key */
        private String clientKey;

        /** 代理客户端与其后面的真实服务器映射关系 */
        private List<ClientProxyMapping> proxyMappings;

        private int status;

        public String getClientKey() {
            return clientKey;
        }

        public void setClientKey(String clientKey) {
            this.clientKey = clientKey;
        }

        public List<ClientProxyMapping> getProxyMappings() {
            return proxyMappings;
        }

        public void setProxyMappings(List<ClientProxyMapping> proxyMappings) {
            this.proxyMappings = proxyMappings;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

    /**
     * 代理客户端与其后面真实服务器映射关系
     *
     * @author fengfei
     *
     */
    public static class ClientProxyMapping {

        /** 代理服务器端口 */
        private Integer inetPort;

        /** 需要代理的网络信息（代理客户端能够访问），格式 192.168.1.99:80 (必须带端口) */
        private String lan;

        /** 备注名称 */
        private String name;

        public Integer getInetPort() {
            return inetPort;
        }

        public void setInetPort(Integer inetPort) {
            this.inetPort = inetPort;
        }

        public String getLan() {
            return lan;
        }

        public void setLan(String lan) {
            this.lan = lan;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * 配置更新回调
     *
     * @author fengfei
     *
     */
    public static interface ConfigChangedListener {

        void onChanged();
    }
}
