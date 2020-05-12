<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="客户名称">
              <a-input v-model="queryParam.userName" placeholder=""/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="公司名称">
              <a-input v-model="queryParam.companyName" placeholder=""/>
            </a-form-item>
          </a-col>
          <template v-if="advanced">
            <a-col :md="8" :sm="24">
              <a-form-item label="对接人">
                <a-input v-model="queryParam.broker" placeholder=""/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="手机">
                <a-input-number v-model="queryParam.phone" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="邮箱">
                <a-input-number v-model="queryParam.email" style="width: 100%"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('客户信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import" v-show="isShow">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0" v-show="isShow">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
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

        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar v-if="record.radioValue == 2" shape="square" :src="getAvatarView(record.certificate)" icon="customer"/>
            <p v-if="record.radioValue == 1">{{record.certificate}}</p>
          </div>
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
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item>
                  <a @click="handleEdit(record)" v-show="isShow">编辑</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <vcCustomer-modal ref="modalForm" @ok="modalFormOk"></vcCustomer-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import VcCustomerModal from './modules/VcCustomerModal'
  import { getAction } from '@/api/manage'

  export default {
    name: "VcCustomerList",
    mixins:[JeecgListMixin],
    components: {
      VcCustomerModal
    },
    data () {
      return {
        // description: '客户信息管理页面',
        isShow:false,
        // 高级搜索 展开/关闭
        advanced: false,
        // 查询参数
        queryParam: {},
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
          {
              title:'客户',
              align:"center",
              dataIndex: 'userName'
          },
          {
            title:'公司名称',
            align:"center",
            dataIndex: 'companyName'
          },
          {
            title:'对接人',
            align:"center",
            dataIndex: 'broker'
          },
          {
            title:'手机号',
            align:"center",
            dataIndex: 'phone'
          },
          {
            title:'邮箱',
            align:"center",
            dataIndex: 'email'
          },
          {
            title:'营业执照',
            align:"center",
            dataIndex: 'certificate',
            scopedSlots: {customRender: "avatarslot"}
          },
          {
            title:'授信额度',
            align:"center",
            dataIndex: 'quota'
          },
          {
            title:'余额',
            align:"center",
            dataIndex: 'money'
          },
          {
            title:'折扣',
            align:"center",
            dataIndex: 'discount'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'修改人',
            align:"center",
            dataIndex: 'updateBy'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'修改时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          imgerver: window._CONFIG['domianURL'] + "/sys/common/view",
          list: "/vcapi/vcCustomer/list",
          delete: "/vcapi/vcCustomer/delete",
          deleteBatch: "/vcapi/vcCustomer/deleteBatch",
          exportXlsUrl: "/vcapi/vcCustomer/exportXls",
          importExcelUrl: "vcapi/vcCustomer/importExcel",
        },
        dictOptions:{
        } 
      }
    },
    created(){
        this.getUserIsAdmin();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      resetSearchForm () {
          this.queryParam = {

          }
      },
      toggleAdvanced () {
          this.advanced = !this.advanced
      },
      getAvatarView: function (avatar) {
          return this.url.imgerver + "/" + avatar;
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
      handleDetail(record) {
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title = "详情";
          this.$refs.modalForm.disableSubmit = true;
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>