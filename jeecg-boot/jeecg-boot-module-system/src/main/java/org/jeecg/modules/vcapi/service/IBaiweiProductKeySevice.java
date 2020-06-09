package org.jeecg.modules.vcapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.vcapi.entity.BaiweiProductKey;

import java.util.List;

public interface IBaiweiProductKeySevice extends IService<BaiweiProductKey> {

    List<BaiweiProductKey> getBaiweiProductKeyByid(String productid);

    List<BaiweiProductKey> getBaiweiProductKeyByidlimit(String productid,int sl);

    void upBaiweiProductKeyByid(String realname,String key);
}
