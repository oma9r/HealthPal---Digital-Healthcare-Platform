import { useQuery } from '@tanstack/react-query'
import { Shield, Users, DollarSign, Activity } from 'lucide-react'
import { treatmentService } from '../../services/api/treatmentService'
import { donationService } from '../../services/api/donationService'
import { ngoService } from '../../services/api/ngoService'

export default function AdminDashboard() {
  const { data: treatments } = useQuery({
    queryKey: ['treatments'],
    queryFn: () => treatmentService.getAll(),
  })

  const { data: donations } = useQuery({
    queryKey: ['donations'],
    queryFn: () => donationService.getAll(),
  })

  const { data: ngos } = useQuery({
    queryKey: ['ngos'],
    queryFn: () => ngoService.getAll(),
  })

  const stats = [
    {
      title: 'Total Treatments',
      value: treatments?.length || 0,
      icon: Activity,
      color: 'text-blue-600',
      bgColor: 'bg-blue-50',
    },
    {
      title: 'Total Donations',
      value: donations?.length || 0,
      icon: DollarSign,
      color: 'text-green-600',
      bgColor: 'bg-green-50',
    },
    {
      title: 'Total NGOs',
      value: ngos?.length || 0,
      icon: Users,
      color: 'text-purple-600',
      bgColor: 'bg-purple-50',
    },
    {
      title: 'Verified NGOs',
      value: ngos?.filter((n: any) => n.verified).length || 0,
      icon: Shield,
      color: 'text-orange-600',
      bgColor: 'bg-orange-50',
    },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
        <p className="text-gray-600 mt-1">System overview and management</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat) => {
          const Icon = stat.icon
          return (
            <div key={stat.title} className="card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-gray-600 mb-1">{stat.title}</p>
                  <p className="text-3xl font-bold text-gray-900">{stat.value}</p>
                </div>
                <div className={`${stat.bgColor} p-3 rounded-lg`}>
                  <Icon className={`w-6 h-6 ${stat.color}`} />
                </div>
              </div>
            </div>
          )
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-xl font-bold text-gray-900 mb-4">Pending Verifications</h2>
          <div className="space-y-3">
            {ngos?.filter((n: any) => !n.verified).slice(0, 5).map((ngo: any) => (
              <div key={ngo.ngoId} className="p-3 border border-gray-200 rounded-lg">
                <p className="font-medium text-gray-900">{ngo.name}</p>
                <button className="mt-2 text-sm text-healthcare-primary hover:underline">
                  Review & Verify
                </button>
              </div>
            )) || <p className="text-gray-500">No pending verifications</p>}
          </div>
        </div>

        <div className="card">
          <h2 className="text-xl font-bold text-gray-900 mb-4">Recent Activity</h2>
          <p className="text-gray-500">Activity log will be displayed here</p>
        </div>
      </div>
    </div>
  )
}

