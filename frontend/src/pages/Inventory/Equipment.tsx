import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { equipmentService } from '../../services/api/equipmentService'
import { Package, Plus, Search, MapPin, CheckCircle } from 'lucide-react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

export default function Equipment() {
  const { user } = useAuth()
  const [searchQuery, setSearchQuery] = useState('')
  const [availableFilter, setAvailableFilter] = useState<boolean | undefined>(undefined)

  const { data: equipment, isLoading } = useQuery({
    queryKey: ['equipment', availableFilter],
    queryFn: () => equipmentService.getAll(availableFilter),
  })

  const filteredEquipment = equipment?.filter((item: any) => {
    if (searchQuery) {
      const query = searchQuery.toLowerCase()
      return (
        item.name?.toLowerCase().includes(query) ||
        item.location?.toLowerCase().includes(query) ||
        item.condition?.toLowerCase().includes(query)
      )
    }
    return true
  })

  if (isLoading) {
    return <div>Loading equipment...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Medical Equipment</h1>
          <p className="text-gray-600 mt-1">Browse available medical equipment</p>
        </div>
        {(user?.role === 'NGO' || user?.role === 'ADMIN') && (
          <Link to="/equipment/new" className="btn-primary inline-flex items-center">
            <Plus className="w-5 h-5 mr-2" />
            Add Equipment
          </Link>
        )}
      </div>

      <div className="card">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-6 space-y-3 md:space-y-0">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Search equipment..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="input-field pl-10"
            />
          </div>

          <div className="flex space-x-2">
            <button
              onClick={() => setAvailableFilter(undefined)}
              className={`px-4 py-2 rounded-lg ${
                availableFilter === undefined ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
              }`}
            >
              All
            </button>
            <button
              onClick={() => setAvailableFilter(true)}
              className={`px-4 py-2 rounded-lg ${
                availableFilter === true ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
              }`}
            >
              Available
            </button>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredEquipment && filteredEquipment.length > 0 ? (
            filteredEquipment.map((item: any) => (
              <div
                key={item.equipmentId}
                className="p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow"
              >
                <div className="flex justify-between items-start mb-2">
                  <h3 className="font-medium text-gray-900">{item.name}</h3>
                  {item.available && (
                    <CheckCircle className="w-5 h-5 text-green-600" />
                  )}
                </div>

                {item.description && (
                  <p className="text-sm text-gray-600 mb-3 line-clamp-2">{item.description}</p>
                )}

                <div className="space-y-2 text-sm">
                  {item.location && (
                    <div className="flex items-center text-gray-600">
                      <MapPin className="w-4 h-4 mr-2" />
                      {item.location}
                    </div>
                  )}

                  {item.condition && (
                    <div>
                      <span className="px-2 py-1 bg-blue-100 text-blue-800 rounded text-xs font-medium">
                        {item.condition}
                      </span>
                    </div>
                  )}

                  {item.ngo && (
                    <div className="text-xs text-gray-500">
                      Provided by: {item.ngo.name}
                    </div>
                  )}
                </div>

                <div className="mt-4">
                  <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                    item.available
                      ? 'bg-green-100 text-green-800'
                      : 'bg-red-100 text-red-800'
                  }`}>
                    {item.available ? 'Available' : 'Unavailable'}
                  </span>
                </div>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center py-12">
              <Package className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">No equipment found</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

