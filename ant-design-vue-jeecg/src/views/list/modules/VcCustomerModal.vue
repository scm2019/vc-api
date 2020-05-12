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
        <a-form-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="['userId',validatorRules.userId]" placeholder="请选择客户">
            <a-select-option v-for="customer in customerList" :key="customer.id">{{customer.username}}</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'companyName', validatorRules.companyName]" placeholder="请输入公司名称"></a-input>
        </a-form-item>
          
        <a-form-item label="对接人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'broker', validatorRules.broker]" placeholder="请输入对接人"></a-input>
        </a-form-item>
          
        <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'phone', validatorRules.phone]" placeholder="请输入手机号"></a-input>
        </a-form-item>
        <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'email', validatorRules.email]" placeholder="请输入邮箱"></a-input>
        </a-form-item>
          
        <a-form-item label="营业执照" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-radio-group @change="onChange" v-model="radioValue">
            <a-radio :value="1">输入框</a-radio>
            <a-radio :value="2">图片、文件</a-radio>
          </a-radio-group>
          <a-input v-decorator="[ 'certificate', validatorRules.certificate]" placeholder="请输入营业执照" v-if="radioValue==1"></a-input>
          <a-upload
            listType="picture-card"
            class="avatar-uploader"
            :showUploadList="false"
            :action="uploadAction"
            :data="{'isup':1}"
            :headers="headers"
            :beforeUpload="beforeUpload"
            @change="handleChange"
            v-if="radioValue == 2"
          >
            <img v-if="certificate" :src="getAvatarView()" alt="头像" style="height:104px;max-width:300px"/>
            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
        </a-form-item>
          
        <a-form-item label="授信额度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'quota', validatorRules.quota]" placeholder="请输入授信额度" style="width: 100%"/>
        </a-form-item>
          
        <a-form-item label="余额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'money', validatorRules.money]" placeholder="请输入余额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="折扣(十分制)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'discount', validatorRules.discount]" placeholder="请输入折扣(十分制)" style="width: 100%" :min="0" :max="10"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import { getAction } from '@/api/manage'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import Vue from 'vue'
  
  export default {
    name: "VcCustomerModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        disableSubmit:false,
        width:800,
        visible: false,
        model: {},
        radioValue : 1,
        customerList:[],
        uploadLoading: false,
        certificate: '',
        headers:{},
        picUrl: "",

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
        userId:{ rules: [{ required: true, message: '请输入客户名称!' }]},
        companyName:{rules: [{ required: true, message: '请输入公司名称' }] },
        broker:{rules: [{ required: true, message: '请输入对接人!' }] },
        phone:{rules: [{ required: true, message: '请输入手机号!' }]},
        email:{rules: [{ required: true, message: '请输入邮箱!' }]},
        certificate:{required: true, message: '请输入营业执照!' },
        radioValue:{},
        quota:{},
        discount:{},
        money:{},
        createBy:{},
        updateBy:{},
        createTime:{},
        updateTime:{},
        },
        url: {
          add: "/vcapi/vcCustomer/add",
          edit: "/vcapi/vcCustomer/edit",
          fileUpload:window._CONFIG['domianURL']+'/sys/common/upload',
          imgerver: window._CONFIG['domianURL']+"/sys/common/view",
        }
     
      }
    },
    created () {
        const token = Vue.ls.get(ACCESS_TOKEN);
        //console.log("VcCustomerModal-----------");
        this.headers = {"X-Access-Token":token}
    },
    computed:{
        uploadAction:function () {
            return this.url.fileUpload;
        }
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        let param ={};
        if(this.model == null || this.model == undefined || this.model.userId == null || this.model.userId ==undefined){
            param = {userId:''};
            this.radioValue= 1;
        }else{
            param = {userId:this.model.userId};
            this.radioValue= this.model.radioValue;
            console.log("VcCustomerModel radioValue:"+this.radioValue);
            this.certificate = this.model.certificate;
        }
        this.getAllInCompleteCustomer(param);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'userId','companyName','broker','phone','email','certificate','radioValue','quota','money','discount'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        debugger;
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
            this.model.radioValue = this.radioValue;
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
        this.form.setFieldsValue(pick(row,'companyName','broker','phone','email','certificate','quota','money','discount'))
      },
      getAllInCompleteCustomer(param){
          //debugger;
          getAction('/sys/user/getAllInCompleteCustomer',param).then((res)=>{
              if(res.success){
                  this.customerList = res.result;
              }
          });
      },
      onChange(){
          this.certificate = '';
      },
      beforeUpload: function(file){
          var fileType = file.type;
          if(fileType.indexOf('image')<0){
              this.$message.warning('请上传非可执行类的文件!');
              return false;
          }
          //TODO 验证文件大小
      },
      handleChange (info) {
          this.picUrl = "";
          if (info.file.status === 'uploading') {
              this.uploadLoading = true;
              return
          }
          if (info.file.status === 'done') {
              var response = info.file.response;
              this.uploadLoading = false;
              console.log(response);
              if(response.success){
                  this.model.certificate = response.message;
                  this.certificate = response.message;
              }else{
                  this.$message.warning(response.message);
              }
          }
      },
      getAvatarView(){
          return this.url.imgerver +"/"+ this.model.certificate;
      },

      
    }
  }
</script>