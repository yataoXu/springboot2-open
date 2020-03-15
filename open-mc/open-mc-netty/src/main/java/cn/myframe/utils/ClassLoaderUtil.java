package cn.myframe.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import cn.myframe.controller.BaseActionController;
import cn.myframe.mvc.annotation.ActionMapping;
import cn.myframe.mvc.annotation.ClearAction;
import cn.myframe.mvc.annotation.ResponseType;
import cn.myframe.netty.wrap.Action;

/**
 * 类加载器工具类
 * 
 * @author dingnate
 */
public class ClassLoaderUtil {
	private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderUtil.class);
	private static final String SUN_MISC_LAUNCHER$_EXT_CLASS_LOADER = "sun.misc.Launcher$ExtClassLoader";

	ClassLoaderUtil() {
	}

	private static Collection<URL> getClassPathUrls() {
		ArrayList<URL> list = new ArrayList<>();
		findUrls(list, getClassLoader());
		return list;
	}

	public static java.lang.ClassLoader getClassLoader() {
		java.lang.ClassLoader ret = Thread.currentThread().getContextClassLoader();
		return ret != null ? ret : ClassLoaderUtil.class.getClassLoader();
	}

	private static void findUrls(Collection<URL> classPathUrls, java.lang.ClassLoader classLoader) {
		if (classLoader instanceof URLClassLoader) {
			classPathUrls.addAll(Arrays.asList(((URLClassLoader) classLoader).getURLs()));
			java.lang.ClassLoader parent = classLoader.getParent();
			if (parent == null || parent.getClass().getName().equals(SUN_MISC_LAUNCHER$_EXT_CLASS_LOADER)) {
				return;
			}
			findUrls(classPathUrls, parent);
		}
	}

	public static void scanClass(){
		for (URL classPathUrl : getClassPathUrls()) {
			String path = classPathUrl.getFile();
			if(path.indexOf("myframe")>0){
				System.out.println(path);
			}

			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			if (path.endsWith(".jar")) {
				scanClassInJar(path);
			} else {
				System.out.println("................");
			}
			//System.out.println(path);
		}
	}

	private static void scanClassInJar(String filePath){
		JarFile jarFile = null;
		try {
			filePath = URLDecoder.decode(filePath, "UTF-8");
			jarFile = new JarFile(filePath);
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				String entryName = jarEntry.getName();
				if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
					String className = entryName.replace("/", ".").substring(0, entryName.length() - 6);
					//System.out.println(className);
					add(Class.forName(className, false, getClassLoader()));
				}
			}
		}catch (Exception e){

		}
	}



	public static void add(Class<?>... controllerClasses) {
		for (Class<?> controllerClass : controllerClasses) {
			if (!controllerClass.isAnnotationPresent(ActionMapping.class)) {
				continue;
			}
			String controllerKey = getControllerKey(controllerClass);
			System.out.println(controllerKey);
			String actionKey;
			String[] argNames = null;
			String httpMethod = "";

		}

	}

	/**
	 * 获取ActionMapping的actionKey
	 * @param controllerClass
	 * @return
	 */
	private static String getControllerKey(Class<?> controllerClass) {
		String controllerKey = null;
		ActionMapping controllerAnn = controllerClass.getAnnotation(ActionMapping.class);
		if (controllerAnn != null) {
			controllerKey = controllerAnn.actionKey();
			if (!controllerKey.startsWith("/")) {
				controllerKey = "/" + controllerKey;
			}
		}
		return controllerKey;
	}

	/**
	 * 扫描指包下的所有类
	 * @param packageNames   如果想扫描cn.myframe包下的类，输入cn/myframe
	 * @return
	 */
	private static List<String> scanPackage(String ...packageNames){
		List<String> list = new ArrayList<String>();
		String className = null;
		try{
			for(String packageName : packageNames){
				ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
				Resource[] resources = resourcePatternResolver.getResources("classpath*:"+packageName+"/**/*.class");
				if(resources != null){
					for(Resource resource : resources){
						className =  resource.getURL().getPath();
						int lastIndexOf = className.lastIndexOf(packageName);
						className = className.substring(lastIndexOf, className.length());
						className = className.replace("/", ".").replace(".class", "");
						list.add(className);
					}
				}
			}
		}catch(Exception e){
			LOG.error(e.getMessage());
		}
		return list;
	}

	public static Map<String, Action> buildActionMapping(String ... packageName ){
		 Map<String, Action> actionMapping = new HashMap<String, Action>();
		try{
			List<String> packageList = scanPackage(packageName);
			packageList.forEach(className -> {
				try {
					Class<?> clz  = Class.forName(className);
					if (!clz.isAnnotationPresent(ActionMapping.class)) {
						return;
					}
					if (!clz.isAnnotationPresent(Component.class)) {
						return;
					}
					Object bean = SpringContextUtils.applicationContext.getBean(clz);
					String controllerKey = getControllerKey(clz);
					Method[] declaredMethods = clz.getDeclaredMethods();
					for(Method method : declaredMethods){
						if (!Modifier.isPublic(method.getModifiers()) || method.isAnnotationPresent(ClearAction.class)) {
							continue;
						}
						String actionKey;
						String[] argNames = null;
						String httpMethod = "";
						ActionMapping annotation = method.getAnnotation(ActionMapping.class);
						if (annotation != null) {
							actionKey = StringUtils.isNotEmpty(annotation.actionKey())?annotation.actionKey():method.getName();
							actionKey = controllerKey + "/" + actionKey;
							argNames = null;
						}else{
							actionKey = controllerKey + "/" + method.getName();
						}
						actionMapping.put(actionKey,new Action(httpMethod, clz, method, argNames,bean));
					}
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}

			});
		}catch(Exception e){
			LOG.error(e.getMessage());
		}

		return actionMapping;
	}

	/**
	 * 获取所有实现BaseActionController接口类中,有ActionMapping注解的方法
	 * @return
	 */
	public static Map<String, Action> buildActionMapping(){
		Map<String, Action> actionMapping = new HashMap<String, Action>();
		List<Class<?>> list = ClassReader.getAllClassByInterface(BaseActionController.class);
		if(list != null && list.size() > 0){
			list.forEach(clz ->{
				String reponseType = "";
				try{
					if (!clz.isAnnotationPresent(ActionMapping.class)) {
						return;
					}
					if (!clz.isAnnotationPresent(Component.class)) {
						return;
					}
					if(clz.isAnnotationPresent(ResponseType.class)){
						ResponseType responseTypeAnn = clz.getAnnotation(ResponseType.class);
						reponseType = responseTypeAnn.value();
					}
					Object bean = SpringContextUtils.applicationContext.getBean(clz);
					String controllerKey = getControllerKey(clz);
					Method[] declaredMethods = clz.getDeclaredMethods();
					for(Method method : declaredMethods) {
						if (!Modifier.isPublic(method.getModifiers()) || method.isAnnotationPresent(ClearAction.class)) {
							continue;
						}
						String actionKey;
						String[] argNames = null;
						String httpMethod = "";
						ActionMapping annotation = method.getAnnotation(ActionMapping.class);
						ResponseType responseTypeAnn = method.getAnnotation(ResponseType.class);
						if(responseTypeAnn != null){
							reponseType = responseTypeAnn.value();
						}
						if (annotation != null) {
							actionKey = StringUtils.isNotEmpty(annotation.actionKey()) ? annotation.actionKey() : method.getName();
							actionKey = controllerKey + "/" + actionKey;
							argNames = null;
						} else {
							actionKey = controllerKey + "/" + method.getName();
						}
						actionMapping.put(actionKey, new Action(httpMethod, clz, method, argNames, bean, reponseType));
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			});
		}
		return actionMapping;
	}

	public static void main(String[] args){
		//scanClass();
		buildActionMapping();
		//System.out.println(JSON.toJSONString(getClassPathUrls()));
	}
}
