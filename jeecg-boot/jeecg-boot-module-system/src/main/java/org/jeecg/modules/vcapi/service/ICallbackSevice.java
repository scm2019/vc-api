package org.jeecg.modules.vcapi.service;

import org.jeecg.modules.vcapi.entity.VcOrderRecharge;

public interface ICallbackSevice {
    /*福禄平台订单回调*/
    VcOrderRecharge orderCallback(String str);
}
