<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="卡号">
              <a-input placeholder="请输入卡号" v-model="queryParam.cardCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="SKU">
              <a-input placeholder="请输入SKU" v-model="queryParam.sku"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="advanced">
            <a-col :md="8" :sm="24">
              <a-form-item label="发卡行">
                <a-input placeholder="请输入发卡行" v-model="queryParam.issuingBank"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="8" :sm="24">
              <a-form-item label="卡名称">
                <a-input placeholder="请输入卡名称" v-model="queryParam.name"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="8" :sm="24">
              <a-form-item label="卡状态">
                <a-select v-model="queryParam.status" placeholder="请选择卡状态">
                  <a-select-option v-for="status in statusList" :key="status.value">{{status.text}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="激活时间">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.activeDate_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.activeDate_end"></j-date>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="过期时间">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.expireDate_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.expireDate_end"></j-date>
              </a-form-item>
            </a-col>
          </template>
          <a-col :md="!advanced && 8 || 24" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('虚拟卡券数据')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
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
        
        <template slot="imgSlot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.mark)" icon="voucher"/>
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
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm v-if="record.status==1" title="确定激活吗?" @confirm="() => handleStatus(record.id,2)">
                  <a>启用</a>
                </a-popconfirm>
                <a-popconfirm v-if="record.status==0 || record.status==5" title="确定删除吗?" @confirm="() => handleDelete(record.id,8)">
                  <a>删除</a>
                </a-popconfirm>
                <a-popconfirm v-if="record.status == 2" title="确定冻结吗?" @confirm="() => handleStatus(record.id,3)">
                  <a>冻结</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <vcVoucher-modal ref="modalForm" @ok="modalFormOk"></vcVoucher-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import VcVoucherModal from './modules/VcVoucherModal'
  import JDate from '@/components/jeecg/JDate.vue'
  import { getAction } from '@/api/manage'
  import {postAction} from "../../api/manage";

  export default {
    name: "VcVoucherList",
    mixins:[JeecgListMixin],
    components: {
      JDate,
      VcVoucherModal
    },
    data () {
      return {
        // description: '虚拟卡券数据管理页面',
        advanced: false,
        statusList:[],
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
            title:'卡面图片',
            align:"center",
            dataIndex: 'mark',
            scopedSlots: {customRender: "imgSlot"}
          },
          {
            title:'卡名称',
            align:"center",
            dataIndex: 'name',
          },
          {
            title:'卡号',
            align:"center",
            dataIndex: 'cardCode'
          },
          {
              title:'卡描述',
              align:"center",
              dataIndex: 'cardDesc'
          },
          {
            title:'发卡行',
            align:"center",
            dataIndex: 'issuingBank'
          },
          {
            title:'SKU',
            align:"center",
            dataIndex: 'sku'
          },
          {
            title:'卡状态',
            align:"center",
            dataIndex: 'statusDesc'
          },
          {
            title:'卡面金额',
            align:"center",
            dataIndex: 'amount',
          },
          {
            title:'卡面余额',
            align:"center",
            dataIndex: 'balance'
          },
          {
            title:'卡类型',
            align:"center",
            dataIndex: 'type',
              customRender: (text, row, index) => {
                  if(text == 1) {
                      return '一次性卡';
                  }else{
                      return '重复卡';
                  }
              }
          },
          {
            title:'激活时间',
            align:"center",
            dataIndex: 'activeDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'过期时间',
            align:"center",
            dataIndex: 'expireDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
              title:'所属客户',
              align:"center",
              dataIndex: 'customerName',
          },
          {
              title: '是否已消费',
              align: "center",
              dataIndex: 'isUsed',
              customRender: (text, row, index) => {
                  if(text == 1) {
                      return '是';
                  }else{
                      return '否';
                  }
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
          list: "/vcapi/vcVoucher/list",
          delete: "/vcapi/vcVoucher/delete",
          deleteBatch: "/vcapi/vcVoucher/deleteBatch",
          exportXlsUrl: "/vcapi/vcVoucher/exportXls",
          importExcelUrl: "vcapi/vcVoucher/importExcel",
          imgerver: window._CONFIG['domianURL'] + "/sys/common/view",
        },
        dictOptions:{
        } 
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
        initDictConfig() {
        },
        toggleAdvanced() {
            this.advanced = !this.advanced
        },
        getAvatarView: function (mark) {
            return this.url.imgerver + "/" + mark;
        },
        getAllStatus() {
            getAction('/sys/dict/getDictItems/' + 'vc_status').then((res) => {
                if (res.success) {
                    this.statusList = res.result;
                }
            });
        },
        handleStatus(id, type) {
            postAction('/vcapi/vcVoucher/edit', {id: id, status: type}).then((res) => {
                if (res.success) {
                    this.msg().success(res.message);
                }
            });
        },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>