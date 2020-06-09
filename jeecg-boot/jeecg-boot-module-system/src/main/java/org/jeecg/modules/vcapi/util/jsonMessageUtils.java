package org.jeecg.modules.vcapi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求福禄平台，组建报文
 */
public class jsonMessageUtils {
    public static final String APP_KEY = "apQdrNaCOb2S34KCAMbD/uvCHJ7LCoIcAZGsuRTFghVlz8ZKSgJjQ7KtNl4cqMbc";
    public static final String SYS_SECRET = "d52f886284524ceb81abd89973b7877f";
    /**
     */
    public static Map<String, Object> jsonStr(String queryCondition,String method) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("app_key",APP_KEY);
        map.put("method", method);
        map.put("timestamp", DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        map.put("version", "2.0");
        map.put("format", "json");
        map.put("charset", "utf-8");
        map.put("sign_type", "md5");
        map.put("app_auth_token", "");
        map.put("biz_content", queryCondition);
        return map;
    }
}
