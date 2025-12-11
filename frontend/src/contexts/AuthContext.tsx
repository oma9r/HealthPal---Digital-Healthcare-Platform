import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import { authService } from '../services/api/authService'

interface User {
  userId: number
  fullName: string
  email: string
  role: 'PATIENT' | 'DOCTOR' | 'DONOR' | 'NGO' | 'ADMIN'
  verified: boolean
}

interface AuthContextType {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  register: (data: any, role: string) => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')
    
    if (storedToken && storedUser) {
      setToken(storedToken)
      setUser(JSON.parse(storedUser))
    }
    setIsLoading(false)
  }, [])

  const login = async (email: string, password: string) => {
    try {
      const response = await authService.login(email, password)
      const [jwtToken, role] = response.split('\t')
      
      setToken(jwtToken)
      localStorage.setItem('token', jwtToken)
      
      // Fetch user details
      const userData = await authService.getCurrentUser(jwtToken)
      const userObj: User = {
        userId: userData.userId,
        fullName: userData.fullName,
        email: userData.email,
        role: role as User['role'],
        verified: userData.verified || false,
      }
      
      setUser(userObj)
      localStorage.setItem('user', JSON.stringify(userObj))
    } catch (error) {
      throw error
    }
  }

  const register = async (data: any, role: string) => {
    try {
      await authService.register(data, role)
      // Auto-login after registration
      if (data.user?.email && data.user?.passwordHash) {
        await login(data.user.email, data.user.passwordHash)
      }
    } catch (error) {
      throw error
    }
  }

  const logout = () => {
    setToken(null)
    setUser(null)
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!token && !!user,
        isLoading,
        login,
        logout,
        register,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

