package org.jeecg.modules.vcapi.entity.req;

import cn.hutool.crypto.asymmetric.Sign;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 回调请求DTO
 * @author: Mr.Luke
 * @create: 2019-10-16 11:26
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("回调请求DTO")
public class CallBackReqDto {

    private String userId;
    private String bizType ;
    private String orderNo ;
    private String accountVal ;
    private String orderStatus ;
    private String time ;
    private String sign ;
}
