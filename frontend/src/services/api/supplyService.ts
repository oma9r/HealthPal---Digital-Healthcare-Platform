import apiClient from './apiClient'

export interface Supply {
  supplyId: number
  name: string
  category?: string
  quantity: number
  expiryDate?: string
  location?: string
  ngo?: any
}

export const supplyService = {
  getAll: async (): Promise<Supply[]> => {
    const response = await apiClient.get('/supplies')
    return response.data
  },

  getById: async (id: number): Promise<Supply> => {
    const response = await apiClient.get(`/supplies/${id}`)
    return response.data
  },

  getByCategory: async (category: string): Promise<Supply[]> => {
    const response = await apiClient.get(`/supplies/category/${category}`)
    return response.data
  },

  getExpiring: async (daysAhead: number = 30): Promise<Supply[]> => {
    const response = await apiClient.get('/supplies/expiring', {
      params: { daysAhead },
    })
    return response.data
  },

  create: async (data: Partial<Supply>): Promise<Supply> => {
    const response = await apiClient.post('/supplies', data)
    return response.data
  },

  update: async (id: number, data: Partial<Supply>): Promise<Supply> => {
    const response = await apiClient.put(`/supplies/${id}`, data)
    return response.data
  },

  updateQuantity: async (id: number, change: number): Promise<Supply> => {
    const response = await apiClient.put(`/supplies/${id}/quantity`, null, {
      params: { change },
    })
    return response.data
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/supplies/${id}`)
  },
}

