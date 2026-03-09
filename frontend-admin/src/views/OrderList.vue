<template>
  <div class="order-list-wrapper">
  <el-card class="order-list" shadow="hover">
    <template #header>
      <div class="card-header">
        <div class="header-title">
          <svg viewBox="0 0 24 24" width="24" height="24" class="header-icon">
            <path fill="currentColor" d="M3,4H21V8H3V4M3,10H21V14H3V10M3,16H21V20H3V16Z"/>
          </svg>
          <span>订单列表</span>
        </div>
        <span class="header-subtitle">共 {{ total }} 条订单记录</span>
      </div>
    </template>
    <!-- 筛选栏 -->
    <el-form :inline="true" :model="query" class="filter-form">
      <el-form-item label="我的编号">
        <el-input v-model="query.myNo" placeholder="我的编号" clearable style="width: 120px" />
      </el-form-item>
      <el-form-item label="订单编号">
        <el-input v-model="query.orderNo" placeholder="订单编号" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="订单题目">
        <el-input v-model="query.title" placeholder="订单题目" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="语言">
        <el-select v-model="query.language" placeholder="全部" clearable style="width: 120px">
          <el-option v-for="item in languageOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部" clearable style="width: 120px">
          <el-option v-for="(label, value) in statusMap" :key="value" :label="label" :value="Number(value)" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadOrders">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="orders" border stripe v-loading="loading" class="order-table">
      <el-table-column prop="orderNo" label="订单编号" width="160" />
      <el-table-column prop="title" label="题目" width="140" show-overflow-tooltip />
      <el-table-column prop="language" label="语言" width="80" />
      <el-table-column prop="database" label="数据库" width="90" />
      <el-table-column label="需求" min-width="200">
        <template #default="{ row }">
          <div class="requirement-cell">
            <el-tooltip :content="row.requirement" placement="top" :disabled="!row.requirement || row.requirement.length < 30">
              <span class="requirement-text">{{ row.requirement || '-' }}</span>
            </el-tooltip>
            <el-button v-if="row.attachmentPath" type="primary" link size="small" class="attachment-btn" @click="downloadFile(row.id)">
              <el-icon><Download /></el-icon>
            </el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag size="small" :type="statusTagType[row.status]">{{ statusMap[row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="instanceName" label="实例名" width="90" show-overflow-tooltip />
      <el-table-column prop="schoolName" label="学校" width="100" show-overflow-tooltip />
      <el-table-column label="总价" width="80" align="right">
        <template #default="{ row }">¥{{ row.totalPrice || 0 }}</template>
      </el-table-column>
      <el-table-column label="结算" width="70" align="center">
        <template #default="{ row }">
          <el-tag size="small" :type="row.settled === 1 ? 'success' : 'info'">{{ row.settled === 1 ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="devDate" label="开发时间" width="100" />
      <el-table-column label="操作" width="120" fixed="right" align="center">
        <template #default="{ row }">
          <div class="action-buttons">
            <el-tooltip content="编辑" placement="top">
              <button class="action-icon-btn" @click="editOrder(row)">
                <el-icon><Edit /></el-icon>
              </button>
            </el-tooltip>
            <el-dropdown trigger="click" @command="(cmd) => handleRowCommand(cmd, row)">
              <button class="action-icon-btn">
                <el-icon><MoreFilled /></el-icon>
              </button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="upload" v-if="row.status >= 3">
                    <el-icon><Upload /></el-icon>上传修改
                  </el-dropdown-item>
                  <el-dropdown-item command="withdraw" v-if="row.status === 0 || row.status === -1">
                    <el-icon><RefreshLeft /></el-icon>撤回订单
                  </el-dropdown-item>
                  <template v-if="isAdmin">
                    <el-dropdown-item command="accept" v-if="row.status === 0" divided>
                      <el-icon><Check /></el-icon>接单
                    </el-dropdown-item>
                    <el-dropdown-item command="price">
                      <el-icon><Edit /></el-icon>填写价格
                    </el-dropdown-item>
                    <el-dropdown-item command="settle">
                      <el-icon><Check /></el-icon>{{ row.settled === 1 ? '取消结算' : '结算' }}
                    </el-dropdown-item>
                    <el-dropdown-item command="status">
                      <el-icon><Edit /></el-icon>修改状态
                    </el-dropdown-item>
                  </template>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="query.pageNum"
        v-model:page-size="query.pageSize"
        :page-sizes="[10, 20, 50]"
        :total="total"
        :pager-count="5"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadOrders"
        @current-change="loadOrders"
      >
        <template #total>
          共 {{ total }} 条
        </template>
      </el-pagination>
    </div>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="editVisible" title="编辑订单" width="720px" class="edit-dialog" :close-on-click-modal="false">
      <el-form :model="editForm" label-width="80px" class="edit-form">
        <div class="form-group">
          <div class="form-group-title">基本信息</div>
          <el-row :gutter="20">
            <el-col :span="14">
              <el-form-item label="订单标题">
                <el-input v-model="editForm.title" placeholder="请输入订单标题" />
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="我的编号">
                <el-input v-model="editForm.myNo" placeholder="请输入编号" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-group">
          <div class="form-group-title">技术配置</div>
          <el-row :gutter="16">
            <el-col :span="8">
              <el-form-item label="语言">
                <el-select v-model="editForm.language" clearable placeholder="请选择" style="width: 100%">
                  <el-option v-for="item in options.language" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="数据库">
                <el-select v-model="editForm.database" clearable placeholder="请选择" style="width: 100%">
                  <el-option v-for="item in options.database" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="大前端">
                <el-select v-model="editForm.frontend" clearable placeholder="请选择" style="width: 100%">
                  <el-option v-for="item in options.frontend" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="16">
            <el-col :span="8">
              <el-form-item label="前端框架">
                <el-select v-model="editForm.frontendFramework" clearable placeholder="请选择" style="width: 100%">
                  <el-option v-for="item in options.frontendFramework" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="终端类型">
                <el-select v-model="editForm.terminal" clearable placeholder="请选择" style="width: 100%">
                  <el-option v-for="item in options.terminal" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="开发日期">
                <el-date-picker v-model="editForm.devDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-group">
          <div class="form-group-title">需求描述</div>
          <el-form-item label-width="0">
            <el-input v-model="editForm.requirement" type="textarea" :rows="3" placeholder="请描述项目需求..." />
          </el-form-item>
        </div>

        <div class="form-group">
          <div class="form-group-title">
            附件
            <span v-if="editForm.attachmentName" class="current-file-tag">当前: {{ editForm.attachmentName }}</span>
          </div>
          <el-upload :auto-upload="false" :on-change="handleEditFileChange" :file-list="editFileList" :limit="1" drag class="simple-upload">
            <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
            <div class="el-upload__text">拖拽文件到此处，或<em>点击上传</em></div>
          </el-upload>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="success" @click="submitDraft" :loading="editLoading" v-if="editForm.status === -1">提交订单</el-button>
        <el-button type="primary" @click="saveEdit" :loading="editLoading">保存</el-button>
      </template>
    </el-dialog>

    <!-- 上传修改弹窗 -->
    <el-dialog v-model="uploadVisible" title="上传修改" width="480px">
      <el-form label-width="80px" class="upload-form">
        <el-form-item label="订单编号">
          <el-input :value="uploadForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="上传文件" class="upload-form-item">
          <el-upload :auto-upload="false" :on-change="handleUploadFileChange" :file-list="uploadFileList" :limit="1" drag class="simple-upload">
            <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
            <div class="el-upload__text">拖拽文件到此处，或<em>点击上传</em></div>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="uploadVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUploadModify" :loading="uploadLoading">提交</el-button>
      </template>
    </el-dialog>

    <!-- 填写价格弹窗 -->
    <el-dialog v-model="priceDialogVisible" title="填写价格" width="360px">
      <el-form label-width="80px">
        <el-form-item label="订单价格">
          <el-input-number v-model="priceInput" :min="0" :precision="2" :step="10" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="priceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSetPrice">确认</el-button>
      </template>
    </el-dialog>

    <!-- 修改状态弹窗 -->
    <el-dialog v-model="statusDialogVisible" title="修改状态" width="360px">
      <el-form label-width="80px">
        <el-form-item label="订单状态">
          <el-select v-model="statusInput" placeholder="选择状态" style="width: 100%">
            <el-option v-for="(label, value) in statusMap" :key="value" :label="label" :value="Number(value)" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmChangeStatus">确认</el-button>
      </template>
    </el-dialog>
  </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, inject } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download, UploadFilled, Edit, Upload, RefreshLeft, Check, MoreFilled } from '@element-plus/icons-vue'
import { orderApi, fileApi } from '../api'

const refreshBalance = inject('refreshBalance', () => {})
const user = JSON.parse(localStorage.getItem('user') || '{}')
const isAdmin = computed(() => user.role === 1)

const loading = ref(false)
const editLoading = ref(false)
const uploadLoading = ref(false)
const orders = ref([])
const total = ref(0)
const editVisible = ref(false)
const uploadVisible = ref(false)
const editFileList = ref([])
const uploadFileList = ref([])
const editFile = ref(null)
const uploadFile = ref(null)

const editForm = reactive({ 
  id: null, title: '', requirement: '', myNo: '', language: '', database: '', 
  frontend: '', frontendFramework: '', terminal: '', devDate: '', attachmentName: '', status: 0
})
const uploadForm = reactive({ id: null, orderNo: '' })
const priceInput = ref(0)
const statusInput = ref(0)

const query = reactive({
  myNo: '', orderNo: '', title: '', language: '', status: null, pageNum: 1, pageSize: 10,
  userId: isAdmin.value ? null : user.id
})

const options = {
  language: ['Java', 'Python', 'C/C++', 'JavaScript', 'Go', 'PHP', 'C#', 'Rust', 'Kotlin', 'Swift'],
  database: ['MySQL', 'PostgreSQL', 'MongoDB', 'Redis', 'Oracle', 'SQLite', 'SQL Server'],
  frontend: ['Web', 'App', '小程序', '桌面应用'],
  frontendFramework: ['Vue', 'React', 'Angular', 'UniApp', 'Flutter', 'React Native', 'Electron'],
  terminal: ['PC端', '移动端', '小程序端']
}

const languageOptions = ['Java', 'Python', 'C/C++', 'JavaScript', 'Go', 'PHP', 'C#']
const statusMap = { '-1': '草稿', 0: '待接单', 1: '已接单', 2: '开发中', 3: '开发完成', 4: '修改完成' }
const statusTagType = { '-1': 'default', 0: 'info', 1: 'warning', 2: 'primary', 3: 'success', 4: 'success' }

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await orderApi.list(query)
    orders.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  Object.assign(query, { myNo: '', orderNo: '', title: '', language: '', status: null, pageNum: 1 })
  loadOrders()
}

const downloadFile = (orderId) => { window.open(fileApi.download(orderId)) }

const editOrder = (row) => {
  Object.assign(editForm, { 
    id: row.id, title: row.title, requirement: row.requirement, myNo: row.myNo,
    language: row.language, database: row.database, frontend: row.frontend,
    frontendFramework: row.frontendFramework, terminal: row.terminal, 
    devDate: row.devDate, attachmentName: row.attachmentName, status: row.status
  })
  editFileList.value = []
  editFile.value = null
  editVisible.value = true
}

const handleEditFileChange = (file) => { editFile.value = file.raw }
const handleUploadFileChange = (file) => { uploadFile.value = file.raw }

const saveEdit = async () => {
  editLoading.value = true
  try {
    if (editFile.value) {
      const formData = new FormData()
      formData.append('id', editForm.id)
      formData.append('title', editForm.title)
      formData.append('requirement', editForm.requirement || '')
      formData.append('myNo', editForm.myNo || '')
      formData.append('language', editForm.language || '')
      formData.append('database', editForm.database || '')
      formData.append('frontend', editForm.frontend || '')
      formData.append('frontendFramework', editForm.frontendFramework || '')
      formData.append('terminal', editForm.terminal || '')
      formData.append('devDate', editForm.devDate || '')
      formData.append('file', editFile.value)
      await orderApi.updateWithFile(formData)
    } else {
      await orderApi.update(editForm)
    }
    ElMessage.success('保存成功')
    editVisible.value = false
    loadOrders()
  } finally {
    editLoading.value = false
  }
}

const submitDraft = async () => {
  if (!editForm.title) {
    ElMessage.warning('请填写订单标题')
    return
  }
  editLoading.value = true
  try {
    // 先保存编辑内容
    if (editFile.value) {
      const formData = new FormData()
      formData.append('id', editForm.id)
      formData.append('title', editForm.title)
      formData.append('requirement', editForm.requirement || '')
      formData.append('myNo', editForm.myNo || '')
      formData.append('language', editForm.language || '')
      formData.append('database', editForm.database || '')
      formData.append('frontend', editForm.frontend || '')
      formData.append('frontendFramework', editForm.frontendFramework || '')
      formData.append('terminal', editForm.terminal || '')
      formData.append('devDate', editForm.devDate || '')
      formData.append('file', editFile.value)
      await orderApi.updateWithFile(formData)
    } else {
      await orderApi.update(editForm)
    }
    // 更新状态为待接单
    await orderApi.updateStatus(editForm.id, 0)
    ElMessage.success('订单提交成功')
    editVisible.value = false
    loadOrders()
  } finally {
    editLoading.value = false
  }
}

const uploadModify = (row) => {
  uploadForm.id = row.id
  uploadForm.orderNo = row.orderNo
  uploadFileList.value = []
  uploadFile.value = null
  uploadVisible.value = true
}

const submitUploadModify = async () => {
  if (!uploadFile.value) {
    ElMessage.warning('请选择要上传的文件')
    return
  }
  uploadLoading.value = true
  try {
    const formData = new FormData()
    formData.append('id', uploadForm.id)
    formData.append('file', uploadFile.value)
    await orderApi.uploadModify(formData)
    ElMessage.success('上传成功')
    uploadVisible.value = false
    loadOrders()
  } finally {
    uploadLoading.value = false
  }
}

const withdrawOrder = async (row) => {
  await ElMessageBox.confirm('确定撤回该订单？', '提示')
  await orderApi.delete(row.id)
  ElMessage.success('撤回成功')
  loadOrders()
}

const acceptOrder = async (row) => {
  await orderApi.updateStatus(row.id, 1)
  ElMessage.success('接单成功')
  loadOrders()
}

const setPrice = async (row) => {
  await orderApi.updatePrice(row.id, priceInput.value)
  ElMessage.success('价格设置成功')
  loadOrders()
  if (row.settled === 1) {
    refreshBalance() // 如果已结算，价格变化会影响余额
  }
}

const toggleSettle = async (row) => {
  await orderApi.updateSettled(row.id, row.settled === 1 ? 0 : 1)
  ElMessage.success('操作成功')
  loadOrders()
  refreshBalance() // 结算状态变化时刷新余额
}

const changeStatus = async (row) => {
  await orderApi.updateStatus(row.id, statusInput.value)
  ElMessage.success('状态更新成功')
  loadOrders()
}

const currentAdminRow = ref(null)
const priceDialogVisible = ref(false)
const statusDialogVisible = ref(false)

const handleRowCommand = (command, row) => {
  currentAdminRow.value = row
  switch (command) {
    case 'upload':
      uploadModify(row)
      break
    case 'withdraw':
      withdrawOrder(row)
      break
    case 'accept':
      acceptOrder(row)
      break
    case 'price':
      priceInput.value = row.totalPrice || 0
      priceDialogVisible.value = true
      break
    case 'settle':
      toggleSettle(row)
      break
    case 'status':
      statusInput.value = row.status
      statusDialogVisible.value = true
      break
  }
}

const handleAdminCommand = (command, row) => {
  handleRowCommand(command, row)
}

const confirmSetPrice = async () => {
  if (currentAdminRow.value) {
    await setPrice(currentAdminRow.value)
    priceDialogVisible.value = false
  }
}

const confirmChangeStatus = async () => {
  if (currentAdminRow.value) {
    await changeStatus(currentAdminRow.value)
    statusDialogVisible.value = false
  }
}

onMounted(loadOrders)
</script>

<style scoped>
.order-list-wrapper {
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.order-list {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.header-icon {
  padding: 8px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 10px;
}

.header-icon path {
  fill: white;
}

.header-subtitle {
  font-size: 13px;
  color: #909399;
}

.filter-form {
  background: #fafbfc;
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
}

:deep(.el-form-item) {
  margin-bottom: 0;
  margin-right: 16px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

/* 需求列样式 */
.requirement-cell {
  display: flex;
  align-items: center;
  gap: 6px;
  width: 100%;
}

.requirement-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #606266;
  font-size: 13px;
  line-height: 1.4;
}

.attachment-btn {
  flex-shrink: 0;
  padding: 2px 4px !important;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.action-icon-btn {
  width: 30px;
  height: 30px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  background: #fff;
  color: #606266;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  transition: all 0.3s;
}

.action-icon-btn:hover {
  color: #667eea;
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.08);
}

/* 编辑表单 */
.edit-form {
  max-height: 60vh;
  overflow-y: auto;
}

.edit-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.edit-form :deep(.el-row) {
  margin-bottom: 4px;
}

.edit-form :deep(.el-row:last-child) {
  margin-bottom: 0;
}

.form-group {
  background: #fafbfc;
  border-radius: 12px;
  padding: 16px 20px;
  margin-bottom: 16px;
  border: 1px solid #ebeef5;
}

.form-group:last-child {
  margin-bottom: 0;
}

.form-group-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 14px;
  padding-bottom: 10px;
  border-bottom: 1px dashed #e4e7ed;
  display: flex;
  align-items: center;
  gap: 8px;
}

.current-file-tag {
  font-size: 12px;
  font-weight: 400;
  color: #667eea;
  margin-left: auto;
}

/* 上传弹窗表单 */
.upload-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.upload-form-item {
  margin-top: 4px;
}

/* 上传样式 */
.simple-upload {
  width: 100%;
}

.simple-upload :deep(.el-upload-dragger) {
  border-radius: 12px;
  padding: 30px 20px;
  border: 2px dashed #dcdfe6;
  background: #fafbfc;
  transition: all 0.3s;
}

.simple-upload :deep(.el-upload-dragger:hover) {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.02);
}

.simple-upload :deep(.el-icon--upload) {
  font-size: 36px;
  color: #c0c4cc;
  margin-bottom: 10px;
}

.simple-upload :deep(.el-upload__text) {
  color: #606266;
  font-size: 14px;
}

.simple-upload :deep(.el-upload__text em) {
  color: #667eea;
  font-style: normal;
}

/* 表格样式 */
.order-table {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-table) {
  font-size: 13px;
  border-radius: 12px;
}

:deep(.el-table th.el-table__cell) {
  background: linear-gradient(135deg, #f5f7fa, #e8eaed) !important;
  font-weight: 600;
  color: #303133;
  padding: 12px 0;
  font-size: 13px;
}

:deep(.el-table td.el-table__cell) {
  padding: 12px 0;
  color: #606266;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: #fafbfc;
}

:deep(.el-table tr:hover > td) {
  background: rgba(102, 126, 234, 0.04) !important;
}

:deep(.el-table .cell) {
  padding: 0 10px;
  line-height: 1.5;
}

:deep(.el-tag) {
  border-radius: 6px;
}

/* 下拉菜单样式 */
:deep(.el-dropdown-menu__item) {
  font-size: 13px;
  padding: 10px 16px;
  color: #606266;
}

:deep(.el-dropdown-menu__item:hover) {
  background: rgba(102, 126, 234, 0.08);
  color: #667eea;
}

:deep(.el-dropdown-menu__item .el-icon) {
  margin-right: 8px;
  color: #909399;
}

/* 弹窗样式 */
:deep(.el-dialog) {
  border-radius: 16px;
}

:deep(.el-dialog__header) {
  padding: 18px 24px;
  border-bottom: 1px solid #ebeef5;
  margin-right: 0;
}

:deep(.el-dialog__title) {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-dialog__headerbtn) {
  top: 18px;
  right: 18px;
}

:deep(.el-dialog__body) {
  padding: 20px 24px;
}

:deep(.el-dialog__footer) {
  padding: 14px 24px;
  border-top: 1px solid #ebeef5;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  border-radius: 8px;
}

:deep(.el-button--primary:hover) {
  opacity: 0.9;
}

:deep(.el-button--success) {
  border-radius: 8px;
}

:deep(.el-button--default) {
  border-radius: 8px;
}

/* 输入框样式 */
:deep(.el-input__wrapper),
:deep(.el-select__wrapper),
:deep(.el-textarea__inner) {
  border-radius: 8px;
}

/* 分页样式 */
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 20px 0 4px;
  margin-top: 8px;
}

:deep(.el-pagination) {
  font-size: 13px;
}

:deep(.el-pagination .el-pager li.is-active) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 6px;
}

:deep(.el-pagination .el-pager li) {
  border-radius: 6px;
}
</style>
