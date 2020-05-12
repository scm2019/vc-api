<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :okButtonProps="{ props: {disabled: disableSubmit} }"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="负责充值的账户ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userId', validatorRules.userId]" placeholder="请输入负责充值的账户ID"></a-input>
        </a-form-item>
          
        <a-form-item label="业务类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bizType']" :trigger-change="true" dictCode="" placeholder="请选择业务类型"/>
        </a-form-item>
          
        <a-form-item label="充值账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accountVal', validatorRules.accountVal]" placeholder="请输入充值账号"></a-input>
        </a-form-item>
          
        <a-form-item label="用户订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'orderNo', validatorRules.orderNo]" placeholder="请输入用户订单号"></a-input>
        </a-form-item>
          
        <a-form-item label="标准产品 ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productId', validatorRules.productId]" placeholder="请输入标准产品 ID"></a-input>
        </a-form-item>
          
        <a-form-item label="购买数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'buyNum', validatorRules.buyNum]" placeholder="请输入购买数量" style="width: 100%"/>
        </a-form-item>
          
        <a-form-item label="下单人的 IP" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'customerIp', validatorRules.customerIp]" placeholder="请输入下单人的 IPP"></a-input>
        </a-form-item>
          
        <a-form-item label="特定产品充值时需要的参数，默认直接为空" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'extraData', validatorRules.extraData]" placeholder="请输入特定产品充值时需要的参数，默认直接为空"></a-input>
        </a-form-item>
          
        <a-form-item label="请求响应代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'requestCode', validatorRules.requestCode]" placeholder="请输入请求响应代码"></a-input>
        </a-form-item>
          
        <a-form-item label="请求状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'requestStatus', validatorRules.requestStatus]" placeholder="请输入请求状态"></a-input>
        </a-form-item>
          
        <a-form-item label="请求响应结果信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'requestMsg', validatorRules.requestMsg]" placeholder="请输入请求响应结果信息"></a-input>
        </a-form-item>
          
        <a-form-item label="订单状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'orderStatus', validatorRules.orderStatus]" placeholder="请输入订单状态" style="width: 100%"/>
        </a-form-item>
          
        <a-form-item label="是否回调" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--          <a-input-number v-decorator="[ 'callback', validatorRules.callback]" placeholder="请输入是否回调" style="width: 100%"/>-->
          <a-select v-decorator="['callback',validatorRules.callback]" placeholder="请选择是否回调">
            <a-select-option :key="1">是</a-select-option>
            <a-select-option :key="0">否</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="回调客户结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'customerCallbackResult', validatorRules.customerCallbackResult]" placeholder="请输入回调客户结果" style="width: 100%"/>
        </a-form-item>

        <a-form-item label="回调时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择回调时间" v-decorator="[ 'callbackTime', validatorRules.callbackTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>

        <a-form-item label="订单回调地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'callbackAddress', validatorRules.callbackAddress]" placeholder="请输入订单回调地址" style="width: 100%"/>
        </a-form-item>

        <a-form-item label="是否需要重新回调" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'againCallbackStatus', validatorRules.againCallbackStatus]" placeholder="请输入是否需要重新回调" style="width: 100%"/>
        </a-form-item>

        <a-form-item label="回调时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'againCallbackTimes', validatorRules.againCallbackTimes]" placeholder="请输入回调时间" style="width: 100%"/>
        </a-form-item>

        <a-form-item label="回调结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'againCallbackResult', validatorRules.againCallbackResult]" placeholder="请输入回调结果" style="width: 100%"/>
        </a-form-item>
        
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  
  export default {
    name: "VcOrderRechargeModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        disableSubmit:false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules:{
        userId:{rules: [{ required: true, message: '请输入负责充值的账户ID!' }]},
        bizType:{rules: [{ required: true, message: '请输入业务类型!' }]},
        accountVal:{rules: [{ required: true, message: '请输入充值账号!' }]},
        orderNo:{rules: [{ required: true, message: '请输入用户订单号!' }]},
        productId:{rules: [{ required: true, message: '请输入标准产品 ID!' }]},
        buyNum:{},
        customerIp:{},
        extraData:{},
        requestCode:{},
        requestStatus:{rules: [{ required: true, message: '请输入请求状态!' }]},
        requestMsg:{},
        createTime:{rules: [{ required: true, message: '请输入新增时间!' }]},
        updateTime:{rules: [{ required: true, message: '请输入修改时间!' }]},
        orderStatus:{rules: [{ required: true, message: '请输入订单状态!' }]},
        callback:{},
        callbackTime:{},
        customerCallbackResult:{},
        callbackAddress:{},
        againCallbackStatus:{},
        againCallbackTimes:{},
        againCallbackResult:{},
        },
        url: {
          add: "/VcOrderRecharge/vcOrderRecharge/add",
          edit: "/VcOrderRecharge/vcOrderRecharge/edit",
        }
     
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'userId','bizType','accountVal','orderNo','productId','buyNum','customerIp','extraData','requestCode','requestStatus','requestMsg','createTime','updateTime','orderStatus','callback','callbackTime','customerCallbackResult','callbackAddress','againCallbackStatus','againCallbackTimes','againCallbackResult'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'userId','bizType','accountVal','orderNo','productId','buyNum','customerIp','extraData','requestCode','requestStatus','requestMsg','createTime','updateTime','orderStatus','callback','callbackTime','customerCallbackResult','callbackAddress','againCallbackStatus','againCallbackTimes','againCallbackResult'))
      }
      
    }
  }
</script>