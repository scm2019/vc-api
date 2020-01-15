package org.jeecg.modules.vcapi.util;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.MD5Util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * @description: 签名计算工具类
 * @author: Mr.Luke
 * @create: 2019-10-15 10:03
 * @Version V1.0
 */
@Slf4j
public class SignUtil {

    public static String createSign(SortedMap<Object,Object> parameters, String key){
        StringBuffer sbkey = new StringBuffer();
        Set es = parameters.entrySet();  //所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if(null != v && !"".equals(v)) {
                sbkey.append(k + v );
            }
        }
        //System.out.println("字符串:"+sb.toString());
        sbkey=sbkey.append(key);
        log.debug("字符串:"+sbkey.toString());

        String sign = null;
        try {
            sign = md5Encode(sbkey.toString());
        } catch (Exception e) {
            return "error";
        }
        log.debug("MD5加密值:"+sign);
        return sign;
    }

    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}
