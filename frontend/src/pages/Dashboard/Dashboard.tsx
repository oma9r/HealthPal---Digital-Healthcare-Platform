import { useAuth } from '../../contexts/AuthContext'
import { useQuery } from '@tanstack/react-query'
import { treatmentService } from '../../services/api/treatmentService'
import { consultationService } from '../../services/api/consultationService'
import { donationService } from '../../services/api/donationService'
import { healthAlertService } from '../../services/api/healthAlertService'
import { Heart, Calendar, DollarSign, AlertCircle, TrendingUp } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function Dashboard() {
  const { user } = useAuth()
  const role = user?.role

  const { data: treatments } = useQuery({
    queryKey: ['treatments', 'active'],
    queryFn: () => treatmentService.getActive(),
    enabled: ['PATIENT', 'DOCTOR', 'ADMIN', 'DONOR'].includes(role || ''),
  })

  const { data: consultations } = useQuery({
    queryKey: ['consultations'],
    queryFn: () => consultationService.getAll(),
    enabled: ['PATIENT', 'DOCTOR', 'ADMIN'].includes(role || ''),
  })

  const { data: donations } = useQuery({
    queryKey: ['donations'],
    queryFn: () => donationService.getAll(),
    enabled: ['DONOR', 'ADMIN', 'NGO'].includes(role || ''),
  })

  const { data: alerts } = useQuery({
    queryKey: ['health-alerts'],
    queryFn: () => healthAlertService.getAll(),
  })

  const stats = [
    {
      title: 'Active Treatments',
      value: treatments?.length || 0,
      icon: Heart,
      color: 'text-red-600',
      bgColor: 'bg-red-50',
      link: '/treatments',
    },
    {
      title: 'Upcoming Consultations',
      value: consultations?.filter((c: any) => c.status === 'SCHEDULED' || c.status === 'CONFIRMED').length || 0,
      icon: Calendar,
      color: 'text-blue-600',
      bgColor: 'bg-blue-50',
      link: '/consultations',
    },
    {
      title: 'Total Donations',
      value: donations?.length || 0,
      icon: DollarSign,
      color: 'text-green-600',
      bgColor: 'bg-green-50',
      link: '/donations',
    },
    {
      title: 'Health Alerts',
      value: alerts?.filter((a: any) => a.active).length || 0,
      icon: AlertCircle,
      color: 'text-orange-600',
      bgColor: 'bg-orange-50',
      link: '/health-alerts',
    },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900 mb-2">
          Welcome back, {user?.fullName}!
        </h1>
        <p className="text-gray-600">Here's what's happening with HealthPal today.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat) => {
          const Icon = stat.icon
          return (
            <Link
              key={stat.title}
              to={stat.link}
              className="card hover:shadow-lg transition-shadow duration-200"
            >
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">{stat.title}</p>
                  <p className="text-3xl font-bold text-gray-900">{stat.value}</p>
                </div>
                <div className={`${stat.bgColor} p-3 rounded-lg`}>
                  <Icon className={`w-6 h-6 ${stat.color}`} />
                </div>
              </div>
            </Link>
          )
        })}
      </div>

      {role === 'PATIENT' && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="card">
            <h2 className="text-xl font-bold text-gray-900 mb-4">My Treatments</h2>
            {treatments && treatments.length > 0 ? (
              <div className="space-y-3">
                {treatments.slice(0, 3).map((treatment: any) => (
                  <Link
                    key={treatment.treatmentId}
                    to={`/treatments/${treatment.treatmentId}`}
                    className="block p-4 border border-gray-200 rounded-lg hover:border-healthcare-primary transition-colors"
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <p className="font-medium text-gray-900">{treatment.description}</p>
                        <p className="text-sm text-gray-600 mt-1">
                          Goal: ${treatment.goalAmount?.toLocaleString() || '0'}
                        </p>
                      </div>
                      <span className={`px-2 py-1 rounded text-xs font-medium ${
                        treatment.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                        treatment.status === 'MET' ? 'bg-blue-100 text-blue-800' :
                        'bg-gray-100 text-gray-800'
                      }`}>
                        {treatment.status}
                      </span>
                    </div>
                  </Link>
                ))}
              </div>
            ) : (
              <p className="text-gray-500">No active treatments</p>
            )}
          </div>

          <div className="card">
            <h2 className="text-xl font-bold text-gray-900 mb-4">Recent Consultations</h2>
            {consultations && consultations.length > 0 ? (
              <div className="space-y-3">
                {consultations.slice(0, 3).map((consultation: any) => (
                  <Link
                    key={consultation.id}
                    to={`/consultations/${consultation.id}`}
                    className="block p-4 border border-gray-200 rounded-lg hover:border-healthcare-primary transition-colors"
                  >
                    <p className="font-medium text-gray-900">
                      {consultation.doctor?.user?.fullName || 'Doctor TBD'}
                    </p>
                    <p className="text-sm text-gray-600 mt-1">
                      {new Date(consultation.scheduledTime).toLocaleDateString()}
                    </p>
                  </Link>
                ))}
              </div>
            ) : (
              <p className="text-gray-500">No consultations scheduled</p>
            )}
          </div>
        </div>
      )}

      {alerts && alerts.filter((a: any) => a.active).length > 0 && (
        <div className="card bg-orange-50 border-orange-200">
          <h2 className="text-xl font-bold text-gray-900 mb-4 flex items-center">
            <AlertCircle className="w-5 h-5 text-orange-600 mr-2" />
            Active Health Alerts
          </h2>
          <div className="space-y-3">
            {alerts.filter((a: any) => a.active).slice(0, 3).map((alert: any) => (
              <div key={alert.id} className="p-4 bg-white rounded-lg">
                <p className="font-medium text-gray-900">{alert.title}</p>
                <p className="text-sm text-gray-600 mt-1">{alert.description}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

