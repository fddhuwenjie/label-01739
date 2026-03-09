<template>
  <div class="create-order-wrapper">
    <el-card class="create-order" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <svg viewBox="0 0 24 24" width="24" height="24" class="header-icon">
              <path fill="currentColor" d="M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z"/>
            </svg>
            <span>录单</span>
          </div>
          <span class="header-subtitle">填写订单信息，提交后等待接单</span>
        </div>
      </template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="order-form">
        <div class="form-section">
          <div class="section-title">
            <span class="section-icon">📋</span>
            基本信息
          </div>
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="订单标题" prop="title">
                <el-input v-model="form.title" placeholder="请输入订单标题" size="large" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="我的编号">
                <el-input v-model="form.myNo" placeholder="选填" size="large" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-section">
          <div class="section-title">
            <span class="section-icon">💻</span>
            技术选型
          </div>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="语言">
                <el-select v-model="form.language" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.language" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="数据库">
                <el-select v-model="form.database" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.database" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="大前端">
                <el-select v-model="form.frontend" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.frontend" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="前端框架">
                <el-select v-model="form.frontendFramework" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.frontendFramework" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="爬虫">
                <el-select v-model="form.crawler" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.crawler" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="大数据">
                <el-select v-model="form.bigData" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.bigData" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="机器学习">
                <el-select v-model="form.machineLearning" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.machineLearning" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="三端">
                <el-select v-model="form.terminal" placeholder="请选择" clearable size="large" style="width: 100%">
                  <el-option v-for="item in options.terminal" :key="item" :label="item" :value="item" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="开发日期" prop="devDate">
                <el-date-picker v-model="form.devDate" type="date" placeholder="选择日期" size="large" style="width: 100%" value-format="YYYY-MM-DD" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-section">
          <div class="section-title">
            <span class="section-icon">👤</span>
            用户信息
          </div>
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="实例名">
                <el-input v-model="form.instanceName" disabled size="large" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="学校名称">
                <el-input v-model="form.schoolName" disabled size="large" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-section">
          <div class="section-title">
            <span class="section-icon">📝</span>
            需求详情
          </div>
          <el-form-item label="需求描述">
            <el-input v-model="form.requirement" type="textarea" :rows="5" placeholder="请详细描述您的需求..." size="large" />
          </el-form-item>
          <el-form-item label="订单附件">
            <el-upload
              class="upload-area"
              drag
              :auto-upload="false"
              :on-change="handleFileChange"
              :file-list="fileList"
              :limit="1"
            >
              <div class="upload-content">
                <svg viewBox="0 0 24 24" width="48" height="48" class="upload-icon">
                  <path fill="currentColor" d="M14,2H6A2,2 0 0,0 4,4V20A2,2 0 0,0 6,22H18A2,2 0 0,0 20,20V8L14,2M18,20H6V4H13V9H18V20M12,12L16,16H13.5V19H10.5V16H8L12,12Z"/>
                </svg>
                <div class="upload-text">拖拽文件到此处或<em>点击上传</em></div>
                <div class="upload-hint">支持各种格式的文件</div>
              </div>
            </el-upload>
          </el-form-item>
        </div>

        <div class="form-actions">
          <el-button type="primary" @click="submitOrder(0)" :loading="loading" size="large" class="submit-btn">
            <svg viewBox="0 0 24 24" width="18" height="18" style="margin-right: 6px;">
              <path fill="currentColor" d="M2,21L23,12L2,3V10L17,12L2,14V21Z"/>
            </svg>
            提交订单
          </el-button>
          <el-button type="info" @click="submitOrder(-1)" :loading="savingDraft" size="large" class="draft-btn">
            <svg viewBox="0 0 24 24" width="18" height="18" style="margin-right: 6px;">
              <path fill="currentColor" d="M17,3H5A2,2 0 0,0 3,5V19A2,2 0 0,0 5,21H19A2,2 0 0,0 21,19V7L17,3M19,19H5V5H16.17L19,7.83V19M12,12A3,3 0 0,0 9,15A3,3 0 0,0 12,18A3,3 0 0,0 15,15A3,3 0 0,0 12,12M6,6H15V10H6V6Z"/>
            </svg>
            暂存草稿
          </el-button>
          <el-button @click="resetForm" size="large" class="reset-btn">
            <svg viewBox="0 0 24 24" width="18" height="18" style="margin-right: 6px;">
              <path fill="currentColor" d="M12,4C14.1,4 16.1,4.8 17.6,6.3C20.7,9.4 20.7,14.5 17.6,17.6C15.8,19.5 13.3,20.2 10.9,19.9L11.4,17.9C13.1,18.1 14.9,17.5 16.2,16.2C18.5,13.9 18.5,10.1 16.2,7.7C15.1,6.6 13.5,6 12,6V10.6L7,5.6L12,0.6V4M6.3,17.6C3.7,15 3.3,11 5.1,7.9L6.6,9.4C5.5,11.6 5.9,14.4 7.8,16.2C8.3,16.7 8.9,17.1 9.6,17.4L9,19.4C8,19 7.1,18.4 6.3,17.6Z"/>
            </svg>
            重置
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { orderApi } from '../api'

const formRef = ref()
const loading = ref(false)
const savingDraft = ref(false)
const fileList = ref([])
const uploadFile = ref(null)

const user = JSON.parse(localStorage.getItem('user') || '{}')

const form = reactive({
  title: '', myNo: '', language: '', database: '', frontend: '', frontendFramework: '',
  crawler: '', bigData: '', machineLearning: '', terminal: '', devDate: '',
  instanceName: user.instanceName || '', schoolName: user.schoolName || '', requirement: ''
})

const options = {
  language: ['Java', 'Python', 'C/C++', 'JavaScript', 'Go', 'PHP', 'C#', 'Rust', 'Kotlin', 'Swift'],
  database: ['MySQL', 'PostgreSQL', 'MongoDB', 'Redis', 'Oracle', 'SQLite', 'SQL Server'],
  frontend: ['Web', 'App', '小程序', '桌面应用'],
  frontendFramework: ['Vue', 'React', 'Angular', 'UniApp', 'Flutter', 'React Native', 'Electron'],
  crawler: ['Scrapy', 'Selenium', 'Puppeteer', 'BeautifulSoup', 'Requests'],
  bigData: ['Hadoop', 'Spark', 'Flink', 'Kafka', 'Hive', 'HBase'],
  machineLearning: ['TensorFlow', 'PyTorch', 'Scikit-learn', 'Keras', 'OpenCV'],
  terminal: ['PC端', '移动端', '小程序端']
}

const rules = {
  title: [{ required: true, message: '请输入订单标题', trigger: 'blur' }],
  devDate: [{ required: true, message: '请选择开发日期', trigger: 'change' }]
}

const handleFileChange = (file) => {
  uploadFile.value = file.raw
}

const submitOrder = async (status) => {
  if (status === 0) {
    await formRef.value.validate()
  }
  
  const isSubmit = status === 0
  if (isSubmit) {
    loading.value = true
  } else {
    savingDraft.value = true
  }
  
  try {
    const formData = new FormData()
    Object.keys(form).forEach(key => {
      if (form[key]) formData.append(key, form[key])
    })
    formData.append('userId', user.id)
    formData.append('status', status)
    if (uploadFile.value) formData.append('file', uploadFile.value)
    await orderApi.submit(formData)
    ElMessage.success(isSubmit ? '订单提交成功' : '草稿保存成功')
    resetForm()
  } finally {
    loading.value = false
    savingDraft.value = false
  }
}

const resetForm = () => {
  formRef.value.resetFields()
  fileList.value = []
  uploadFile.value = null
}
</script>

<style scoped>
.create-order-wrapper {
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.create-order {
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

.form-section {
  background: #fafbfc;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
  transition: all 0.3s;
}

.form-section:hover {
  border-color: #c0c4cc;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px dashed #e4e7ed;
}

.section-icon {
  font-size: 18px;
}

.upload-area {
  width: 100%;
}

.upload-area :deep(.el-upload-dragger) {
  border-radius: 12px;
  border: 2px dashed #dcdfe6;
  background: #fafbfc;
  transition: all 0.3s;
  padding: 40px 20px;
}

.upload-area :deep(.el-upload-dragger:hover) {
  border-color: #667eea;
  background: rgba(102, 126, 234, 0.02);
}

.upload-content {
  text-align: center;
}

.upload-icon {
  color: #c0c4cc;
  margin-bottom: 12px;
}

.upload-text {
  font-size: 14px;
  color: #606266;
}

.upload-text em {
  color: #667eea;
  font-style: normal;
}

.upload-hint {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.submit-btn {
  min-width: 140px;
  height: 44px;
  font-size: 15px;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  transition: all 0.3s;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.draft-btn {
  min-width: 140px;
  height: 44px;
  font-size: 15px;
  border-radius: 10px;
  background: linear-gradient(135deg, #909399, #606266);
  border: none;
  transition: all 0.3s;
}

.draft-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(144, 147, 153, 0.4);
}

.reset-btn {
  min-width: 100px;
  height: 44px;
  font-size: 15px;
  border-radius: 10px;
  transition: all 0.3s;
}

.reset-btn:hover {
  transform: translateY(-2px);
}

:deep(.el-input__wrapper),
:deep(.el-select__wrapper),
:deep(.el-textarea__inner) {
  border-radius: 8px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
