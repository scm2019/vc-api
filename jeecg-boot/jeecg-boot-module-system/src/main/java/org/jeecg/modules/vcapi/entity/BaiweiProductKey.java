package org.jeecg.modules.vcapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
/**
*@Description: 柏维公司产品密钥表
*@Author: OuYang ZhiQiang
*@date: 2020-06-08
**/
@Data
@Builder
@TableName("baiwei_product")
public class BaiweiProductKey {
    private static final long serialVersionUID = 1L;
    /**主键*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    @Excel(name = "密钥编号", width = 15)
    private String productkeyId;
    @Excel(name = "产品编号",width = 15)
    private String productId;
    @Excel(name = "产品密钥", width = 15)
    private String productKey;
    @Excel(name = "密钥状态", width = 15)
    private String productkeyState;

}
