import apiClient from './apiClient'

export const authService = {
  login: async (email: string, password: string) => {
    const response = await apiClient.post('/auth/login', {
      email,
      passwordHash: password,
    })
    return response.data
  },

  register: async (data: any, role: string) => {
    const endpoint = role === 'patient' 
      ? '/auth/register/patient'
      : role === 'doctor'
      ? '/auth/register/doctor'
      : role === 'admin'
      ? '/auth/register/admin'
      : '/auth/register/patient'
    
    const response = await apiClient.post(endpoint, data)
    return response.data
  },

  getCurrentUser: async (token: string) => {
    const response = await apiClient.get('/user/all', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    // This is a placeholder - adjust based on your actual user endpoint
    return response.data[0]
  },
}

