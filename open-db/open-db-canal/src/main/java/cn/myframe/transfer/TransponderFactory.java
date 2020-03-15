package cn.myframe.transfer;

import cn.myframe.annotation.CanalEventListener;
import cn.myframe.core.ListenerPoint;
import cn.myframe.properties.CanalProperties;
import com.alibaba.otter.canal.client.CanalConnector;


import java.util.List;
import java.util.Map;

/**
 * 信息转换工厂类接口层
 *
 */
@FunctionalInterface
public interface TransponderFactory {
	
	/**
	 * @param connector        canal 连接工具
	 * @param config           canal 链接信息
	 * @param listeners 实现接口的监听器
	 * @param annoListeners    注解监听拦截
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 14:43
	 * @CopyRight 万物皆导
	 */
	MessageTransponder newTransponder(CanalConnector connector, Map.Entry<String, CanalProperties.Instance> config, List<CanalEventListener> listeners, List<ListenerPoint> annoListeners);
}
