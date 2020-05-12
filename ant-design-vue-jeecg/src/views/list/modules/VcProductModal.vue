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
        <a-form-item label="id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'id', validatorRules.id]" placeholder="请输入产品id号" style="width: 100%" :disabled="isDisabled"/>
        </a-form-item>


        <a-form-item label="原价" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'price', validatorRules.price]" placeholder="请输入原价" style="width: 100%"/>
        </a-form-item>
          
        <a-form-item label="折扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'saleDiscount', validatorRules.saleDiscount]" placeholder="请输入折扣" style="width: 100%"/>
        </a-form-item>
          
        <a-form-item label="销售价格" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'salePrice', validatorRules.salePrice]" placeholder="请输入销售价格" style="width: 100%"/>
        </a-form-item>
          
        <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'unit', validatorRules.unit]" placeholder="请输入单位"></a-input>
        </a-form-item>
          
        <a-form-item label="数字描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'numSpec', validatorRules.numSpec]" placeholder="请输入数字描述"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  
  export default {
    name: "VcProductModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        isDisabled: false,
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
        price:{rules: [{ required: true, message: '请输入产品单价!' }]},
        saleDiscount:{rules: [{ required: true, message: '请输入产品折扣!' }]},
        salePrice:{rules: [{ required: true, message: '请输入产品折后价格!' }]},
        unit:{},
        numSpec:{},
        },
        url: {
          add: "/vcapi/vcProduct/add",
          edit: "/vcapi/vcProduct/edit",
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
        if(this.model == null || this.model == undefined || this.model.id == null || this.model.id == undefined){
            this.isDisabled = false;
        }else{
            this.isDisabled = true;
        }
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','price','saleDiscount','salePrice','unit','numSpec'))
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
        this.form.setFieldsValue(pick(row,'id','price','saleDiscount','salePrice','unit','numSpec'))
      }
      
    }
  }
</script>