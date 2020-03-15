package cn.myframe.transfer;


import cn.myframe.annotation.CanalEventListener;
import cn.myframe.annotation.ListenPoint;
import cn.myframe.core.CanalMsg;
import cn.myframe.core.ListenerPoint;
import cn.myframe.properties.CanalProperties;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 默认转信息换器
 */
public class DefaultMessageTransponder extends AbstractMessageTransponder {
	
	
	public DefaultMessageTransponder(CanalConnector connector, Map.Entry<String, CanalProperties.Instance> config,
									 List<CanalEventListener> listeners, List<ListenerPoint> annoListeners) {
		super(connector, config, listeners, annoListeners);
	}
	
	
	/**
	 * 断言注解方式的监听过滤规则
	 *
	 * @param destination 指定
	 * @param schemaName  数据库实例
	 * @param tableName   表名称
	 * @param eventType   事件类型
	 * @return
	 */
	protected Predicate<Map.Entry<Method, ListenPoint>> getAnnotationFilter(String destination, String schemaName, String tableName, CanalEntry.EventType eventType) {
		//看看指令是否正确
		Predicate<Map.Entry<Method, ListenPoint>> df = e -> StringUtils.isEmpty(e.getValue().destination())
				|| e.getValue().destination().equals(destination) || destination == null;
		
		//看看数据库实例名是否一样
		Predicate<Map.Entry<Method, ListenPoint>> sf = e -> e.getValue().schema().length == 0
				|| Arrays.stream(e.getValue().schema()).anyMatch(s -> s.equals(schemaName)) || schemaName == null;
		
		//看看表名是否一样
		Predicate<Map.Entry<Method, ListenPoint>> tf = e -> e.getValue().table().length == 0
				|| Arrays.stream(e.getValue().table()).anyMatch(t -> t.equals(tableName)) || tableName == null;
		
		//类型一致？
		Predicate<Map.Entry<Method, ListenPoint>> ef = e -> e.getValue().eventType().length == 0
				|| Arrays.stream(e.getValue().eventType()).anyMatch(ev -> ev == eventType) || eventType == null;
		
		return df.and(sf).and(tf).and(ef);
	}
	
	/**
	 * 获取处理的参数
	 *
	 * @param method    监听的方法
	 * @param canalMsg  事件节点
	 * @param rowChange 詳細參數
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 17:18
	 * @CopyRight 万物皆导
	 */
	protected Object[] getInvokeArgs(Method method, CanalMsg canalMsg, CanalEntry.RowChange rowChange) {
		return Arrays.stream(method.getParameterTypes())
				.map(p -> p == CanalMsg.class ? canalMsg : p == CanalEntry.RowChange.class ? rowChange : null)
				.toArray();
	}
	
	
	/**
	 * 忽略实体类的类型
	 *
	 * @param
	 * @return
	 */
	protected List<CanalEntry.EntryType> getIgnoreEntryTypes() {
		return Arrays.asList(CanalEntry.EntryType.TRANSACTIONBEGIN, CanalEntry.EntryType.TRANSACTIONEND, CanalEntry.EntryType.HEARTBEAT);
	}

	/**
	 * 处理消息
	 */
	@Override
	protected void distributeEvent(Message message) {
		//获取操作实体
		List<CanalEntry.Entry> entries = message.getEntries();
		//遍历实体
		for (CanalEntry.Entry entry : entries) {
			//忽略实体类的类型
			List<CanalEntry.EntryType> ignoreEntryTypes = getIgnoreEntryTypes();
			if (ignoreEntryTypes != null
					&& ignoreEntryTypes.stream().anyMatch(t -> entry.getEntryType() == t)) {
				continue;
			}
			//canal 改变信息
			CanalEntry.RowChange rowChange = null;
			try {
				//获取信息改变
				rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

			} catch (Exception e) {
				/*throw new CanalClientException("错误 ##转换错误 , 数据信息:" + entry.toString(),
						e);*/
			}

			distributeByAnnotation(destination,
					entry.getHeader().getSchemaName(),
					entry.getHeader().getTableName(), rowChange);
			/*distributeByImpl(destination,
					entry.getHeader().getSchemaName(),
					entry.getHeader().getTableName(), rowChange);*/

		}
	}

	/**
	 * 处理注解方式的 canal 监听器
	 *
	 * @param destination canal 指令
	 * @param schemaName  实例名称
	 * @param tableName   表名称
	 * @param rowChange   数据
	 * @return
	 */
	protected void distributeByAnnotation(String destination,
										  String schemaName,
										  String tableName,
										  CanalEntry.RowChange rowChange) {

		//对注解的监听器进行事件委托
		if (!CollectionUtils.isEmpty(annoListeners)) {
			annoListeners.forEach(point -> point
					.getInvokeMap()
					.entrySet()
					.stream()
					.filter(getAnnotationFilter(destination, schemaName, tableName, rowChange.getEventType()))
					.forEach(entry -> {
						Method method = entry.getKey();
						method.setAccessible(true);
						try {
							CanalMsg canalMsg = new CanalMsg();
							canalMsg.setDestination(destination);
							canalMsg.setSchemaName(schemaName);
							canalMsg.setTableName(tableName);

							Object[] args = getInvokeArgs(method, canalMsg, rowChange);
							method.invoke(point.getTarget(), args);
						} catch (Exception e) {
							/*logger.error("{}: 委托 canal 监听器发生错误! 错误类:{}, 方法名:{}",
									Thread.currentThread().getName(),
									point.getTarget().getClass().getName(), method.getName());*/
						}
					}));
		}
	}


	/**
	 * 处理监听信息
	 */
	/*protected void distributeByImpl(String destination,
									String schemaName,
									String tableName,
									CanalEntry.RowChange rowChange) {
		if (listeners != null) {
			for (CanalEventListener listener : listeners) {
				listener.onEvent(destination, schemaName, tableName, rowChange);
			}
		}
	}*/
}
