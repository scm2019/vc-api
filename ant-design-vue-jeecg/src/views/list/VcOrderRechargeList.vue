<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="48">
                  <a-col :md="8" :sm="24">
                    <a-form-item label="充值账号">
                      <a-input v-model="queryParam.accountVal" placeholder=""/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="8" :sm="24">
                    <a-form-item label="业务类型">
                      <a-select v-model="queryParam.bizType" placeholder="请选择" default-value="0">
                        <a-select-option value="OIL">加油卡业务</a-select-option>
                        <a-select-option value="MOBILE">手机话费业务</a-select-option>
                        <a-select-option value="ECARD">点卡会员业务 (Q 币、会员、视频、游戏等)</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                  <template v-if="advanced">
                  <a-col :md="8" :sm="24">
                      <a-form-item label="用户订单号">
                        <a-input v-model="queryParam.orderNo" placeholder=""/>
                      </a-form-item>
                      </a-col>
                      <a-col :md="8" :sm="24">
                        <a-form-item label="用户">
                          <a-select v-model="queryParam.createBy" placeholder="请选择客户">
                            <a-select-option v-for="customer in customerList" :key="customer.username">{{customer.username}}</a-select-option>
                          </a-select>
                        </a-form-item>
                      </a-col>
                    </template>
                  <a-col :md="!advanced && 8 || 24" :sm="24">
                     <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                       <a-button type="primary" @click="searchQuery">查询</a-button>
                       <a-button style="margin-left: 8px" @click="resetSearchForm">重置</a-button>
                       <a @click="toggleAdvanced" style="margin-left: 8px">
                         {{ advanced ? '收起' : '展开' }}
                         <a-icon :type="advanced ? 'up' : 'down'"/>
                       </a>
                     </span>
                   </a-col>
                </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus" v-show="isShow">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('vc_order_recharge')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import" v-show="isShow">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0" v-show="isShow">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-show="isShow">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">查看</a>

          <a-divider type="vertical" />
          <a-dropdown v-show="isShow">
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
              <a-menu-item>
               <a @click="handleEdit(record)">编辑</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <vcOrderRecharge-modal ref="modalForm" @ok="modalFormOk"></vcOrderRecharge-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import VcOrderRechargeModal from './modules/VcOrderRechargeModal'
  import { getAction } from '@/api/manage'
  export default {
    name: "VcOrderRechargeList",
    mixins:[JeecgListMixin],
    components: {
      VcOrderRechargeModal
    },
    data () {
      return {
        // 高级搜索 展开/关闭
        advanced: false,
        // 查询参数
        queryParam: {},
        isShow : false,
        customerList:[],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          // {
          //   title:'负责充值的账户ID',
          //   align:"center",
          //   dataIndex: 'userId',
          //
          // },
          {
            title:'业务类型',
            align:"center",
            dataIndex: 'bizType'
          },
          {
            title:'充值账号',
            align:"center",
            dataIndex: 'accountVal'
          },
          {
            title:'用户订单号',
            align:"center",
            dataIndex: 'orderNo'
          },
          {
            title:'标准产品 ID',
            align:"center",
            dataIndex: 'productId'
          },
          {
            title:'标准产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'购买数量',
            align:"center",
            dataIndex: 'buyNum'
          },
          // {
          //   title:'请求响应代码',
          //   align:"center",
          //   dataIndex: 'requestCode'
          // },
          // {
          //   title:'请求状态',
          //   align:"center",
          //   dataIndex: 'requestStatus'
          // },
          // {
          //   title:'请求响应结果信息',
          //   align:"center",
          //   dataIndex: 'requestMsg'
          // },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'订单状态',
          //   align:"center",
          //   dataIndex: 'orderStatus'
          // },
          {
            title:'客户',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/vcapi/vcOrderRecharge/pageList",
          delete: "/vcapi/vcOrderRecharge/delete",
          deleteBatch: "/vcapi/vcOrderRecharge/deleteBatch",
          exportXlsUrl: "/vcapi/vcOrderRecharge/exportXls",
          importExcelUrl: "/vcapi/vcOrderRecharge/importExcel",
        },
        dictOptions:{
         bizType:[],
        } 
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    created (){
       this.getUserIsAdmin();
       this.getAllCustomers();
       //this.initDictConfig();
    },
    methods: {
      initDictConfig(){
          getAction('/vcapi/vcRecharge/BizType').then((res) => {
            if (res.code == 200) {
              this.$set(this.dictOptions, 'bizType', res.data)
            }
          })
      },
      getAllCustomers(){
          //debugger;
          getAction('/sys/user/getUsersByRoleCode',{code:'customer'}).then((res)=>{
              if(res.success){
                  this.customerList = res.result;
              }
          });
      },
      getUserIsAdmin(){
        getAction('/sys/user/queryUserInfoByToken').then((res)=>{
           if(res.success){
               if(res.result.roles.length != 0 && res.result.roles.includes('admin')){
                   this.isShow = true;
               }
           }
        });
      },
      resetSearchForm () {
        this.queryParam = {

        }
      },
     toggleAdvanced () {
        this.advanced = !this.advanced
      },
      handleDetail(record) {
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title = "详情";
          this.$refs.modalForm.disableSubmit = true;
      },
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>