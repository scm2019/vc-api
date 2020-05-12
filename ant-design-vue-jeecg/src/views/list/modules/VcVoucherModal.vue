<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="卡面图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-upload
            listType="picture-card"
            class="avatar-uploader"
            :showUploadList="false"
            :action="uploadAction"
            :data="{'isup':1}"
            :headers="headers"
            :beforeUpload="beforeUpload"
            @change="handleChange"
            :disabled="isDisabled"
          >
            <img v-if="isUpload == true" :src="getAvatarView()" alt="头像" style="height:104px;max-width:300px"/>
            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item label="卡名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入卡名称" :disabled="isDisabled"></a-input>
        </a-form-item>

        <a-form-item label="卡号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cardCode', validatorRules.cardCode]" placeholder="请输入卡号" :disabled="isDisabled"></a-input>
        </a-form-item>
          
        <a-form-item label="SKU" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sku', validatorRules.sku]" placeholder="请输入SKU" :disabled="isDisabled"></a-input>
        </a-form-item>

        <a-form-item label="卡密" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'password', validatorRules.password]" placeholder="请输入卡密" :disabled="isDisabled"></a-input>
        </a-form-item>

        <a-form-item label="卡描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cardDesc', validatorRules.cardDesc]" placeholder="请输入卡描述" style="width: 100%" :disabled="isDisabled"/>
        </a-form-item>
          
        <a-form-item label="卡状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="[ 'status', validatorRules.status]" placeholder="请选择卡状态" :disabled="isDisabled">
            <a-select-option v-for="status in statusList" :key="status.value">{{status.text}}</a-select-option>
          </a-select>
<!--          <a-input v-decorator="[ 'state', validatorRules.state]" placeholder="请输入卡状态 0|作废 1|待激活 2|有效 3|冻结 4|挂失 5|失效"></a-input>-->
        </a-form-item>

        <a-form-item label="卡面金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'amount', validatorRules.amount]" placeholder="请输入卡面金额" style="width: 100%" :disabled="isDisabled"/>
        </a-form-item>

        <a-form-item label="卡面余额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'balance', validatorRules.balance]" placeholder="请输入卡面余额" style="width: 100%" :disabled="isDisabled"/>
        </a-form-item>
          
        <a-form-item label="卡类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="[ 'type', validatorRules.type]" placeholder="请选择卡类型" :disabled="isDisabled">
            <a-select-option :key="1">一次性卡</a-select-option>
            <a-select-option :key="2">重复卡</a-select-option>
          </a-select>
          <!--<a-input-number v-decorator="[ 'type', validatorRules.type]" placeholder="请输入卡类型  1 一次性卡 2 重复卡" style="width: 100%"/>-->
        </a-form-item>
          
        <a-form-item label="激活时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择激活时间" v-decorator="[ 'activeDate', validatorRules.activeDate]" :trigger-change="true" style="width: 100%" :disabled="isDisabled"/>
        </a-form-item>
          
        <a-form-item label="过期时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择过期时间" v-decorator="[ 'expireDate', validatorRules.expireDate]" :trigger-change="true" style="width: 100%" :disabled="isDisabled"/>
        </a-form-item>
          
        <a-form-item label="是否已消费" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--          <a-input-number v-decorator="[ 'isUsed', validatorRules.isUsed]" placeholder="请输入是否已消费" style="width: 100%"/>-->
          <a-select v-decorator="[ 'isUsed', validatorRules.isUsed]" placeholder="请选择是否已消费" :disabled="isDisabled">
            <a-select-option :key="0">否</a-select-option>
            <a-select-option :key="1">是</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="发卡行" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'issuingBank', validatorRules.issuingBank]" placeholder="请输入发卡行" :disabled="isDisabled"></a-input>
        </a-form-item>

        <a-form-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="[ 'customerId', validatorRules.customerId]" placeholder="请选择客户" :disabled="isDisabled">
            <a-select-option v-for="customer in customerList" :key="customer.id">{{customer.username}}</a-select-option>
          </a-select>
          <!--          <a-input v-decorator="[ 'state', validatorRules.state]" placeholder="请输入卡状态 0|作废 1|待激活 2|有效 3|冻结 4|挂失 5|失效"></a-input>-->
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import Vue from 'vue'


  export default {
    name: "VcVoucherModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        isDisabled: false,
        uploadLoading: false,
        customerList:[],
        statusList:[],
        headers:{},
        isUpload: false,
        mark :'',
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
        name:{rules: [{ required: true, message: '请输入卡名称!' }]},
        cardCode:{rules: [{ required: true, message: '请输入卡号!' }]},
        sku:{rules: [{ required: true, message: '请输入SKU!' }]},
        status:{rules: [{ required: true, message: '请选择卡状态' }]},
        cardDesc:{rules: [{ required: true, message: '请选择卡描述' }]},
        issuingBank:{rules: [{ required: true, message: '请输入发卡行' }]},
        password:{rules: [{ required: true, message: '请输入卡密' }]},
        balance:{},
        type:{},
        activeDate:{},
        expireDate:{rules: [{ required: true, message: '请输入过期时间!' }]},
        isUsed:{},
        mark:{},
        customerId:{},
        amount:{rules: [{ required: true, message: '请输入卡面金额!' }]},
        },
        url: {
          add: "/vcapi/vcVoucher/add",
          edit: "/vcapi/vcVoucher/edit",
          fileUpload:window._CONFIG['domianURL']+'/sys/common/upload',
          imgerver: window._CONFIG['domianURL']+"/sys/common/view",
        }
     
      }
    },
    created () {
        const token = Vue.ls.get(ACCESS_TOKEN);
        //console.log("VcCustomerModal-----------");
        this.headers = {"X-Access-Token":token}
        this.getAllStatus();
        //获取所有角色为customer 的用户数据
        this.getAllCustomers();
    },
    computed:{
        uploadAction:function () {
            return this.url.fileUpload;
        }
    },
    methods: {
        add() {
            this.edit({});
        },
        edit(record) {
            debugger;
            this.form.resetFields();
            this.model = Object.assign({}, record);
            if (this.model == null || this.model == undefined || this.model.id == null || this.model.id == undefined) {
                this.isDisabled = false;
                this.isUpload = false;
            } else {
                if (this.model.status != 1) {
                    this.isDisabled = true;
                }
                this.mark = this.model.mark;
                this.isUpload = true;
            }
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, 'cardCode', 'sku', 'status', 'balance', 'type', 'activeDate', 'expireDate', 'isUsed', 'mark', 'amount', 'password', 'name', 'issuingBank', 'cardDesc','customerId'))
            })
        },
        close() {
            this.$emit('close');
            this.visible = false;
        },
        handleOk() {
            const that = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    that.confirmLoading = true;
                    let httpurl = '';
                    let method = '';
                    if (!this.model.id) {
                        httpurl += this.url.add;
                        method = 'post';
                    } else {
                        httpurl += this.url.edit;
                        method = 'put';
                    }
                    let formData = Object.assign(this.model, values);
                    console.log("表单提交数据", formData)
                    httpAction(httpurl, formData, method).then((res) => {
                        if (res.success) {
                            that.$message.success(res.message);
                            that.$emit('ok');
                        } else {
                            that.$message.warning(res.message);
                        }
                    }).finally(() => {
                        that.confirmLoading = false;
                        that.close();
                    })
                }

            })
        },
        handleCancel() {
            this.close()
        },
        popupCallback(row) {
            this.form.setFieldsValue(pick(row, 'cardCode', 'sku', 'status', 'balance', 'type', 'activeDate', 'expireDate', 'isUsed', 'mark', 'amount', 'password', 'name', 'issuingBank', 'cardDesc','customerId'))
        },
        beforeUpload: function (file) {
            var fileType = file.type;
            console.log("上传文件的格式为:" + fileType);
            if (fileType.indexOf('image') < 0) {
                this.$message.warning('请上传图片!');
                return false;
            }
            //TODO 验证文件大小
        },
        handleChange(info) {
            this.picUrl = "";
            if (info.file.status === 'uploading') {
                this.uploadLoading = true;
                return
            }
            if (info.file.status === 'done') {
                var response = info.file.response;
                this.uploadLoading = false;
                console.log(response);
                if (response.success) {
                    this.model.mark = response.message;
                    this.mark = response.message;
                    this.isUpload = true;
                } else {
                    this.isUpload = false;
                    this.$message.warning(response.message);
                }
            }
        },
        getAvatarView() {
            return this.url.imgerver + "/" + this.model.mark;
        },
        getAllStatus() {
            getAction('/sys/dict/getDictItems/' + 'vc_status').then((res) => {
                if (res.success) {
                    this.statusList = res.result;
                }
            });
        },
        getAllCustomers() {
            getAction('/sys/user/getUsersByRoleCode',{code:'customer'}).then((res) => {
                if (res.success) {
                    this.customerList = res.result;
                }
            });
        },
    }
  }
</script>