<template>
  <el-container class="home-container">
    <el-header class="header">
      <div class="logo">
        <svg viewBox="0 0 40 40" class="logo-icon">
          <defs>
            <linearGradient id="headerGradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" style="stop-color:#fff"/>
              <stop offset="100%" style="stop-color:#e0e7ff"/>
            </linearGradient>
          </defs>
          <circle cx="20" cy="20" r="18" fill="url(#headerGradient)" opacity="0.2"/>
          <path d="M12 20 L18 26 L28 14" stroke="white" stroke-width="3" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <span>录单系统</span>
      </div>
      <div class="user-info">
        <div class="balance-card">
          <svg viewBox="0 0 24 24" class="balance-icon">
            <path fill="currentColor" d="M12,2A10,10 0 0,1 22,12A10,10 0 0,1 12,22A10,10 0 0,1 2,12A10,10 0 0,1 12,2M12,4A8,8 0 0,0 4,12A8,8 0 0,0 12,20A8,8 0 0,0 20,12A8,8 0 0,0 12,4M11,17V16H9V14H13V13H10A1,1 0 0,1 9,12V9A1,1 0 0,1 10,8H11V7H13V8H15V10H11V11H14A1,1 0 0,1 15,12V15A1,1 0 0,1 14,16H13V17H11Z"/>
          </svg>
          <span class="balance-label">余额</span>
          <span class="balance-value">¥{{ balance.toFixed(2) }}</span>
        </div>
        <div class="user-card">
          <div class="avatar">{{ user?.instanceName?.charAt(0) || 'U' }}</div>
          <div class="user-detail">
            <span class="username">{{ user?.instanceName }}</span>
            <span class="school">{{ user?.schoolName }}</span>
          </div>
        </div>
        <el-tag v-if="isAdmin" type="danger" effect="dark" class="admin-tag">管理员</el-tag>
        <el-button type="danger" size="small" round @click="logout" class="logout-btn">
          <svg viewBox="0 0 24 24" width="14" height="14" style="margin-right: 4px;">
            <path fill="currentColor" d="M16,17V14H9V10H16V7L21,12L16,17M14,2A2,2 0 0,1 16,4V6H14V4H5V20H14V18H16V20A2,2 0 0,1 14,22H5A2,2 0 0,1 3,20V4A2,2 0 0,1 5,2H14Z"/>
          </svg>
          退出
        </el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="220px" class="aside">
        <el-menu :default-active="$route.path" router class="side-menu">
          <el-menu-item index="/home/create" class="menu-item">
            <div class="menu-icon">
              <svg viewBox="0 0 24 24" width="20" height="20">
                <path fill="currentColor" d="M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z"/>
              </svg>
            </div>
            <span>录单</span>
          </el-menu-item>
          <el-menu-item index="/home/list" class="menu-item">
            <div class="menu-icon">
              <svg viewBox="0 0 24 24" width="20" height="20">
                <path fill="currentColor" d="M3,4H21V8H3V4M3,10H21V14H3V10M3,16H21V20H3V16Z"/>
              </svg>
            </div>
            <span>订单列表</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <router-view @balance-change="loadBalance" />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, provide } from 'vue'
import { useRouter } from 'vue-router'
import { orderApi } from '../api'

const router = useRouter()
const balance = ref(0)

const user = computed(() => {
  const u = localStorage.getItem('user')
  return u ? JSON.parse(u) : null
})

const isAdmin = computed(() => user.value?.role === 1)

const loadBalance = async () => {
  if (user.value?.id) {
    try {
      const res = await orderApi.getBalance(user.value.id)
      balance.value = res.data || 0
    } catch (e) {
      console.error('获取余额失败', e)
    }
  }
}

provide('refreshBalance', loadBalance)

const logout = () => {
  localStorage.removeItem('user')
  localStorage.removeItem('token')
  router.push('/login')
}

onMounted(loadBalance)
</script>

<style scoped>
.home-container {
  height: 100vh;
  background: #f0f2f5;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 1px;
}

.logo-icon {
  width: 36px;
  height: 36px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.balance-card {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  padding: 8px 16px;
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.balance-icon {
  width: 20px;
  height: 20px;
  color: #ffd700;
}

.balance-label {
  font-size: 12px;
  opacity: 0.8;
}

.balance-value {
  font-size: 16px;
  font-weight: 700;
  color: #ffd700;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.1);
  padding: 6px 12px 6px 6px;
  border-radius: 25px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f093fb, #f5576c);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
}

.user-detail {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.username {
  font-size: 13px;
  font-weight: 600;
}

.school {
  font-size: 11px;
  opacity: 0.8;
}

.admin-tag {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { box-shadow: 0 0 0 0 rgba(245, 108, 108, 0.4); }
  50% { box-shadow: 0 0 0 8px rgba(245, 108, 108, 0); }
}

.logout-btn {
  border: 1px solid rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.1);
  color: white;
  transition: all 0.3s;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.aside {
  background: white;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  padding-top: 20px;
}

.side-menu {
  border-right: none;
}

.menu-item {
  margin: 8px 12px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.menu-item:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
}

.menu-item.is-active {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.menu-item.is-active .menu-icon svg {
  fill: white;
}

.menu-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: rgba(102, 126, 234, 0.1);
  margin-right: 12px;
  transition: all 0.3s;
}

.menu-item.is-active .menu-icon {
  background: rgba(255, 255, 255, 0.2);
}

.main {
  background: linear-gradient(180deg, #f0f2f5 0%, #e8ecf3 100%);
  padding: 24px;
  overflow-y: auto;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}
</style>
