import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { treatmentService } from '../../services/api/treatmentService'
import { donationService } from '../../services/api/donationService'
import { ArrowLeft, Heart, DollarSign, TrendingUp, Users } from 'lucide-react'
import { format } from 'date-fns'
import { toast } from 'react-toastify'
import { useState } from 'react'
import { useAuth } from '../../contexts/AuthContext'

export default function TreatmentDetail() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { user } = useAuth()
  const [donationAmount, setDonationAmount] = useState('')
  const [showDonateModal, setShowDonateModal] = useState(false)
  const queryClient = useQueryClient()

  const { data: treatment, isLoading } = useQuery({
    queryKey: ['treatment', id],
    queryFn: () => treatmentService.getById(Number(id)),
  })

  const { data: progress } = useQuery({
    queryKey: ['treatment-progress', id],
    queryFn: () => treatmentService.getProgress(Number(id)),
    enabled: !!treatment,
  })

  const { data: donations } = useQuery({
    queryKey: ['treatment-donations', id],
    queryFn: () => donationService.getAll({ treatmentId: Number(id) }),
    enabled: !!treatment,
  })

  const donateMutation = useMutation({
    mutationFn: (amount: number) =>
      donationService.create({
        treatment: { treatmentId: treatment?.treatmentId },
        amount,
        donationType: 'MONEY',
        paymentStatus: 'PENDING',
        dateDonated: new Date().toISOString(),
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['treatment-progress', id] })
      queryClient.invalidateQueries({ queryKey: ['treatment-donations', id] })
      setShowDonateModal(false)
      setDonationAmount('')
      toast.success('Donation submitted successfully!')
    },
  })

  const handleDonate = (e: React.FormEvent) => {
    e.preventDefault()
    const amount = parseFloat(donationAmount)
    if (amount > 0) {
      donateMutation.mutate(amount)
    }
  }

  if (isLoading) {
    return <div>Loading...</div>
  }

  if (!treatment) {
    return <div>Treatment not found</div>
  }

  const progressPercent = treatment.goalAmount
    ? Math.min((treatment.raisedAmount / treatment.goalAmount) * 100, 100)
    : 0

  return (
    <div className="space-y-6">
      <button
        onClick={() => navigate('/treatments')}
        className="flex items-center text-gray-600 hover:text-gray-900 mb-4"
      >
        <ArrowLeft className="w-4 h-4 mr-2" />
        Back to Treatments
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <div className="card mb-6">
            <div className="flex items-center justify-between mb-4">
              <h1 className="text-2xl font-bold text-gray-900">{treatment.description}</h1>
              <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                treatment.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                treatment.status === 'MET' ? 'bg-blue-100 text-blue-800' :
                'bg-gray-100 text-gray-800'
              }`}>
                {treatment.status}
              </span>
            </div>

            <div className="space-y-4 mb-6">
              <div>
                <label className="text-sm font-medium text-gray-500">Treatment Type</label>
                <p className="text-gray-900 capitalize">{treatment.treatmentType?.toLowerCase()}</p>
              </div>

              {treatment.startDate && (
                <div>
                  <label className="text-sm font-medium text-gray-500">Start Date</label>
                  <p className="text-gray-900">{format(new Date(treatment.startDate), 'PPP')}</p>
                </div>
              )}

              <div>
                <label className="text-sm font-medium text-gray-500">Patient</label>
                <p className="text-gray-900">{treatment.patient?.user?.fullName}</p>
              </div>
            </div>

            <div className="border-t pt-6">
              <div className="flex justify-between items-center mb-2">
                <span className="text-sm font-medium text-gray-700">Progress</span>
                <span className="text-sm font-bold text-healthcare-primary">{progressPercent.toFixed(1)}%</span>
              </div>
              
              <div className="w-full bg-gray-200 rounded-full h-4 mb-4">
                <div
                  className="bg-healthcare-primary h-4 rounded-full transition-all flex items-center justify-end pr-2"
                  style={{ width: `${progressPercent}%` }}
                >
                  {progressPercent > 10 && (
                    <span className="text-xs text-white font-medium">
                      ${treatment.raisedAmount?.toLocaleString()}
                    </span>
                  )}
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-gray-600">Goal Amount</p>
                  <p className="text-2xl font-bold text-gray-900">
                    ${treatment.goalAmount?.toLocaleString() || '0'}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Raised Amount</p>
                  <p className="text-2xl font-bold text-green-600">
                    ${treatment.raisedAmount?.toLocaleString() || '0'}
                  </p>
                </div>
              </div>
            </div>
          </div>

          {donations && donations.length > 0 && (
            <div className="card">
              <h2 className="text-xl font-bold text-gray-900 mb-4">Recent Donations</h2>
              <div className="space-y-3">
                {donations.slice(0, 10).map((donation: any) => (
                  <div key={donation.donationId} className="flex justify-between items-center p-4 bg-gray-50 rounded-lg">
                    <div>
                      <p className="font-medium text-gray-900">
                        {donation.donor?.user?.fullName || 'Anonymous'}
                      </p>
                      <p className="text-sm text-gray-600">
                        {format(new Date(donation.dateDonated), 'PPP')}
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-green-600">${donation.amount?.toLocaleString()}</p>
                      <span className={`text-xs px-2 py-1 rounded ${
                        donation.paymentStatus === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                        donation.paymentStatus === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                        'bg-gray-100 text-gray-800'
                      }`}>
                        {donation.paymentStatus}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        <div className="lg:col-span-1">
          <div className="card">
            <h3 className="font-bold text-gray-900 mb-4">Support This Treatment</h3>
            {treatment.status === 'ACTIVE' && (
              <button
                onClick={() => setShowDonateModal(true)}
                className="w-full btn-primary mb-4"
              >
                <DollarSign className="w-5 h-5 inline mr-2" />
                Donate Now
              </button>
            )}

            <div className="space-y-3">
              <div className="p-4 bg-blue-50 rounded-lg">
                <p className="text-sm font-medium text-blue-900 mb-1">Total Donations</p>
                <p className="text-2xl font-bold text-blue-600">{donations?.length || 0}</p>
              </div>

              <div className="p-4 bg-green-50 rounded-lg">
                <p className="text-sm font-medium text-green-900 mb-1">Remaining Goal</p>
                <p className="text-2xl font-bold text-green-600">
                  ${((treatment.goalAmount || 0) - (treatment.raisedAmount || 0)).toLocaleString()}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {showDonateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">Make a Donation</h2>
            <form onSubmit={handleDonate} className="space-y-4">
              <div>
                <label className="label">Amount (USD)</label>
                <input
                  type="number"
                  min="1"
                  step="0.01"
                  required
                  value={donationAmount}
                  onChange={(e) => setDonationAmount(e.target.value)}
                  className="input-field"
                  placeholder="Enter amount"
                />
              </div>
              <div className="flex space-x-3">
                <button
                  type="button"
                  onClick={() => setShowDonateModal(false)}
                  className="flex-1 btn-secondary"
                >
                  Cancel
                </button>
                <button type="submit" className="flex-1 btn-primary" disabled={donateMutation.isPending}>
                  {donateMutation.isPending ? 'Processing...' : 'Donate'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

