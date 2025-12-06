import apiClient from './apiClient'

export interface Treatment {
  treatmentId: number
  patient: any
  treatmentType: 'SURGERY' | 'CANCER' | 'DIALYSIS' | 'REHAB'
  description: string
  goalAmount: number
  raisedAmount: number
  startDate?: string
  endDate?: string
  status: 'ACTIVE' | 'MET' | 'CLOSED'
}

export interface TreatmentProgress {
  treatmentId: number
  goalAmount: number
  raisedAmount: number
  progressPercent: number
}

export const treatmentService = {
  getAll: async (status?: string): Promise<Treatment[]> => {
    const params = status ? { status } : {}
    const response = await apiClient.get('/treatments', { params })
    return response.data
  },

  getActive: async (): Promise<Treatment[]> => {
    const response = await apiClient.get('/treatments/active')
    return response.data
  },

  getById: async (id: number): Promise<Treatment> => {
    const response = await apiClient.get(`/treatments/${id}`)
    return response.data
  },

  getProgress: async (id: number): Promise<TreatmentProgress> => {
    const response = await apiClient.get(`/treatments/progress/${id}`)
    return response.data
  },

  getByPatient: async (patientId: number): Promise<Treatment[]> => {
    const response = await apiClient.get(`/treatments/patient/${patientId}`)
    return response.data
  },

  create: async (data: Partial<Treatment>): Promise<Treatment> => {
    const response = await apiClient.post('/treatments', data)
    return response.data
  },

  update: async (id: number, data: Partial<Treatment>): Promise<Treatment> => {
    const response = await apiClient.put(`/treatments/${id}`, data)
    return response.data
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/treatments/${id}`)
  },
}

