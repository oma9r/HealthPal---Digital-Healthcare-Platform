import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { ngoService } from '../../services/api/ngoService'
import { Users, CheckCircle, XCircle, Plus } from 'lucide-react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

export default function NGO() {
  const { user } = useAuth()
  const [showVerifiedOnly, setShowVerifiedOnly] = useState(false)

  const { data: ngos, isLoading } = useQuery({
    queryKey: ['ngos', showVerifiedOnly],
    queryFn: () => ngoService.getAll(showVerifiedOnly ? true : undefined),
  })

  if (isLoading) {
    return <div>Loading NGOs...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">NGOs</h1>
          <p className="text-gray-600 mt-1">Verified medical non-governmental organizations</p>
        </div>
        {user?.role !== 'NGO' && !user?.role !== 'ADMIN' && (
          <Link to="/ngo/register" className="btn-primary inline-flex items-center">
            <Plus className="w-5 h-5 mr-2" />
            Register NGO
          </Link>
        )}
      </div>

      <div className="card">
        <div className="flex justify-end mb-6">
          <label className="flex items-center space-x-2 cursor-pointer">
            <input
              type="checkbox"
              checked={showVerifiedOnly}
              onChange={(e) => setShowVerifiedOnly(e.target.checked)}
              className="rounded"
            />
            <span className="text-sm text-gray-700">Show verified only</span>
          </label>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {ngos && ngos.length > 0 ? (
            ngos.map((ngo: any) => (
              <div
                key={ngo.ngoId}
                className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow"
              >
                <div className="flex items-start justify-between mb-3">
                  <h3 className="font-bold text-gray-900 text-lg">{ngo.name}</h3>
                  {ngo.verified ? (
                    <CheckCircle className="w-5 h-5 text-green-600" title="Verified" />
                  ) : (
                    <XCircle className="w-5 h-5 text-gray-400" title="Not Verified" />
                  )}
                </div>

                {ngo.contactInfo && (
                  <p className="text-sm text-gray-600 mb-3">{ngo.contactInfo}</p>
                )}

                <div className="flex items-center justify-between pt-3 border-t">
                  <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                    ngo.verified
                      ? 'bg-green-100 text-green-800'
                      : 'bg-yellow-100 text-yellow-800'
                  }`}>
                    {ngo.verified ? 'Verified' : 'Pending Verification'}
                  </span>
                  <Link
                    to={`/ngo/${ngo.ngoId}`}
                    className="text-sm text-healthcare-primary hover:underline"
                  >
                    View Details
                  </Link>
                </div>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center py-12">
              <Users className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">No NGOs found</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

