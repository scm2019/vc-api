package org.jeecg.modules.vcapi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class InputParamSign {
    public static void main(String[] args) {
        String sysSecret = "0a091b3aa4324435aab703142518a8f7";
        // String sign = Sign(sysSecret);
        //System.out.println(sign);
    }

    public static String Sign(Map<String, Object> mapJson ,String sysSecret){
        Map<String, String> map = new HashMap<String, String>();
     /*   map.put("app_key","i4esv1l+76l/7NQCL3QudG90Fq+YgVfFGJAWgT+7qO1Bm9o/adG/1iwO2qXsAXNB");
        map.put("method", "fulu.user.info.get");
        map.put("timestamp", "2019-07-24 09:45:11");
        map.put("version", "2.0");
        map.put("format", "json");
        map.put("charset", "utf-8");
        map.put("sign_type", "md5");
        map.put("app_auth_token", "");
        map.put("biz_content", "{}");*/

        String json = JSON.toJSONString(mapJson);//map转String
        JSONObject jsonObject = JSON.parseObject(json);//String转json
        char[] s =JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue).toCharArray();
        Arrays.sort(s);
        String outputSignOriginalStr = new String(s) + sysSecret;
        String sign = MD5Utils.MD5(outputSignOriginalStr);
        return sign;
    }
}
