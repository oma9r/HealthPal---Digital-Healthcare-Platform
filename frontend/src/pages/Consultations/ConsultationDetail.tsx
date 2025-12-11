import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { consultationService } from '../../services/api/consultationService'
import { ArrowLeft, Send, Paperclip } from 'lucide-react'
import { format } from 'date-fns'
import { useState } from 'react'
import { toast } from 'react-toastify'

export default function ConsultationDetail() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [messageText, setMessageText] = useState('')

  const { data: consultation, isLoading } = useQuery({
    queryKey: ['consultation', id],
    queryFn: () => consultationService.getById(Number(id)),
  })

  const { data: messages } = useQuery({
    queryKey: ['consultation-messages', id],
    queryFn: () => consultationService.getMessages(Number(id)),
    enabled: !!consultation,
  })

  const sendMessageMutation = useMutation({
    mutationFn: (text: string) => consultationService.sendMessage(Number(id), text),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['consultation-messages', id] })
      setMessageText('')
      toast.success('Message sent')
    },
  })

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault()
    if (messageText.trim()) {
      sendMessageMutation.mutate(messageText)
    }
  }

  if (isLoading) {
    return <div>Loading...</div>
  }

  if (!consultation) {
    return <div>Consultation not found</div>
  }

  return (
    <div className="space-y-6">
      <button
        onClick={() => navigate('/consultations')}
        className="flex items-center text-gray-600 hover:text-gray-900 mb-4"
      >
        <ArrowLeft className="w-4 h-4 mr-2" />
        Back to Consultations
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <div className="card mb-6">
            <h1 className="text-2xl font-bold text-gray-900 mb-4">Consultation Details</h1>
            
            <div className="space-y-4">
              <div>
                <label className="text-sm font-medium text-gray-500">Doctor</label>
                <p className="text-gray-900">{consultation.doctor?.user?.fullName || 'TBD'}</p>
              </div>
              
              <div>
                <label className="text-sm font-medium text-gray-500">Scheduled Time</label>
                <p className="text-gray-900">{format(new Date(consultation.scheduledTime), 'PPp')}</p>
              </div>
              
              <div>
                <label className="text-sm font-medium text-gray-500">Mode</label>
                <p className="text-gray-900 capitalize">{consultation.mode}</p>
              </div>
              
              <div>
                <label className="text-sm font-medium text-gray-500">Status</label>
                <span className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${
                  consultation.status === 'SCHEDULED' || consultation.status === 'CONFIRMED'
                    ? 'bg-blue-100 text-blue-800'
                    : consultation.status === 'COMPLETED'
                    ? 'bg-green-100 text-green-800'
                    : 'bg-red-100 text-red-800'
                }`}>
                  {consultation.status}
                </span>
              </div>
              
              {consultation.notes && (
                <div>
                  <label className="text-sm font-medium text-gray-500">Notes</label>
                  <p className="text-gray-900">{consultation.notes}</p>
                </div>
              )}
            </div>
          </div>

          <div className="card">
            <h2 className="text-xl font-bold text-gray-900 mb-4">Messages</h2>
            
            <div className="space-y-4 mb-6 max-h-96 overflow-y-auto">
              {messages && messages.length > 0 ? (
                messages.map((message: any) => (
                  <div
                    key={message.messageId}
                    className="p-4 bg-gray-50 rounded-lg"
                  >
                    <div className="flex justify-between items-start mb-2">
                      <p className="font-medium text-gray-900">{message.sender?.fullName}</p>
                      <p className="text-xs text-gray-500">
                        {format(new Date(message.createdAt), 'PPp')}
                      </p>
                    </div>
                    <p className="text-gray-700">{message.messageText}</p>
                    {message.attachmentUrl && (
                      <a
                        href={message.attachmentUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-sm text-healthcare-primary hover:underline mt-2 inline-flex items-center"
                      >
                        <Paperclip className="w-3 h-3 mr-1" />
                        Attachment
                      </a>
                    )}
                  </div>
                ))
              ) : (
                <p className="text-gray-500 text-center py-8">No messages yet</p>
              )}
            </div>

            <form onSubmit={handleSendMessage} className="flex space-x-2">
              <input
                type="text"
                value={messageText}
                onChange={(e) => setMessageText(e.target.value)}
                placeholder="Type a message..."
                className="input-field flex-1"
              />
              <button type="submit" className="btn-primary">
                <Send className="w-5 h-5" />
              </button>
            </form>
          </div>
        </div>

        <div className="lg:col-span-1">
          <div className="card">
            <h3 className="font-bold text-gray-900 mb-4">Quick Actions</h3>
            <div className="space-y-2">
              <button className="w-full btn-secondary text-left">Reschedule</button>
              <button className="w-full btn-secondary text-left">Cancel Consultation</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

