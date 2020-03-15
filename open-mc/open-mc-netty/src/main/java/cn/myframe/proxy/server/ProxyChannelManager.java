package cn.myframe.proxy.server;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.myframe.proxy.config.ProxyConfig;
import cn.myframe.proxy.protocol.Constants;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 代理服务连接管理（代理客户端连接+用户请求连接）
 *
 * @author fengfei
 *
 */
public class ProxyChannelManager {

    private static Logger logger = LoggerFactory.getLogger(ProxyChannelManager.class);

    private static final AttributeKey<Map<String, Channel>> USER_CHANNELS = AttributeKey.newInstance("user_channels");

    private static final AttributeKey<String> REQUEST_LAN_INFO = AttributeKey.newInstance("request_lan_info");

    private static final AttributeKey<List<Integer>> CHANNEL_PORT = AttributeKey.newInstance("channel_port");

    private static final AttributeKey<String> CHANNEL_CLIENT_KEY = AttributeKey.newInstance("channel_client_key");

    private static Map<Integer, Channel>  portCmdChannelMapping = new ConcurrentHashMap<Integer, Channel>();

    private static Map<String, Channel> cmdChannels = new ConcurrentHashMap<String, Channel>();

   

    /**
     * 增加代理服务器端口与代理控制客户端连接的映射关系
     *
     * @param ports
     * @param channel
     */
    public static void addCmdChannel(List<Integer> ports, String clientKey, Channel channel) {

        if (ports == null) {
            throw new IllegalArgumentException("port can not be null");
        }

        // 客户端（proxy-client）相对较少，这里同步的比较重
        // 保证服务器对外端口与客户端到服务器的连接关系在临界情况时调用removeChannel(Channel channel)时不出问题
        synchronized (portCmdChannelMapping) {
            for (int port : ports) {
                portCmdChannelMapping.put(port, channel);
            }
        }

        channel.attr(CHANNEL_PORT).set(ports);
        channel.attr(CHANNEL_CLIENT_KEY).set(clientKey);
        channel.attr(USER_CHANNELS).set(new ConcurrentHashMap<String, Channel>());
        cmdChannels.put(clientKey, channel);
    }

    /**
     * 代理客户端连接断开后清除关系
     *
     * @param channel
     */
    public static void removeCmdChannel(Channel channel) {
        logger.warn("channel closed, clear user channels, {}", channel);
        if (channel.attr(CHANNEL_PORT).get() == null) {
            return;
        }

        String clientKey = channel.attr(CHANNEL_CLIENT_KEY).get();
        Channel channel0 = cmdChannels.remove(clientKey);
        if (channel != channel0) {
            cmdChannels.put(clientKey, channel);
        }

        List<Integer> ports = channel.attr(CHANNEL_PORT).get();
        for (int port : ports) {
            Channel proxyChannel = portCmdChannelMapping.remove(port);
            if (proxyChannel == null) {
                continue;
            }

            // 在执行断连之前新的连接已经连上来了
            if (proxyChannel != channel) {
                portCmdChannelMapping.put(port, proxyChannel);
            }
        }

        if (channel.isActive()) {
            logger.info("disconnect proxy channel {}", channel);
            channel.close();
        }

        Map<String, Channel> userChannels = getUserChannels(channel);
        Iterator<String> ite = userChannels.keySet().iterator();
        while (ite.hasNext()) {
            Channel userChannel = userChannels.get(ite.next());
            if (userChannel.isActive()) {
                userChannel.close();
                logger.info("disconnect user channel {}", userChannel);
            }
        }
    }

    public static Channel getCmdChannel(Integer port) {
        return portCmdChannelMapping.get(port);
    }

    public static Channel getCmdChannel(String clientKey) {
        return cmdChannels.get(clientKey);
    }

    /**
     * 增加用户连接与代理客户端连接关系
     *
     * @param cmdChannel
     * @param userId
     * @param userChannel
     */
    public static void addUserChannelToCmdChannel(Channel cmdChannel, String userId, Channel userChannel) {
        InetSocketAddress sa = (InetSocketAddress) userChannel.localAddress();
        String lanInfo = ProxyConfig.getInstance().getLanInfo(sa.getPort());
        userChannel.attr(Constants.USER_ID).set(userId);
        userChannel.attr(REQUEST_LAN_INFO).set(lanInfo);
        cmdChannel.attr(USER_CHANNELS).get().put(userId, userChannel);
    }

    /**
     * 删除用户连接与代理客户端连接关系
     *
     * @param cmdChannel
     * @param userId
     * @return
     */
    public static Channel removeUserChannelFromCmdChannel(Channel cmdChannel, String userId) {
        if (cmdChannel.attr(USER_CHANNELS).get() == null) {
            return null;
        }

        synchronized (cmdChannel) {
            return cmdChannel.attr(USER_CHANNELS).get().remove(userId);
        }
    }

    /**
     * 根据代理客户端连接与用户编号获取用户连接
     *
     * @param cmdChannel
     * @param userId
     * @return
     */
    public static Channel getUserChannel(Channel cmdChannel, String userId) {
        return cmdChannel.attr(USER_CHANNELS).get().get(userId);
    }

    /**
     * 获取用户编号
     *
     * @param userChannel
     * @return
     */
    public static String getUserChannelUserId(Channel userChannel) {
        return userChannel.attr(Constants.USER_ID).get();
    }

    /**
     * 获取用户请求的内网IP端口信息
     *
     * @param userChannel
     * @return
     */
    public static String getUserChannelRequestLanInfo(Channel userChannel) {
        return userChannel.attr(REQUEST_LAN_INFO).get();
    }

    /**
     * 获取代理控制客户端连接绑定的所有用户连接
     *
     * @param cmdChannel
     * @return
     */
    public static Map<String, Channel> getUserChannels(Channel cmdChannel) {
        return cmdChannel.attr(USER_CHANNELS).get();
    }

}
