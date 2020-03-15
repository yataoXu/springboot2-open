package cn.myframe.properties;

import java.util.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


import lombok.Data;

@ConfigurationProperties(prefix="netty.proxy-server")
@Data
@Validated
public class NettyProxyServerProperties {
	
	private List<ClientServer> clientServer;

	private Integer port;

	@Data
	public static class  ClientServer{
		Integer port;
		String keys;
		String bind;
	}

	/** 需要代理的端口 */
	private static List<Integer> portList =  null;
	/** 代理真实服务器 */
	private static Map<Integer, String> inetPortLanInfoMapping = null;
	/** 代理服务器为各个代理客户端（key）开启对应的端口列表（value） */
	private static  Map<String, List<Integer>> clientInetPortMapping = null;

	public List<Integer> ports(){
		if(portList == null){
			 portList =  new ArrayList<>();
			if(clientServer != null){
				clientServer.forEach(server -> portList.add(server.getPort()));
			}
		}
		return portList;
	}

	public  Map<Integer, String> portLanInfoMapping(){
		if(inetPortLanInfoMapping == null){
			inetPortLanInfoMapping = new HashMap<Integer, String>();
			if(clientServer != null){
				clientServer.forEach(server ->{
					  inetPortLanInfoMapping.put(server.getPort(),server.getBind());
					}
				);
			}
		}
		return  inetPortLanInfoMapping;
	}

	/**
	 * 获取代理端密钥（keys）对应的所有端口
	 * @return
	 */
	public Map<String, List<Integer>> getClientInetPortMapping(){
		if(clientInetPortMapping == null){
			clientInetPortMapping = new HashMap<String, List<Integer>>();
			if(clientServer != null){
				clientServer.forEach(
						server ->{
							if(!clientInetPortMapping.containsKey(server.getKeys())){
								List<Integer> ports = new ArrayList<>();
								for(ClientServer cServer: clientServer){
									if(server.getKeys().equals(cServer.getKeys())){
										ports.add(cServer.getPort());
									}
								}
								clientInetPortMapping.put(server.getKeys(),ports);
							}

						}
				);
			}
		}
		return  clientInetPortMapping;
	}



}




