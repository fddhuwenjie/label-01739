import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { 
    path: '/home', 
    name: 'Home', 
    component: () => import('../views/Home.vue'),
    children: [
      { path: '', redirect: '/home/create' },
      { path: 'create', name: 'CreateOrder', component: () => import('../views/CreateOrder.vue') },
      { path: 'list', name: 'OrderList', component: () => import('../views/OrderList.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('user')
  if (to.path !== '/login' && !user) {
    next('/login')
  } else {
    next()
  }
})

export default router
