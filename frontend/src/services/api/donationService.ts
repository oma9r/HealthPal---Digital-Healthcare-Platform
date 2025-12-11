import apiClient from './apiClient'

export interface Donation {
  donationId: number
  donor?: any
  ngo?: any
  treatment?: any
  equipment?: any
  supply?: any
  amount: number
  donationType: 'MONEY' | 'EQUIPMENT' | 'SUPPLIES'
  paymentStatus: 'PENDING' | 'COMPLETED' | 'REFUNDED'
  dateDonated: string
  notes?: string
}

export const donationService = {
  getAll: async (params?: {
    donorId?: number
    treatmentId?: number
    status?: string
  }): Promise<Donation[]> => {
    const response = await apiClient.get('/donations', { params })
    return response.data
  },

  getById: async (id: number): Promise<Donation> => {
    const response = await apiClient.get(`/donations/${id}`)
    return response.data
  },

  create: async (data: Partial<Donation>): Promise<Donation> => {
    const response = await apiClient.post('/donations', data)
    return response.data
  },

  updateStatus: async (id: number, status: string): Promise<Donation> => {
    const response = await apiClient.put(`/donations/${id}/status`, { status })
    return response.data
  },

  getTransparency: async (treatmentId?: number): Promise<Donation[]> => {
    const params = treatmentId ? { treatmentId } : {}
    const response = await apiClient.get('/donations/transparency', { params })
    return response.data
  },
}

