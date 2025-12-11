import axios, { AxiosInstance, AxiosError } from 'axios'
import { toast } from 'react-toastify'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor to add JWT token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response) {
      const status = error.response.status
      const data = error.response.data as any

      switch (status) {
        case 401:
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          window.location.href = '/login'
          toast.error('Session expired. Please login again.')
          break
        case 403:
          toast.error('You do not have permission to perform this action.')
          break
        case 404:
          toast.error(data?.message || 'Resource not found.')
          break
        case 500:
          toast.error('Server error. Please try again later.')
          break
        default:
          toast.error(data?.message || 'An error occurred.')
      }
    } else if (error.request) {
      toast.error('Network error. Please check your connection.')
    }

    return Promise.reject(error)
  }
)

export default apiClient

