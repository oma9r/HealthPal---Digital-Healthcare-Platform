import apiClient from './apiClient'

export interface HealthAlert {
  id: number
  title: string
  description: string
  severity: string
  active: boolean
  createdAt: string
}

export const healthAlertService = {
  getAll: async (): Promise<HealthAlert[]> => {
    const response = await apiClient.get('/health-alerts')
    return response.data
  },

  create: async (data: { title: string; description: string; severity?: string }): Promise<HealthAlert> => {
    const response = await apiClient.post('/health-alerts', data)
    return response.data
  },
}

