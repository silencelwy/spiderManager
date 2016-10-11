package com.ecmoho.jpush.client;

import com.ecmoho.jpush.api.JPushClient;
import com.ecmoho.jpush.api.common.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientExample extends JPushCommon{
	protected static final Logger LOG = LoggerFactory.getLogger(ClientExample.class);

	public static void main(String[] args) {
//		testDefaultClient();
//		testCustomClient();
//		testCustomPushClient();
	}

	public static void testDefaultClient() {
		JPushClient client = new JPushClient(masterSecret, appKey);

	//	JPushClient client1 = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
	}

	public static void testCustomClient() {
		ClientConfig config = ClientConfig.getInstance();
		config.setMaxRetryTimes(5);
		config.setConnectionTimeout(10 * 1000);	// 10 seconds
		config.setSSLVersion("TLSv1.1");		// JPush server supports SSLv3, TLSv1, TLSv1.1, TLSv1.2

		JPushClient jPushClient = new JPushClient(masterSecret, appKey, null, config);
	}

	public static void testCustomPushClient() {
		ClientConfig config = ClientConfig.getInstance();

		config.setApnsProduction(false); 	// development env
		config.setTimeToLive(60 * 60 * 24); // one day

	//	config.setGlobalPushSetting(false, 60 * 60 * 24); // development env, one day

		JPushClient jPushClient = new JPushClient(masterSecret, appKey, null, config); 	// JPush client

	//	PushClient pushClient = new PushClient(masterSecret, appKey, null, config); 	// push client only

	}
	
}


