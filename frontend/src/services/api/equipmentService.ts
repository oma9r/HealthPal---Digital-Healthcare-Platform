import apiClient from './apiClient'

export interface Equipment {
  equipmentId: number
  name: string
  description?: string
  condition?: string
  location?: string
  available: boolean
  ngo?: any
}

export const equipmentService = {
  getAll: async (available?: boolean): Promise<Equipment[]> => {
    const params = available !== undefined ? { available } : {}
    const response = await apiClient.get('/equipment', { params })
    return response.data
  },

  getById: async (id: number): Promise<Equipment> => {
    const response = await apiClient.get(`/equipment/${id}`)
    return response.data
  },

  search: async (location?: string, condition?: string): Promise<Equipment[]> => {
    const params = { location, condition }
    const response = await apiClient.get('/equipment/search', { params })
    return response.data
  },

  create: async (data: Partial<Equipment>): Promise<Equipment> => {
    const response = await apiClient.post('/equipment', data)
    return response.data
  },

  update: async (id: number, data: Partial<Equipment>): Promise<Equipment> => {
    const response = await apiClient.put(`/equipment/${id}`, data)
    return response.data
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/equipment/${id}`)
  },

  toggleAvailability: async (id: number): Promise<Equipment> => {
    const response = await apiClient.put(`/equipment/${id}/toggle-availability`)
    return response.data
  },
}

