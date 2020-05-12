package org.jeecg.modules.vcapi.vo;

import com.alibaba.druid.util.DaemonThreadFactory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.modules.vcapi.entity.VcVoucher;

import java.util.Date;

@Data
public class VcVoucherVo extends VcVoucher {

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date expireStartDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date expireEndDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date activeStartDate;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date activeEndDate;
}
