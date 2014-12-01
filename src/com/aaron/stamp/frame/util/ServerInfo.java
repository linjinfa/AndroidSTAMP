package com.aaron.stamp.frame.util;

/**
 * 服务相关信息
 * 
 * @author linjinfa 331710168@qq.com
 * @date 2014年5月26日
 */
public final class ServerInfo {

	private ServerInfo() {
		super();
	}

	/**
	 * 版本号
	 */
	private static final String API_VERSION_NO = "v1";
	/**
	 * 域名
	 */
	private static final String DOMAIN = "tibet.zou.cn";
	/**
	 * 服务地址
	 */
	public static final String SERVER_URL = "http://" + DOMAIN + "/";
	
}


