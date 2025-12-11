import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { medicalRecordService } from '../../services/api/medicalRecordService'
import { FileText, Download, Filter } from 'lucide-react'
import { format } from 'date-fns'
import { useAuth } from '../../contexts/AuthContext'

export default function MedicalRecords() {
  const { user } = useAuth()
  const [typeFilter, setTypeFilter] = useState<string>('')

  // For patients, get their own records; for doctors/admin, show all or filtered
  const patientId = user?.role === 'PATIENT' ? user?.userId : undefined

  const { data: records, isLoading } = useQuery({
    queryKey: ['medical-records', patientId, typeFilter],
    queryFn: () => {
      if (patientId) {
        return medicalRecordService.getByPatient(patientId, typeFilter || undefined)
      }
      return typeFilter
        ? medicalRecordService.getByType(typeFilter)
        : Promise.resolve([])
    },
  })

  if (isLoading) {
    return <div>Loading medical records...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Medical Records</h1>
          <p className="text-gray-600 mt-1">View and manage medical records</p>
        </div>
      </div>

      <div className="card">
        <div className="flex flex-wrap gap-2 mb-6">
          <button
            onClick={() => setTypeFilter('')}
            className={`px-4 py-2 rounded-lg ${
              !typeFilter ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
            }`}
          >
            All Types
          </button>
          {['DIAGNOSIS', 'LAB', 'SURGERY', 'NOTES'].map((type) => (
            <button
              key={type}
              onClick={() => setTypeFilter(type)}
              className={`px-4 py-2 rounded-lg capitalize ${
                typeFilter === type ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
              }`}
            >
              {type.toLowerCase()}
            </button>
          ))}
        </div>

        <div className="space-y-4">
          {records && records.length > 0 ? (
            records.map((record: any) => (
              <div
                key={record.recordId}
                className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow"
              >
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3 mb-2">
                      <span className="px-3 py-1 bg-purple-100 text-purple-800 rounded-full text-xs font-medium capitalize">
                        {record.recordType?.toLowerCase()}
                      </span>
                      {record.dateOfRecord && (
                        <span className="text-sm text-gray-600">
                          {format(new Date(record.dateOfRecord), 'PPP')}
                        </span>
                      )}
                    </div>

                    {record.description && (
                      <p className="text-gray-900 mb-2">{record.description}</p>
                    )}

                    {record.patient && user?.role !== 'PATIENT' && (
                      <p className="text-sm text-gray-600">
                        Patient: {record.patient.user?.fullName}
                      </p>
                    )}

                    <p className="text-xs text-gray-500 mt-2">
                      Created: {format(new Date(record.createdAt), 'PPP')}
                    </p>
                  </div>

                  {record.documentUrl && (
                    <a
                      href={record.documentUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="btn-secondary inline-flex items-center ml-4"
                    >
                      <Download className="w-4 h-4 mr-2" />
                      Download
                    </a>
                  )}
                </div>
              </div>
            ))
          ) : (
            <div className="text-center py-12">
              <FileText className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">No medical records found</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

