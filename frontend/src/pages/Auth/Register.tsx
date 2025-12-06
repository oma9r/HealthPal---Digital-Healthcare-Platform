import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'
import { toast } from 'react-toastify'
import { Heart, ArrowLeft } from 'lucide-react'

export default function Register() {
  const { role } = useParams<{ role?: string }>()
  const [selectedRole, setSelectedRole] = useState(role || 'patient')
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    phoneNumber: '',
    dateOfBirth: '',
    gender: '',
    address: '',
    medicalSummary: '',
    speciality: '',
    bio: '',
    organization: '',
  })
  const [isLoading, setIsLoading] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    try {
      const registrationData: any = {
        user: {
          fullName: formData.fullName,
          email: formData.email,
          passwordHash: formData.password,
          phoneNumber: formData.phoneNumber,
          role: selectedRole.toUpperCase(),
        },
      }

      if (selectedRole === 'patient') {
        registrationData.dateOfBirth = formData.dateOfBirth
        registrationData.gender = formData.gender
        registrationData.address = formData.address
        registrationData.medicalSummary = formData.medicalSummary
      } else if (selectedRole === 'doctor') {
        registrationData.speciality = formData.speciality
        registrationData.bio = formData.bio
      } else if (selectedRole === 'donor') {
        registrationData.organization = formData.organization
      }

      await register(registrationData, selectedRole)
      toast.success('Registration successful!')
      navigate('/')
    } catch (error: any) {
      toast.error(error.response?.data || 'Registration failed. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 px-4 py-12">
      <div className="max-w-2xl w-full">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-healthcare-primary rounded-full mb-4">
            <Heart className="w-8 h-8 text-white" />
          </div>
          <h1 className="text-4xl font-bold text-gray-900 mb-2">Join HealthPal</h1>
          <p className="text-gray-600">Create your account to get started</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8">
          <div className="mb-6">
            <label className="label">I am a:</label>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
              {['patient', 'doctor', 'donor', 'ngo'].map((r) => (
                <button
                  key={r}
                  type="button"
                  onClick={() => setSelectedRole(r)}
                  className={`px-4 py-2 rounded-lg border-2 transition-colors ${
                    selectedRole === r
                      ? 'border-healthcare-primary bg-blue-50 text-healthcare-primary font-medium'
                      : 'border-gray-200 hover:border-gray-300'
                  }`}
                >
                  {r.charAt(0).toUpperCase() + r.slice(1)}
                </button>
              ))}
            </div>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="label">Full Name *</label>
                <input
                  type="text"
                  required
                  value={formData.fullName}
                  onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                  className="input-field"
                />
              </div>

              <div>
                <label className="label">Email *</label>
                <input
                  type="email"
                  required
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="input-field"
                />
              </div>

              <div>
                <label className="label">Password *</label>
                <input
                  type="password"
                  required
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  className="input-field"
                />
              </div>

              <div>
                <label className="label">Phone Number *</label>
                <input
                  type="tel"
                  required
                  value={formData.phoneNumber}
                  onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                  className="input-field"
                />
              </div>
            </div>

            {selectedRole === 'patient' && (
              <>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="label">Date of Birth</label>
                    <input
                      type="date"
                      value={formData.dateOfBirth}
                      onChange={(e) => setFormData({ ...formData, dateOfBirth: e.target.value })}
                      className="input-field"
                    />
                  </div>

                  <div>
                    <label className="label">Gender</label>
                    <select
                      value={formData.gender}
                      onChange={(e) => setFormData({ ...formData, gender: e.target.value })}
                      className="input-field"
                    >
                      <option value="">Select</option>
                      <option value="male">Male</option>
                      <option value="female">Female</option>
                    </select>
                  </div>

                  <div className="md:col-span-2">
                    <label className="label">Address</label>
                    <input
                      type="text"
                      value={formData.address}
                      onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                      className="input-field"
                    />
                  </div>

                  <div className="md:col-span-2">
                    <label className="label">Medical Summary</label>
                    <textarea
                      value={formData.medicalSummary}
                      onChange={(e) => setFormData({ ...formData, medicalSummary: e.target.value })}
                      className="input-field"
                      rows={3}
                    />
                  </div>
                </div>
              </>
            )}

            {selectedRole === 'doctor' && (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="label">Speciality *</label>
                  <input
                    type="text"
                    required
                    value={formData.speciality}
                    onChange={(e) => setFormData({ ...formData, speciality: e.target.value })}
                    className="input-field"
                    placeholder="e.g., Cardiology, Pediatrics"
                  />
                </div>

                <div className="md:col-span-2">
                  <label className="label">Bio</label>
                  <textarea
                    value={formData.bio}
                    onChange={(e) => setFormData({ ...formData, bio: e.target.value })}
                    className="input-field"
                    rows={3}
                  />
                </div>
              </div>
            )}

            {selectedRole === 'donor' && (
              <div>
                <label className="label">Organization (Optional)</label>
                <input
                  type="text"
                  value={formData.organization}
                  onChange={(e) => setFormData({ ...formData, organization: e.target.value })}
                  className="input-field"
                />
              </div>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className="w-full btn-primary py-3 text-lg disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? 'Creating account...' : 'Create Account'}
            </button>
          </form>

          <div className="mt-6 text-center">
            <a href="/login" className="inline-flex items-center text-sm text-gray-600 hover:text-healthcare-primary">
              <ArrowLeft className="w-4 h-4 mr-1" />
              Back to Login
            </a>
          </div>
        </div>
      </div>
    </div>
  )
}

