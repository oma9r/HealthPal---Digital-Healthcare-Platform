import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { supplyService } from '../../services/api/supplyService'
import { Package, Plus, Search, AlertTriangle } from 'lucide-react'
import { Link } from 'react-router-dom'
import { format } from 'date-fns'
import { useAuth } from '../../contexts/AuthContext'

export default function Supplies() {
  const { user } = useAuth()
  const [searchQuery, setSearchQuery] = useState('')
  const [categoryFilter, setCategoryFilter] = useState<string>('')

  const { data: supplies, isLoading } = useQuery({
    queryKey: ['supplies', categoryFilter],
    queryFn: () =>
      categoryFilter
        ? supplyService.getByCategory(categoryFilter)
        : supplyService.getAll(),
  })

  const { data: expiringSupplies } = useQuery({
    queryKey: ['supplies-expiring'],
    queryFn: () => supplyService.getExpiring(30),
  })

  const filteredSupplies = supplies?.filter((item: any) => {
    if (searchQuery) {
      const query = searchQuery.toLowerCase()
      return (
        item.name?.toLowerCase().includes(query) ||
        item.category?.toLowerCase().includes(query)
      )
    }
    return true
  })

  const categories = Array.from(new Set(supplies?.map((s: any) => s.category).filter(Boolean) || []))

  if (isLoading) {
    return <div>Loading supplies...</div>
  }

  const isExpiringSoon = (expiryDate?: string) => {
    if (!expiryDate) return false
    const expiry = new Date(expiryDate)
    const thirtyDaysFromNow = new Date()
    thirtyDaysFromNow.setDate(thirtyDaysFromNow.getDate() + 30)
    return expiry <= thirtyDaysFromNow && expiry >= new Date()
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Medical Supplies</h1>
          <p className="text-gray-600 mt-1">Manage medical supplies inventory</p>
        </div>
        {(user?.role === 'NGO' || user?.role === 'ADMIN') && (
          <Link to="/supplies/new" className="btn-primary inline-flex items-center">
            <Plus className="w-5 h-5 mr-2" />
            Add Supply
          </Link>
        )}
      </div>

      {expiringSupplies && expiringSupplies.length > 0 && (
        <div className="card bg-orange-50 border-orange-200">
          <div className="flex items-center mb-4">
            <AlertTriangle className="w-5 h-5 text-orange-600 mr-2" />
            <h2 className="text-lg font-bold text-orange-900">
              Expiring Supplies ({expiringSupplies.length})
            </h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
            {expiringSupplies.slice(0, 6).map((item: any) => (
              <div key={item.supplyId} className="p-3 bg-white rounded-lg">
                <p className="font-medium text-gray-900">{item.name}</p>
                <p className="text-sm text-orange-600">
                  Expires: {format(new Date(item.expiryDate), 'PPP')}
                </p>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="card">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-6 space-y-3 md:space-y-0">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
            <input
              type="text"
              placeholder="Search supplies..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="input-field pl-10"
            />
          </div>

          <div className="flex flex-wrap gap-2">
            <button
              onClick={() => setCategoryFilter('')}
              className={`px-4 py-2 rounded-lg ${
                !categoryFilter ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
              }`}
            >
              All Categories
            </button>
            {categories.map((category) => (
              <button
                key={category}
                onClick={() => setCategoryFilter(category)}
                className={`px-4 py-2 rounded-lg ${
                  categoryFilter === category ? 'bg-healthcare-primary text-white' : 'bg-gray-200'
                }`}
              >
                {category}
              </button>
            ))}
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredSupplies && filteredSupplies.length > 0 ? (
            filteredSupplies.map((item: any) => (
              <div
                key={item.supplyId}
                className={`p-4 border rounded-lg hover:shadow-md transition-shadow ${
                  isExpiringSoon(item.expiryDate)
                    ? 'border-orange-300 bg-orange-50'
                    : 'border-gray-200'
                }`}
              >
                <div className="flex justify-between items-start mb-2">
                  <h3 className="font-medium text-gray-900">{item.name}</h3>
                  {isExpiringSoon(item.expiryDate) && (
                    <AlertTriangle className="w-5 h-5 text-orange-600" />
                  )}
                </div>

                <div className="space-y-2 text-sm mb-3">
                  {item.category && (
                    <span className="px-2 py-1 bg-blue-100 text-blue-800 rounded text-xs font-medium">
                      {item.category}
                    </span>
                  )}

                  <div>
                    <span className="text-gray-600">Quantity: </span>
                    <span className="font-medium text-gray-900">{item.quantity || 0}</span>
                  </div>

                  {item.expiryDate && (
                    <div>
                      <span className="text-gray-600">Expires: </span>
                      <span className={`font-medium ${
                        isExpiringSoon(item.expiryDate) ? 'text-orange-600' : 'text-gray-900'
                      }`}>
                        {format(new Date(item.expiryDate), 'PPP')}
                      </span>
                    </div>
                  )}

                  {item.location && (
                    <div className="text-xs text-gray-500">{item.location}</div>
                  )}
                </div>

                {item.ngo && (
                  <div className="text-xs text-gray-500 pt-2 border-t">
                    Provided by: {item.ngo.name}
                  </div>
                )}
              </div>
            ))
          ) : (
            <div className="col-span-full text-center py-12">
              <Package className="w-12 h-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">No supplies found</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

