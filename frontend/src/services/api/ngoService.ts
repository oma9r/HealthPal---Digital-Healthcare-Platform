import apiClient from './apiClient'

export interface NGO {
  ngoId: number
  user: any
  name: string
  contactInfo?: string
  verified: boolean
}

export const ngoService = {
  getAll: async (verified?: boolean): Promise<NGO[]> => {
    const params = verified !== undefined ? { verified } : {}
    const response = await apiClient.get('/ngo', { params })
    return response.data
  },

  getById: async (id: number): Promise<NGO> => {
    const response = await apiClient.get(`/ngo/${id}`)
    return response.data
  },

  register: async (data: Partial<NGO>): Promise<NGO> => {
    const response = await apiClient.post('/ngo/register', data)
    return response.data
  },

  update: async (id: number, data: Partial<NGO>): Promise<NGO> => {
    const response = await apiClient.put(`/ngo/${id}`, data)
    return response.data
  },

  verify: async (id: number): Promise<NGO> => {
    const response = await apiClient.put(`/ngo/${id}/verify`)
    return response.data
  },

  getEquipment: async (ngoId: number): Promise<any[]> => {
    const response = await apiClient.get(`/ngo/${ngoId}/equipment`)
    return response.data
  },

  getSupplies: async (ngoId: number): Promise<any[]> => {
    const response = await apiClient.get(`/ngo/${ngoId}/supplies`)
    return response.data
  },
}

