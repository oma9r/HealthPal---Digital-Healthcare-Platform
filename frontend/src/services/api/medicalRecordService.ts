import apiClient from './apiClient'

export interface MedicalRecord {
  recordId: number
  patient: any
  recordType: 'DIAGNOSIS' | 'LAB' | 'SURGERY' | 'NOTES'
  description?: string
  documentUrl?: string
  dateOfRecord?: string
  createdAt: string
}

export const medicalRecordService = {
  getByPatient: async (patientId: number, type?: string): Promise<MedicalRecord[]> => {
    const params = type ? { type } : {}
    const response = await apiClient.get(`/medical-records/patient/${patientId}`, { params })
    return response.data
  },

  getById: async (id: number): Promise<MedicalRecord> => {
    const response = await apiClient.get(`/medical-records/${id}`)
    return response.data
  },

  create: async (data: Partial<MedicalRecord>): Promise<MedicalRecord> => {
    const response = await apiClient.post('/medical-records', data)
    return response.data
  },

  update: async (id: number, data: Partial<MedicalRecord>): Promise<MedicalRecord> => {
    const response = await apiClient.put(`/medical-records/${id}`, data)
    return response.data
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/medical-records/${id}`)
  },

  getByType: async (type: string): Promise<MedicalRecord[]> => {
    const response = await apiClient.get(`/medical-records/type/${type}`)
    return response.data
  },
}

