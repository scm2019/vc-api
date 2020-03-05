package org.jeecg.modules.vcapi.service;

import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.req.CallBackReqDto;
import org.jeecg.modules.vcapi.entity.req.RechargeReqDto;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: 充值APi的业务接口
 * @author: Mr.Luke
 * @create: 2019-10-16 09:50
 * @Version V1.0
 */
public interface VcRechargeService {


     /**
      * @Author: Mr.Luke
      * @Description: 根据业务类型查询商品
      * @Date: 10:00 2019/10/16
      * @Param: [bizType]
      * @return: org.jeecg.modules.shiro.vo.ResponseBean
      */
     ResponseBean getProductByBizType(String bizType);

     /**
      * @Author: Mr.Luke
      * @Description: 查询用户余额
      * @Date: 10:00 2019/10/16
      * @Param: [bizType]
      * @return: org.jeecg.modules.shiro.vo.ResponseBean
      */
     ResponseBean getUserBalance(String bizType);

     /**
      * @Author: Mr.Luke
      * @Description: 充值
      * @Date: 10:01 2019/10/16
      * @Param: [rechargeReqDto]
      * @return: org.jeecg.modules.shiro.vo.ResponseBean
      */
     ResponseBean recharge(@RequestBody RechargeReqDto rechargeReqDto);

     ResponseBean queryOrder(String orderNo,String bizType);

     String callBack(CallBackReqDto callBackReqDto);

     /**
      * @Author: Mr.Luke
      * @Description: 将回调失败的数据重新回调
      * @Date: 14:17 2020/3/5
      * @Param: [callBackReqDto]
      * @return: java.lang.String
      */
     String againCallBack();

}
