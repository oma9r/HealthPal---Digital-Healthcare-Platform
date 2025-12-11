import apiClient from './apiClient'

export interface Consultation {
  id: number
  patient: any
  doctor: any
  scheduledTime: string
  status: string
  mode: string
  lowBandwidth: boolean
  notes?: string
  createdAt: string
}

export interface ConsultationMessage {
  messageId: number
  consultation: Consultation
  sender: any
  messageText: string
  attachmentUrl?: string
  createdAt: string
}

export const consultationService = {
  getAll: async (): Promise<Consultation[]> => {
    const response = await apiClient.get('/consultations')
    return response.data
  },

  getById: async (id: number): Promise<Consultation> => {
    const response = await apiClient.get(`/consultations/${id}`)
    return response.data
  },

  create: async (data: {
    doctorId: number
    scheduledAt: string
    mode: string
    lowBandwidth: boolean
    notes?: string
  }): Promise<Consultation> => {
    const response = await apiClient.post('/consultations', data)
    return response.data
  },

  getMessages: async (consultationId: number): Promise<ConsultationMessage[]> => {
    const response = await apiClient.get(`/consultations/${consultationId}/messages`)
    return response.data
  },

  sendMessage: async (consultationId: number, messageText: string, attachmentUrl?: string): Promise<ConsultationMessage> => {
    const response = await apiClient.post(`/consultations/${consultationId}/messages`, {
      messageText,
      attachmentUrl,
    })
    return response.data
  },
}

