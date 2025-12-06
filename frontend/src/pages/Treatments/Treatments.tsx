import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { treatmentService } from '../../services/api/treatmentService'
import { Link } from 'react-router-dom'
import { Heart, Plus, TrendingUp } from 'lucide-react'
import { useAuth } from '../../contexts/AuthContext'
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts'

export default function Treatments() {
  const { user } = useAuth()
  const [statusFilter, setStatusFilter] = useState<string>('')

  const { data: treatments, isLoading } = useQuery({
    queryKey: ['treatments', statusFilter],
    queryFn: () => treatmentService.getAll(statusFilter || undefined),
  })

  const COLORS = ['#10b981', '#f59e0b', '#ef4444']

  const statusStats = treatments?.reduce((acc: any, t: any) => {
    acc[t.status] = (acc[t.status] || 0) + 1
    return acc
  }, {})

  const chartData = statusStats ? Object.entries(statusStats).map(([name, value]) => ({
    name,
    value,
  })) : []

  if (isLoading) {
    return <div>Loading treatments...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Medical Treatments</h1>
          <p className="text-gray-600 mt-1">Browse and manage treatment sponsorships</p>
        </div>
        {user?.role === 'PATIENT' && (
          <Link to="/treatments/new" className="btn-primary inline-flex items-center">
            <Plus className="w-5 h-5 mr-2" />
            Create Treatment
          </Link>
        )}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        <div className="lg:col-span-3">
          <div className="flex space-x-2 mb-4">
            <button
              onClick={() => setStatusFilter('')}
              className={`px-4 py-2 rounded-lg ${!statusFilter ? 'bg-healthcare-primary text-white' : 'bg-gray-200'}`}
            >
              All
            </button>
            <button
              onClick={() => setStatusFilter('ACTIVE')}
              className={`px-4 py-2 rounded-lg ${statusFilter === 'ACTIVE' ? 'bg-healthcare-primary text-white' : 'bg-gray-200'}`}
            >
              Active
            </button>
            <button
              onClick={() => setStatusFilter('MET')}
              className={`px-4 py-2 rounded-lg ${statusFilter === 'MET' ? 'bg-healthcare-primary text-white' : 'bg-gray-200'}`}
            >
              Goal Met
            </button>
          </div>

          <div className="space-y-4">
            {treatments && treatments.length > 0 ? (
              treatments.map((treatment: any) => (
                <Link
                  key={treatment.treatmentId}
                  to={`/treatments/${treatment.treatmentId}`}
                  className="card hover:shadow-lg transition-shadow duration-200"
                >
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center space-x-3 mb-2">
                        <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                          treatment.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                          treatment.status === 'MET' ? 'bg-blue-100 text-blue-800' :
                          'bg-gray-100 text-gray-800'
                        }`}>
                          {treatment.status}
                        </span>
                        <span className="px-3 py-1 bg-purple-100 text-purple-800 rounded-full text-xs font-medium">
                          {treatment.treatmentType}
                        </span>
                      </div>
                      
                      <p className="font-medium text-gray-900 mb-2">{treatment.description}</p>
                      
                      <div className="flex items-center space-x-4 text-sm text-gray-600">
                        <span>Goal: ${treatment.goalAmount?.toLocaleString() || '0'}</span>
                        <span>Raised: ${treatment.raisedAmount?.toLocaleString() || '0'}</span>
                        {treatment.goalAmount && (
                          <span className="flex items-center">
                            <TrendingUp className="w-4 h-4 mr-1" />
                            {((treatment.raisedAmount / treatment.goalAmount) * 100).toFixed(1)}%
                          </span>
                        )}
                      </div>
                      
                      {treatment.goalAmount && (
                        <div className="mt-3 w-full bg-gray-200 rounded-full h-2">
                          <div
                            className="bg-healthcare-primary h-2 rounded-full transition-all"
                            style={{
                              width: `${Math.min((treatment.raisedAmount / treatment.goalAmount) * 100, 100)}%`,
                            }}
                          />
                        </div>
                      )}
                    </div>
                  </div>
                </Link>
              ))
            ) : (
              <div className="card text-center py-12">
                <Heart className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                <p className="text-gray-500">No treatments found</p>
              </div>
            )}
          </div>
        </div>

        <div className="lg:col-span-1">
          <div className="card">
            <h3 className="font-bold text-gray-900 mb-4">Statistics</h3>
            {chartData.length > 0 ? (
              <ResponsiveContainer width="100%" height={200}>
                <PieChart>
                  <Pie
                    data={chartData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {chartData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <p className="text-gray-500 text-sm">No data available</p>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

