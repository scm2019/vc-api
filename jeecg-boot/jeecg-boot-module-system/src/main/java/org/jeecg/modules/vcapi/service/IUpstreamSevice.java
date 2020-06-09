package org.jeecg.modules.vcapi.service;

import org.jeecg.modules.shiro.vo.ResponseBean;
import org.springframework.stereotype.Service;
import java.util.Map;

public interface IUpstreamSevice {
    /*根据业务类型查询佳诺产品*/
    ResponseBean getProductByBizType(String bizType);
    /*查询福禄所有产品*/
    ResponseBean getProductByBizType();
}
