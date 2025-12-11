import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './contexts/AuthContext'
import Layout from './components/Layout/Layout'
import Login from './pages/Auth/Login'
import Register from './pages/Auth/Register'
import Dashboard from './pages/Dashboard/Dashboard'
import Consultations from './pages/Consultations/Consultations'
import ConsultationDetail from './pages/Consultations/ConsultationDetail'
import Treatments from './pages/Treatments/Treatments'
import TreatmentDetail from './pages/Treatments/TreatmentDetail'
import Donations from './pages/Donations/Donations'
import Equipment from './pages/Inventory/Equipment'
import Supplies from './pages/Inventory/Supplies'
import NGO from './pages/NGO/NGO'
import MedicalRecords from './pages/MedicalRecords/MedicalRecords'
import HealthAlerts from './pages/HealthAlerts/HealthAlerts'
import Profile from './pages/Profile/Profile'
import AdminDashboard from './pages/Admin/AdminDashboard'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isLoading } = useAuth()
  
  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>
  }
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }
  
  return <>{children}</>
}

function AppRoutes() {
  const { user } = useAuth()
  
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/register/:role" element={<Register />} />
      
      <Route path="/" element={
        <ProtectedRoute>
          <Layout />
        </ProtectedRoute>
      }>
        <Route index element={<Dashboard />} />
        <Route path="consultations" element={<Consultations />} />
        <Route path="consultations/:id" element={<ConsultationDetail />} />
        <Route path="treatments" element={<Treatments />} />
        <Route path="treatments/:id" element={<TreatmentDetail />} />
        <Route path="donations" element={<Donations />} />
        <Route path="equipment" element={<Equipment />} />
        <Route path="supplies" element={<Supplies />} />
        <Route path="ngo" element={<NGO />} />
        <Route path="medical-records" element={<MedicalRecords />} />
        <Route path="health-alerts" element={<HealthAlerts />} />
        <Route path="profile" element={<Profile />} />
        
        {user?.role === 'ADMIN' && (
          <Route path="admin" element={<AdminDashboard />} />
        )}
      </Route>
      
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  )
}

export default App

