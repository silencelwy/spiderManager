package com.ecmoho.jpush.api.report;


import com.ecmoho.jpush.api.common.ClientConfig;
import com.ecmoho.jpush.api.common.ServiceHelper;
import com.ecmoho.jpush.api.common.TimeUnit;
import com.ecmoho.jpush.api.common.connection.HttpProxy;
import com.ecmoho.jpush.api.common.connection.NativeHttpClient;
import com.ecmoho.jpush.api.common.resp.APIConnectionException;
import com.ecmoho.jpush.api.common.resp.APIRequestException;
import com.ecmoho.jpush.api.common.resp.BaseResult;
import com.ecmoho.jpush.api.common.resp.ResponseWrapper;
import com.ecmoho.jpush.api.utils.StringUtils;

import java.net.URLEncoder;
import java.util.regex.Pattern;

public class ReportClient {    

    private final NativeHttpClient _httpClient;
    private String _hostName;
    private String _receivePath;
    private String _userPath;
    private String _messagePath;

    public ReportClient(String masterSecret, String appKey) {
        this(masterSecret, appKey, null, ClientConfig.getInstance());
    }

    /**
     * This will be removed in the future. Please use ClientConfig{@link com.ecmoho.jpush.api.common.ClientConfig#setMaxRetryTimes} instead of this constructor.
     *
     */
    @Deprecated
	public ReportClient(String masterSecret, String appKey, int maxRetryTimes) {
	    this(masterSecret, appKey, maxRetryTimes, null);
	}

    /**
     * This will be removed in the future. Please use ClientConfig{@link com.ecmoho.jpush.api.common.ClientConfig#setMaxRetryTimes} instead of this constructor.
     *
     */
    @Deprecated
	public ReportClient(String masterSecret, String appKey, int maxRetryTimes, HttpProxy proxy) {
        ServiceHelper.checkBasic(appKey, masterSecret);

        ClientConfig conf = ClientConfig.getInstance();
        conf.setMaxRetryTimes(maxRetryTimes);

        _hostName = (String) conf.get(ClientConfig.REPORT_HOST_NAME);
        _receivePath = (String) conf.get(ClientConfig.REPORT_RECEIVE_PATH);
        _userPath = (String) conf.get(ClientConfig.REPORT_USER_PATH);
        _messagePath = (String) conf.get(ClientConfig.REPORT_MESSAGE_PATH);

        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        _httpClient = new NativeHttpClient(authCode, proxy, conf);
	}

    public ReportClient(String masterSecret, String appKey, HttpProxy proxy, ClientConfig conf) {
        ServiceHelper.checkBasic(appKey, masterSecret);

        _hostName = (String) conf.get(ClientConfig.REPORT_HOST_NAME);
        _receivePath = (String) conf.get(ClientConfig.REPORT_RECEIVE_PATH);
        _userPath = (String) conf.get(ClientConfig.REPORT_USER_PATH);
        _messagePath = (String) conf.get(ClientConfig.REPORT_MESSAGE_PATH);

        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        _httpClient = new NativeHttpClient(authCode, proxy, conf);
    }
	
	
    public ReceivedsResult getReceiveds(String[] msgIdArray)
            throws APIConnectionException, APIRequestException {
        return getReceiveds(StringUtils.arrayToString(msgIdArray));
    }
	
    public ReceivedsResult getReceiveds(String msgIds) 
            throws APIConnectionException, APIRequestException {
        checkMsgids(msgIds);
        
        String url = _hostName + _receivePath + "?msg_ids=" + msgIds;
        ResponseWrapper response = _httpClient.sendGet(url);
        
        return ReceivedsResult.fromResponse(response);
	}
	
    public MessagesResult getMessages(String msgIds)
            throws APIConnectionException, APIRequestException {
        checkMsgids(msgIds);
        
        String url = _hostName + _messagePath + "?msg_ids=" + msgIds;
        ResponseWrapper response = _httpClient.sendGet(url);
        
        return MessagesResult.fromResponse(response);
    }
    
    public UsersResult getUsers(TimeUnit timeUnit, String start, int duration)
            throws APIConnectionException, APIRequestException {        
        String startEncoded = null;
        try {
            startEncoded = URLEncoder.encode(start, "utf-8");
        } catch (Exception e) {
        }
        
        String url = _hostName + _userPath
                + "?time_unit=" + timeUnit.toString()
                + "&start=" + startEncoded + "&duration=" + duration;
        ResponseWrapper response = _httpClient.sendGet(url);
        
        return BaseResult.fromResponse(response, UsersResult.class);
    }
    
    
    private final static Pattern MSGID_PATTERNS = Pattern.compile("[^0-9, ]");

    public static void checkMsgids(String msgIds) {
        if (StringUtils.isTrimedEmpty(msgIds)) {
            throw new IllegalArgumentException("msgIds param is required.");
        }
        
        if (MSGID_PATTERNS.matcher(msgIds).find()) {
            throw new IllegalArgumentException("msgIds param format is incorrect. "
                    + "It should be msg_id (number) which response from JPush Push API. "
                    + "If there are many, use ',' as interval. ");
        }
        
        msgIds = msgIds.trim();
        if (msgIds.endsWith(",")) {
            msgIds = msgIds.substring(0, msgIds.length() - 1);
        }
        
        String[] splits = msgIds.split(",");
        try {
            for (String s : splits) {
                s = s.trim();
                if (!StringUtils.isEmpty(s)) {
                    Long.parseLong(s);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Every msg_id should be valid Long number which splits by ','");
        }
    }

}


