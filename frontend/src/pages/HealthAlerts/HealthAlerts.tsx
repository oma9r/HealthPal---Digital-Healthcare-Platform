import { useQuery } from '@tanstack/react-query'
import { healthAlertService } from '../../services/api/healthAlertService'
import { AlertCircle, AlertTriangle, Info } from 'lucide-react'
import { format } from 'date-fns'
import { useAuth } from '../../contexts/AuthContext'

export default function HealthAlerts() {
  const { user } = useAuth()

  const { data: alerts, isLoading } = useQuery({
    queryKey: ['health-alerts'],
    queryFn: () => healthAlertService.getAll(),
  })

  const activeAlerts = alerts?.filter((a: any) => a.active) || []
  const inactiveAlerts = alerts?.filter((a: any) => !a.active) || []

  const getSeverityIcon = (severity?: string) => {
    switch (severity?.toUpperCase()) {
      case 'HIGH':
      case 'CRITICAL':
        return <AlertTriangle className="w-5 h-5 text-red-600" />
      case 'MEDIUM':
        return <AlertCircle className="w-5 h-5 text-orange-600" />
      default:
        return <Info className="w-5 h-5 text-blue-600" />
    }
  }

  const getSeverityColor = (severity?: string) => {
    switch (severity?.toUpperCase()) {
      case 'HIGH':
      case 'CRITICAL':
        return 'bg-red-50 border-red-200'
      case 'MEDIUM':
        return 'bg-orange-50 border-orange-200'
      default:
        return 'bg-blue-50 border-blue-200'
    }
  }

  if (isLoading) {
    return <div>Loading health alerts...</div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Health Alerts</h1>
        <p className="text-gray-600 mt-1">Stay informed about public health alerts</p>
      </div>

      {activeAlerts.length > 0 && (
        <div>
          <h2 className="text-xl font-bold text-gray-900 mb-4">Active Alerts</h2>
          <div className="space-y-4">
            {activeAlerts.map((alert: any) => (
              <div
                key={alert.id}
                className={`card ${getSeverityColor(alert.severity)} border-2`}
              >
                <div className="flex items-start space-x-4">
                  <div className="flex-shrink-0 mt-1">
                    {getSeverityIcon(alert.severity)}
                  </div>
                  <div className="flex-1">
                    <div className="flex items-center justify-between mb-2">
                      <h3 className="text-lg font-bold text-gray-900">{alert.title}</h3>
                      {alert.severity && (
                        <span className="px-3 py-1 bg-white rounded-full text-xs font-medium">
                          {alert.severity}
                        </span>
                      )}
                    </div>
                    <p className="text-gray-700 mb-3">{alert.description}</p>
                    <p className="text-xs text-gray-500">
                      Posted: {format(new Date(alert.createdAt), 'PPP')}
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {inactiveAlerts.length > 0 && (
        <div>
          <h2 className="text-xl font-bold text-gray-900 mb-4">Past Alerts</h2>
          <div className="space-y-3">
            {inactiveAlerts.map((alert: any) => (
              <div
                key={alert.id}
                className="card border border-gray-200"
              >
                <div className="flex items-start justify-between">
                  <div>
                    <h3 className="font-medium text-gray-900">{alert.title}</h3>
                    <p className="text-sm text-gray-600 mt-1 line-clamp-2">
                      {alert.description}
                    </p>
                    <p className="text-xs text-gray-500 mt-2">
                      {format(new Date(alert.createdAt), 'PPP')}
                    </p>
                  </div>
                  <span className="px-3 py-1 bg-gray-100 text-gray-600 rounded-full text-xs font-medium">
                    Inactive
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {(!activeAlerts.length && !inactiveAlerts.length) && (
        <div className="card text-center py-12">
          <AlertCircle className="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <p className="text-gray-500">No health alerts available</p>
        </div>
      )}
    </div>
  )
}

