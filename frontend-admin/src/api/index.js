import request from './request'

export const userApi = {
  login: (data) => request.post('/user/login', data),
  register: (data) => request.post('/user/register', data),
  find: (params) => request.get('/user/find', { params })
}

export const orderApi = {
  submit: (data) => request.post('/order/submit', data, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  list: (params) => request.get('/order/list', { params }),
  get: (id) => request.get(`/order/${id}`),
  update: (data) => request.put('/order/update', data),
  updateWithFile: (data) => request.post('/order/updateWithFile', data, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  uploadModify: (data) => request.post('/order/uploadModify', data, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  delete: (id) => request.delete(`/order/${id}`),
  updateStatus: (id, status) => request.post(`/order/status/${id}`, { status }),
  updatePrice: (id, price) => request.post(`/order/price/${id}`, { price }),
  updateSettled: (id, settled) => request.post(`/order/settle/${id}`, { settled }),
  getBalance: (userId) => request.get(`/order/balance/${userId}`)
}

export const fileApi = {
  download: (orderId) => `/api/file/download/${orderId}`
}
