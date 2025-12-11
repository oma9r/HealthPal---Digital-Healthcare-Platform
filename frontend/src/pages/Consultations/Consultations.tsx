import { useQuery } from '@tanstack/react-query'
import { consultationService } from '../../services/api/consultationService'
import { Link } from 'react-router-dom'
import { Calendar, Video, Phone, MessageSquare, Plus } from 'lucide-react'
import { format } from 'date-fns'

export default function Consultations() {
  const { data: consultations, isLoading } = useQuery({
    queryKey: ['consultations'],
    queryFn: () => consultationService.getAll(),
  })

  const getModeIcon = (mode: string) => {
    switch (mode?.toUpperCase()) {
      case 'VIDEO':
        return <Video className="w-4 h-4" />
      case 'AUDIO':
        return <Phone className="w-4 h-4" />
      case 'CHAT':
        return <MessageSquare className="w-4 h-4" />
      default:
        return <Calendar className="w-4 h-4" />
    }
  }

  const getStatusColor = (status: string) => {
    switch (status?.toUpperCase()) {
      case 'SCHEDULED':
      case 'CONFIRMED':
        return 'bg-blue-100 text-blue-800'
      case 'COMPLETED':
        return 'bg-green-100 text-green-800'
      case 'CANCELLED':
        return 'bg-red-100 text-red-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  if (isLoading) {
    return <div>Loading consultations...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Consultations</h1>
          <p className="text-gray-600 mt-1">Manage your medical consultations</p>
        </div>
        <Link to="/consultations/new" className="btn-primary inline-flex items-center">
          <Plus className="w-5 h-5 mr-2" />
          Book Consultation
        </Link>
      </div>

      <div className="grid grid-cols-1 gap-4">
        {consultations && consultations.length > 0 ? (
          consultations.map((consultation: any) => (
            <Link
              key={consultation.id}
              to={`/consultations/${consultation.id}`}
              className="card hover:shadow-lg transition-shadow duration-200"
            >
              <div className="flex justify-between items-start">
                <div className="flex-1">
                  <div className="flex items-center space-x-3 mb-2">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(consultation.status)}`}>
                      {consultation.status}
                    </span>
                    <div className="flex items-center text-gray-600">
                      {getModeIcon(consultation.mode)}
                      <span className="ml-2 text-sm">{consultation.mode}</span>
                    </div>
                    {consultation.lowBandwidth && (
                      <span className="px-2 py-1 bg-yellow-100 text-yellow-800 rounded text-xs">
                        Low Bandwidth
                      </span>
                    )}
                  </div>
                  
                  <p className="font-medium text-gray-900 mb-1">
                    {consultation.doctor?.user?.fullName || 'Doctor TBD'}
                  </p>
                  
                  <p className="text-sm text-gray-600 mb-2">
                    {format(new Date(consultation.scheduledTime), 'PPp')}
                  </p>
                  
                  {consultation.notes && (
                    <p className="text-sm text-gray-500 line-clamp-2">{consultation.notes}</p>
                  )}
                </div>
              </div>
            </Link>
          ))
        ) : (
          <div className="card text-center py-12">
            <Calendar className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500 mb-4">No consultations found</p>
            <Link to="/consultations/new" className="btn-primary inline-flex items-center">
              <Plus className="w-5 h-5 mr-2" />
              Book Your First Consultation
            </Link>
          </div>
        )}
      </div>
    </div>
  )
}

