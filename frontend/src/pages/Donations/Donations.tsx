import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { donationService } from '../../services/api/donationService'
import { DollarSign, Filter, Search } from 'lucide-react'
import { format } from 'date-fns'
import { useAuth } from '../../contexts/AuthContext'

export default function Donations() {
  const { user } = useAuth()
  const [statusFilter, setStatusFilter] = useState<string>('')
  const [searchQuery, setSearchQuery] = useState('')

  const { data: donations, isLoading } = useQuery({
    queryKey: ['donations', statusFilter],
    queryFn: () => donationService.getAll({ status: statusFilter || undefined }),
  })

  const filteredDonations = donations?.filter((donation: any) => {
    if (!searchQuery) return true
    const query = searchQuery.toLowerCase()
    return (
      donation.donor?.user?.fullName?.toLowerCase().includes(query) ||
      donation.treatment?.description?.toLowerCase().includes(query) ||
      donation.amount?.toString().includes(query)
    )
  })

  const totalDonated = donations?.reduce((sum: number, d: any) => sum + (d.amount || 0), 0) || 0

  if (isLoading) {
    return <div>Loading donations...</div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Donations</h1>
        <p className="text-gray-600 mt-1">Track and manage your donations</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <div className="card bg-blue-50 border-blue-200">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-blue-900 mb-1">Total Donations</p>
              <p className="text-3xl font-bold text-blue-600">{donations?.length || 0}</p>
            </div>
            <DollarSign className="w-12 h-12 text-blue-600" />
          </div>
        </div>

        <div className="card bg-green-50 border-green-200">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-green-900 mb-1">Total Amount</p>
              <p className="text-3xl font-bold text-green-600">${totalDonated.toLocaleString()}</p>
            </div>
            <DollarSign className="w-12 h-12 text-green-600" />
          </div>
        </div>

        <div className="card bg-purple-50 border-purple-200">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-purple-900 mb-1">Completed</p>
              <p className="text-3xl font-bold text-purple-600">
                {donations?.filter((d: any) => d.paymentStatus === 'COMPLETED').length || 0}
              </p>
            </div>
            <DollarSign className="w-12 h-12 text-purple-600" />
          </div>
        </div>
      </div>

      <div className="card">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-6 space-y-3 md:space-y-0">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Search donations..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="input-field pl-10"
            />
          </div>

          <div className="flex space-x-2">
            <button
              onClick={() => setStatusFilter('')}
              className={`px-4 py-2 rounded-lg ${
                !statusFilter ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
              }`}
            >
              All
            </button>
            <button
              onClick={() => setStatusFilter('COMPLETED')}
              className={`px-4 py-2 rounded-lg ${
                statusFilter === 'COMPLETED' ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
              }`}
            >
              Completed
            </button>
            <button
              onClick={() => setStatusFilter('PENDING')}
              className={`px-4 py-2 rounded-lg ${
                statusFilter === 'PENDING' ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
              }`}
            >
              Pending
            </button>
          </div>
        </div>

        <div className="space-y-3">
          {filteredDonations && filteredDonations.length > 0 ? (
            filteredDonations.map((donation: any) => (
              <div
                key={donation.donationId}
                className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow"
              >
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3 mb-2">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                        donation.paymentStatus === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                        donation.paymentStatus === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {donation.paymentStatus}
                      </span>
                      <span className="px-3 py-1 bg-purple-100 text-purple-800 rounded-full text-xs font-medium">
                        {donation.donationType}
                      </span>
                    </div>

                    <p className="font-medium text-gray-900 mb-1">
                      {donation.treatment?.description || donation.equipment?.name || donation.supply?.name || 'General Donation'}
                    </p>

                    <p className="text-sm text-gray-600">
                      {format(new Date(donation.dateDonated), 'PPP')}
                    </p>

                    {donation.notes && (
                      <p className="text-sm text-gray-500 mt-2">{donation.notes}</p>
                    )}
                  </div>

                  <div className="text-right ml-4">
                    <p className="text-2xl font-bold text-green-600">
                      ${donation.amount?.toLocaleString()}
                    </p>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="text-center py-12">
              <DollarSign className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">No donations found</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

