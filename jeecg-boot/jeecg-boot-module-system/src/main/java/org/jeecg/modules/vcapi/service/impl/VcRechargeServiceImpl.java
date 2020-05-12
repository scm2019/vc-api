package org.jeecg.modules.vcapi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IDUtils;
import org.jeecg.modules.shiro.vo.ResponseBean;
import org.jeecg.modules.vcapi.entity.VcCustomer;
import org.jeecg.modules.vcapi.entity.VcOrderCallback;
import org.jeecg.modules.vcapi.entity.VcOrderRecharge;
import org.jeecg.modules.vcapi.entity.VcProduct;
import org.jeecg.modules.vcapi.entity.req.CallBackReqDto;
import org.jeecg.modules.vcapi.entity.req.RechargeReqDto;
import org.jeecg.modules.vcapi.enums.*;
import org.jeecg.modules.vcapi.service.*;
import org.jeecg.modules.vcapi.util.SignUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @description:
 * @author: Mr.Luke
 * @create: 2019-10-16 09:52
 * @Version V1.0
 */
@Slf4j
@Service
public class VcRechargeServiceImpl implements VcRechargeService {

    private final RestTemplate restTemplate;
    private final IVcOrderRechargeService vcOrderRechargeService;
    private final IVcOrderCallbackService vcOrderCallbackService;
    private final IVcCustomerService customerService;
    private final IVcProductService productService;

    @Value("${api.apiKey}")
    private String apiKey;
    @Value("${api.userId}")
    private String userId;
    @Value("${api.http.address}")
    private String apiHttpAddress;


    @Autowired
    public VcRechargeServiceImpl(RestTemplate restTemplate, IVcOrderRechargeService vcOrderRechargeService,IVcOrderCallbackService vcOrderCallbackService,
                                 IVcCustomerService vcCustomerService,IVcProductService vcProductService) {
        this.restTemplate = restTemplate;
        this.vcOrderRechargeService = vcOrderRechargeService;
        this.vcOrderCallbackService = vcOrderCallbackService;
        this.customerService = vcCustomerService;
        this.productService = vcProductService;
    }



    @Override
    public ResponseBean getProductByBizType(String bizType){
        if (bizType==null){
            return new ResponseBean(400,"参数不能为空","参数不能为空");
        }
        log.debug("获取："+bizType+",类型商品。");
        JSONObject jsonObject= queryApi(bizType, ApiTypeEnum.QueryProducts.getCode(),null);
        if (jsonObject==null)
        {
            return new ResponseBean(500,"网络访问失败！！！","网络访问失败！！！");
        }
        if (ApiStatusEnum.ERROR.getCode().equals(jsonObject.getString("status"))){
            return new ResponseBean(400,jsonObject.getString("code"),jsonObject.getString("msg"));
        }
        return new ResponseBean(200,jsonObject.getString("msg"),jsonObject.get("Products"));
    }


    @Override
    public ResponseBean getUserBalance(String bizType){
        if (bizType==null){
            return new ResponseBean(400,"参数不能为空","参数不能为空");
        }
        log.debug("获取："+bizType+",类型余额。访问地址:"+apiHttpAddress);
        JSONObject jsonObject= queryApi(bizType, ApiTypeEnum.QueryBalance.getCode(),null);
        if (jsonObject==null)
        {
            return new ResponseBean(500,"网络访问失败！！！","网络访问失败！！！");
        }
        if (ApiStatusEnum.ERROR.getCode().equals(jsonObject.getString("status"))){
            return new ResponseBean(400,jsonObject.getString("code"),jsonObject.getString("msg"));
        }
        return new ResponseBean(200,jsonObject.getString("msg"),jsonObject.getBigDecimal("Balance"));
    }

    @Override
    public ResponseBean recharge(@RequestBody RechargeReqDto rechargeReqDto) {
        VcOrderRecharge vcOrderRecharge = VcOrderRecharge.builder()
                .orderNo(rechargeReqDto.getOrderNo())
                .bizType(rechargeReqDto.getBizType())
                .build();
        List<VcOrderRecharge> vcOrderRechargeList = vcOrderRechargeService.getVcOrderRechargeList(vcOrderRecharge);
        if (vcOrderRechargeList != null&&vcOrderRechargeList.size()>0) {
            log.info("存在订单号相同的数据,id="+vcOrderRechargeList.get(0).getId());
            return new ResponseBean(400, "", "存在订单号相同的数据！！！");
        }
        //查询当前这个用户的加款或者授信是否有值
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        VcCustomer customer = customerService.getCustomerByUserId(sysUser.getId());
        BigDecimal zero = new BigDecimal(0);
        if(null == customer || (customer.getMoney().equals(zero) &&  customer.getQuota().equals(zero))){
            return new ResponseBean(400, "", "余额不足！！！");
        }else{
            VcProduct product = productService.getById(rechargeReqDto.getProductId());
            if(null == product){
                return new ResponseBean(400, "", "指定的订单产品不存在，或者已经下架！！！");
            }else{
                //默认为1，即为不打折
                BigDecimal discount = new BigDecimal(1);
                if(null != customer.getDiscount()){
                    discount = customer.getDiscount().divide(new BigDecimal(10));
                }
                BigDecimal orderPrice = product.getPrice().multiply(new BigDecimal(rechargeReqDto.getBuyNum())).multiply(discount);
                int priceIsEnought = orderPrice.compareTo(customer.getMoney());
                if(priceIsEnought > 0){
                   //订单金额超过加款的话，看看授信是否足够
                    int quotaIsEnought = orderPrice.compareTo(customer.getQuota());
                    if(quotaIsEnought > 0){
                        return new ResponseBean(400, "", "余额不足！！！");
                    }else{
                        customer.setQuota(quotaIsEnought == 0? zero : customer.getQuota().subtract(orderPrice));
                    }
                }else{
                    //说明订单金额等于加款,扣加款
                    customer.setMoney(priceIsEnought == 0 ? zero : customer.getMoney().subtract(orderPrice));
                }
                //修改客户信息表
                customerService.updateById(customer);
            }
        }
        BeanUtils.copyProperties(rechargeReqDto, vcOrderRecharge);
        SortedMap<Object, Object> param = new TreeMap<Object, Object>();
        param.put("OrderNo", rechargeReqDto.getOrderNo());
        param.put("ProductId", rechargeReqDto.getProductId());
        param.put("AccountVal", rechargeReqDto.getAccountVal());
        if (rechargeReqDto.getBuyNum() != null){
            param.put("BuyNum", rechargeReqDto.getBuyNum());
        }
        if (rechargeReqDto.getCustomerIP() != null) {
            param.put("CustomerIP", rechargeReqDto.getCustomerIP());
        }
        if (rechargeReqDto.getExtraData() != null){
            param.put("ExtraData",rechargeReqDto.getExtraData());
        }
        vcOrderRecharge.setId(IDUtils.getID());

        vcOrderRecharge.setUserId(userId);

        vcOrderRecharge.setOrderStatus(OrderStatusEnum.WAIT.getValue());
        Date date=new Date();
        vcOrderRecharge.setCreateTime(date);
        vcOrderRecharge.setUpdateTime(date);
        vcOrderRecharge.setCallback(0);
        log.debug("开始访问，充值API，参数："+param);
//
        //todo 测试代码
        vcOrderRecharge.setRequestCode("OK");
        vcOrderRecharge.setRequestStatus("1");
        vcOrderRecharge.setRequestMsg("成功");
        vcOrderRechargeService.save(vcOrderRecharge);
        return new ResponseBean(200,OrderStatusEnum.SUCCESS.getCode(),OrderStatusEnum.SUCCESS.getContent());

//        JSONObject jsonObject=queryApi(rechargeReqDto.getBizType(),ApiTypeEnum.SubmitOrder.getCode(),param);
//        if (jsonObject==null)
//        {
//            vcOrderRecharge.setOrderStatus(OrderStatusEnum.ERROR.getValue());
//            log.info("访问充值API，网络访问失败！！！");
//            vcOrderRecharge.setRequestStatus("-500");
//            vcOrderRecharge.setRequestMsg("网络连接失败");
//            vcOrderRechargeService.save(vcOrderRecharge);
//            return new ResponseBean(500,"网络访问失败！！！","网络访问失败！！！");
//        }
//        log.debug("访问充值API，成功");
//        vcOrderRecharge.setRequestCode(jsonObject.getString("code"));
//        vcOrderRecharge.setRequestStatus(jsonObject.getString("status"));
//        vcOrderRecharge.setRequestMsg(jsonObject.getString("msg"));
//
//        if (ApiStatusEnum.ERROR.getCode().equals(jsonObject.getString("status"))){
//            log.info("访问充值API，结果失败,失败信息："+jsonObject.getString("msg"));
//            vcOrderRecharge.setOrderStatus(OrderStatusEnum.FAILED.getValue());
//            vcOrderRechargeService.save(vcOrderRecharge);
//            return new ResponseBean(400,jsonObject.getString("code"),jsonObject.getString("msg"));
//        }
//
//
//        String orderStatus=jsonObject.getString("OrderStatus");
//        OrderStatusEnum orderStatusEnum=OrderStatusEnum.getOrderStatusEnumByCode(orderStatus);
//        vcOrderRecharge.setOrderStatus(orderStatusEnum.getValue());
//        log.info("访问充值API，访问成功,订单 状态："+orderStatusEnum.getContent());
//        vcOrderRechargeService.save(vcOrderRecharge);
//        return new ResponseBean(200,orderStatus,orderStatusEnum.getContent());
    }

    @Override
    public ResponseBean rechargeForAdmin(RechargeReqDto rechargeReqDto) {
        VcOrderRecharge vcOrderRecharge = VcOrderRecharge.builder()
                .orderNo(rechargeReqDto.getOrderNo())
                .bizType(rechargeReqDto.getBizType())
                .build();
        List<VcOrderRecharge> vcOrderRechargeList = vcOrderRechargeService.getVcOrderRechargeList(vcOrderRecharge);
        if (vcOrderRechargeList != null&&vcOrderRechargeList.size()>0) {
            log.info("存在订单号相同的数据,id="+vcOrderRechargeList.get(0).getId());
            return new ResponseBean(400, "", "存在订单号相同的数据！！！");
        }

        BeanUtils.copyProperties(rechargeReqDto, vcOrderRecharge);


        SortedMap<Object, Object> param = new TreeMap<Object, Object>();
        param.put("OrderNo", rechargeReqDto.getOrderNo());
        param.put("ProductId", rechargeReqDto.getProductId());
        param.put("AccountVal", rechargeReqDto.getAccountVal());
        if (rechargeReqDto.getBuyNum() != null){
            param.put("BuyNum", rechargeReqDto.getBuyNum());
        }
        if (rechargeReqDto.getCustomerIP() != null) {
            param.put("CustomerIP", rechargeReqDto.getCustomerIP());
        }
        if (rechargeReqDto.getExtraData() != null){
            param.put("ExtraData",rechargeReqDto.getExtraData());
        }
        vcOrderRecharge.setId(IDUtils.getID());

        vcOrderRecharge.setUserId(userId);

        vcOrderRecharge.setOrderStatus(OrderStatusEnum.WAIT.getValue());
        Date date=new Date();
        vcOrderRecharge.setCreateTime(date);
        vcOrderRecharge.setUpdateTime(date);
        vcOrderRecharge.setCallback(0);
        log.debug("开始访问，充值API，参数："+param);
//
        //todo 测试代码
//        vcOrderRecharge.setRequestCode("OK");
//        vcOrderRecharge.setRequestStatus("1");
//        vcOrderRecharge.setRequestMsg("成功");
//        vcOrderRechargeService.save(vcOrderRecharge);
//        return new ResponseBean(200,OrderStatusEnum.SUCCESS.getCode(),OrderStatusEnum.SUCCESS.getContent());

        JSONObject jsonObject=queryApi(rechargeReqDto.getBizType(),ApiTypeEnum.SubmitOrder.getCode(),param);
        if (jsonObject==null)
        {
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.ERROR.getValue());
            log.info("访问充值API，网络访问失败！！！");
            vcOrderRecharge.setRequestStatus("-500");
            vcOrderRecharge.setRequestMsg("网络连接失败");
            vcOrderRechargeService.save(vcOrderRecharge);
            return new ResponseBean(500,"网络访问失败！！！","网络访问失败！！！");
        }
        log.debug("访问充值API，成功");
        vcOrderRecharge.setRequestCode(jsonObject.getString("code"));
        vcOrderRecharge.setRequestStatus(jsonObject.getString("status"));
        vcOrderRecharge.setRequestMsg(jsonObject.getString("msg"));

        if (ApiStatusEnum.ERROR.getCode().equals(jsonObject.getString("status"))){
            log.info("访问充值API，结果失败,失败信息："+jsonObject.getString("msg"));
            vcOrderRecharge.setOrderStatus(OrderStatusEnum.FAILED.getValue());
            vcOrderRechargeService.save(vcOrderRecharge);
            return new ResponseBean(400,jsonObject.getString("code"),jsonObject.getString("msg"));
        }


        String orderStatus=jsonObject.getString("OrderStatus");
        OrderStatusEnum orderStatusEnum=OrderStatusEnum.getOrderStatusEnumByCode(orderStatus);
        vcOrderRecharge.setOrderStatus(orderStatusEnum.getValue());
        log.info("访问充值API，访问成功,订单 状态："+orderStatusEnum.getContent());
        vcOrderRechargeService.save(vcOrderRecharge);
        return new ResponseBean(200,orderStatus,orderStatusEnum.getContent());
    }

    @Override
    public ResponseBean queryOrder(String orderNo, String bizType) {

        SortedMap<Object, Object> param = new TreeMap<Object, Object>();
        param.put("OrderNo", orderNo);


        log.debug("开始访问，充值查询API，参数："+param);
        JSONObject jsonObject=queryApi(bizType,ApiTypeEnum.QueryOrder.getCode(),param);

        if (jsonObject==null)
        {
            log.info("访问充值查询API，网络访问失败！！！");
            return new ResponseBean(500,"","网络访问失败！！！");
        }
        log.debug("访问充值查询API，成功");


        if (ApiStatusEnum.ERROR.getCode().equals(jsonObject.getString("status"))){
            log.info("访问充值查询API，结果失败,失败信息："+jsonObject.getString("msg"));
            return new ResponseBean(400,jsonObject.getString("code"),jsonObject.getString("msg"));
        }

        String orderStatus=jsonObject.getString("OrderStatus");
        OrderStatusEnum orderStatusEnum=OrderStatusEnum.getOrderStatusEnumByCode(orderStatus);
        log.info("访问充值查询API，访问成功,订单 状态："+orderStatusEnum.getContent());

        VcOrderRecharge vcOrderRecharge=VcOrderRecharge.builder()
                .orderNo(orderNo)
                .bizType(bizType)
                .build();
        List<VcOrderRecharge> list=vcOrderRechargeService.getVcOrderRechargeList(vcOrderRecharge);
        if (list==null||list.size()==0){
            log.error("数据异常，异常数据订单ID："+orderNo);
        }else {
            vcOrderRecharge=list.get(0);
            log.debug("是否需要更新状态，订单ID："+orderNo);
            if(!orderStatusEnum.getValue().equals(vcOrderRecharge.getOrderStatus())) {
                vcOrderRecharge.setOrderStatus(orderStatusEnum.getValue());
                vcOrderRechargeService.updateById(vcOrderRecharge);
            }
        }
        return new ResponseBean(200,orderStatus,orderStatusEnum.getContent());
    }

    @Override
    public String callBack(CallBackReqDto callBackReqDto) {
        log.debug("收到订单号："+callBackReqDto.getOrderNo()+" ;的消息回调,"+callBackReqDto);
        if (callBackReqDto==null||callBackReqDto.getOrderNo()==null){
            log.error("参数错误！！！");
            return CallbackStatusEnum.ERROR.getContent();
        }

        VcOrderRecharge vcOrderRecharge=VcOrderRecharge.builder()
                .orderNo(callBackReqDto.getOrderNo())
                .bizType(callBackReqDto.getBizType())
                .accountVal(callBackReqDto.getAccountVal())
                .build();
        List<VcOrderRecharge> list=vcOrderRechargeService.getVcOrderRechargeList(vcOrderRecharge);
        OrderStatusEnum orderStatusEnum=OrderStatusEnum.getOrderStatusEnumByCode(callBackReqDto.getOrderStatus());
        if (list==null||list.size()==0){
            log.error("数据异常，异常数据订单ID："+callBackReqDto.getOrderNo());
            VcOrderCallback vcOrderCallback=VcOrderCallback.builder()
                    .id(IDUtils.getID())
                    .accountVal(callBackReqDto.getAccountVal())
                    .bizType(callBackReqDto.getBizType())
                    .orderNo(callBackReqDto.getOrderNo())
                    .userId(userId)
                    .orderStatus(orderStatusEnum.getValue())
                    .callbackStatus("数据异常，数据不存在："+callBackReqDto.getOrderNo())
                    .callbackTime(new Date())
                    .build();
            vcOrderCallbackService.save(vcOrderCallback);
            log.debug("回调地址失败，数据缺失！！！");
            return CallbackStatusEnum.ERROR.getContent();
        }else {
            vcOrderRecharge = list.get(0);
            log.debug("回调地址：" + vcOrderRecharge.getCallbackAddress());

            String result=requestCallback(callBackReqDto,vcOrderRecharge.getCallbackAddress(),orderStatusEnum);
            VcOrderCallback vcOrderCallback=VcOrderCallback.builder()
                    .id(IDUtils.getID())
                    .accountVal(callBackReqDto.getAccountVal())
                    .bizType(callBackReqDto.getBizType())
                    .orderNo(callBackReqDto.getOrderNo())
                    .userId(userId)
                    .orderStatus(orderStatusEnum.getValue())
                    .callbackStatus(CallbackStatusEnum.ERROR.getCode())
                    .callbackTime(new Date())
                    .build();

            log.debug("订单状态：",callBackReqDto.getOrderStatus());
            vcOrderCallback.setCallbackStatus(result);
            vcOrderCallbackService.save(vcOrderCallback);
            if (CallbackStatusEnum.OK.getCode().equals(result)){
                log.debug("回调地址成功！！！");
            }
            vcOrderRecharge.setCustomerCallbackResult(result);
            vcOrderRecharge.setOrderStatus(orderStatusEnum.getValue());
            vcOrderRecharge.setCallback(1);
            vcOrderRecharge.setCallbackTime(new Date());

            vcOrderRechargeService.updateById(vcOrderRecharge);
            return result;
        }
    }

    @Override
    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public String againCallBack() {
        log.debug("主动回调任务开始");
        Long start=System.currentTimeMillis();
        List<VcOrderRecharge> list= vcOrderRechargeService.getAgainCallBack();
        log.debug("需要主动回调任务数："+list.size());
        list.forEach(entity->{
            OrderStatusEnum orderStatusEnum=OrderStatusEnum.getOrderStatusEnumByCode(queryOrder(entity.getOrderNo(),entity.getBizType()).getMsg());
            entity.setOrderStatus(orderStatusEnum.getValue());
            log.debug("回调订单详情："+entity);
            CallBackReqDto callBackReqDto=CallBackReqDto.builder()
                    .accountVal(entity.getAccountVal())
                    .bizType(entity.getBizType())
                    .orderNo(entity.getOrderNo())
                    .orderStatus(orderStatusEnum.getCode())
                    .time(String.valueOf(System.currentTimeMillis()))
                    .build();
            String result=requestCallback(callBackReqDto,entity.getCallbackAddress(),orderStatusEnum);
            VcOrderRecharge vcOrderRecharge=new VcOrderRecharge();
            vcOrderRecharge.setId(entity.getId());
            if (CallbackStatusEnum.OK.getCode().equals(result)){
                log.debug("回调成功！！！");
                vcOrderRecharge.setAgainCallbackStatus(AgainCallbackStatus.SUCCESS.getCode());
            }else{
                log.warn("回调失败！！！");
                vcOrderRecharge.setAgainCallbackStatus(AgainCallbackStatus.FAIL.getCode());
            }
            vcOrderRecharge.setAgainCallbackResult(result);
            vcOrderRecharge.setAgainCallbackTimes(new Date());
            vcOrderRechargeService.updateById(vcOrderRecharge);
        });
        log.debug("回调订单结束，总耗时："+(System.currentTimeMillis()-start)/1000+" 秒");
        return "OK";
    }

    /**
     * @Author: Mr.Luke
     * @Description: 发起回调方法
     * @Date: 14:15 2020/3/5
     * @Param: [callBackReqDto, address, orderStatusEnum]
     * @return: java.lang.String
     */
    private String requestCallback(CallBackReqDto  callBackReqDto,String address,OrderStatusEnum orderStatusEnum){
        StringBuilder str = new StringBuilder("?");
        str.append("orderNo=" + callBackReqDto.getOrderNo());
        str.append("&accountVal=" + callBackReqDto.getAccountVal());
        str.append("&bizType=" + callBackReqDto.getBizType());
        str.append("&orderStatus=" + callBackReqDto.getOrderStatus());
        str.append("&orderMsg=" + orderStatusEnum.getContent());
        str.append("&time=" + callBackReqDto.getTime());

        log.debug("回调参数：" + str.toString());
        String result="network error";
        try {
            result = restTemplate.getForObject(address + str.toString(), String.class);
        }catch (Exception e){
            log.error("回调地址错误",e);
        }
        return result;
    }



    /**
     * @Author: Mr.Luke
     * @Description: 查询服务实现
     * @Date: 11:46 2019/10/15
     * @Param: [bizType, service]
     * @return: org.jeecg.modules.shiro.vo.ResponseBean
     */
    private JSONObject queryApi(String bizType, String service, SortedMap<Object,Object> param){

        SortedMap<Object,Object> signMap=new TreeMap<Object,Object>();

        StringBuilder url=new StringBuilder(apiHttpAddress);
        url.append("?Service="+service+"&V=2.0&UserId=");
        url.append(userId);
        signMap.put("UserId",userId);
        url.append("&BizType=");
        url.append(bizType);
        signMap.put("BizType",bizType);
        url.append("&Time=");
        long time=System.currentTimeMillis()/1000;
        url.append(time);
        signMap.put("Time",time);

        if (param!=null){
            signMap.putAll(param);
            param.forEach((key,value)->{
                url.append("&"+key+"="+value);
            });
        }

        url.append("&Sign=");
        String sign= SignUtil.createSign(signMap,apiKey);
        url.append(sign);
        log.debug("访问url:"+url);
        ResponseEntity<String> response=restTemplate.getForEntity(url.toString(), String.class);
        log.debug("响应结果："+response.getBody());
        if (!HttpStatus.OK.equals(response.getStatusCode())){
            log.info("查询"+service+"失败，错误代码："+response.getStatusCode());
            log.info("访问URL："+url);
            return null;
        }
        return JSONObject.parseObject(response.getBody());
    }



}
