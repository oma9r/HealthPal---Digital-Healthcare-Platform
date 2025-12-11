import apiClient from './apiClient'

export interface TranslationResponse {
  originalText: string
  translatedText: string
  sourceLang?: string
  targetLang?: string
}

export const translationService = {
  translate: async (text: string, sourceLang: string, targetLang: string): Promise<TranslationResponse> => {
    const response = await apiClient.post('/translate', {
      text,
      sourceLang,
      targetLang,
    })
    return response.data
  },

  translateArabicToEnglish: async (text: string): Promise<TranslationResponse> => {
    const response = await apiClient.post('/translate/ar-to-en', { text })
    return response.data
  },

  translateEnglishToArabic: async (text: string): Promise<TranslationResponse> => {
    const response = await apiClient.post('/translate/en-to-ar', { text })
    return response.data
  },
}

