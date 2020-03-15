package cn.myframe.zookeeper.service;

/**
 * 
 * 获取可重入共享锁后的处理方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年7月3日 下午2:39:34
 * @param <T>
 */
public interface SRLockDealCallback<T> {
	
	/**
	 * 获取可重入共享锁后的处理方法
	 * @return
	 */
	public T deal();

}
