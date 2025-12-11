import { useAuth } from '../../contexts/AuthContext'
import { User, Mail, Phone, Calendar, MapPin } from 'lucide-react'
import { useState } from 'react'

export default function Profile() {
  const { user } = useAuth()
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState({
    fullName: user?.fullName || '',
    email: user?.email || '',
    phoneNumber: '',
    dateOfBirth: '',
    address: '',
  })

  const handleSave = () => {
    // TODO: Implement profile update
    setIsEditing(false)
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">My Profile</h1>
        <p className="text-gray-600 mt-1">Manage your personal information</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <div className="card">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-xl font-bold text-gray-900">Personal Information</h2>
              {!isEditing ? (
                <button onClick={() => setIsEditing(true)} className="btn-secondary">
                  Edit Profile
                </button>
              ) : (
                <div className="space-x-2">
                  <button onClick={() => setIsEditing(false)} className="btn-secondary">
                    Cancel
                  </button>
                  <button onClick={handleSave} className="btn-primary">
                    Save Changes
                  </button>
                </div>
              )}
            </div>

            <div className="space-y-4">
              <div>
                <label className="label flex items-center">
                  <User className="w-4 h-4 mr-2" />
                  Full Name
                </label>
                {isEditing ? (
                  <input
                    type="text"
                    value={formData.fullName}
                    onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                    className="input-field"
                  />
                ) : (
                  <p className="text-gray-900">{user?.fullName}</p>
                )}
              </div>

              <div>
                <label className="label flex items-center">
                  <Mail className="w-4 h-4 mr-2" />
                  Email
                </label>
                {isEditing ? (
                  <input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="input-field"
                  />
                ) : (
                  <p className="text-gray-900">{user?.email}</p>
                )}
              </div>

              <div>
                <label className="label flex items-center">
                  <Phone className="w-4 h-4 mr-2" />
                  Phone Number
                </label>
                {isEditing ? (
                  <input
                    type="tel"
                    value={formData.phoneNumber}
                    onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                    className="input-field"
                  />
                ) : (
                  <p className="text-gray-900">{formData.phoneNumber || 'Not provided'}</p>
                )}
              </div>

              <div>
                <label className="label flex items-center">
                  <Calendar className="w-4 h-4 mr-2" />
                  Date of Birth
                </label>
                {isEditing ? (
                  <input
                    type="date"
                    value={formData.dateOfBirth}
                    onChange={(e) => setFormData({ ...formData, dateOfBirth: e.target.value })}
                    className="input-field"
                  />
                ) : (
                  <p className="text-gray-900">{formData.dateOfBirth || 'Not provided'}</p>
                )}
              </div>

              <div>
                <label className="label flex items-center">
                  <MapPin className="w-4 h-4 mr-2" />
                  Address
                </label>
                {isEditing ? (
                  <textarea
                    value={formData.address}
                    onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                    className="input-field"
                    rows={3}
                  />
                ) : (
                  <p className="text-gray-900">{formData.address || 'Not provided'}</p>
                )}
              </div>
            </div>
          </div>
        </div>

        <div className="lg:col-span-1">
          <div className="card">
            <div className="text-center">
              <div className="w-24 h-24 bg-healthcare-primary rounded-full flex items-center justify-center text-white mx-auto mb-4">
                <User className="w-12 h-12" />
              </div>
              <h3 className="text-lg font-bold text-gray-900">{user?.fullName}</h3>
              <p className="text-sm text-gray-600 capitalize mt-1">{user?.role?.toLowerCase()}</p>
            </div>

            <div className="mt-6 pt-6 border-t">
              <div className="space-y-3">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Account Status</span>
                  <span className={`font-medium ${
                    user?.verified ? 'text-green-600' : 'text-yellow-600'
                  }`}>
                    {user?.verified ? 'Verified' : 'Pending'}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Member Since</span>
                  <span className="font-medium text-gray-900">2024</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

