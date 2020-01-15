package org.jeecg.common.util;

import java.util.UUID;

/**
 * @description: 获取ID的工具类
 * @author: Mr.Luke
 * @create: 2019-10-15 17:21
 * @Version V1.0
 */
public class IDUtils {

    public static String getID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
