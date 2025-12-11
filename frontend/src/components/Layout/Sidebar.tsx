import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  MessageSquare,
  Heart,
  DollarSign,
  Package,
  Users,
  FileText,
  AlertCircle,
  Settings,
  Shield,
} from 'lucide-react'
import { clsx } from 'clsx'

interface SidebarProps {
  user: any
  currentPath: string
}

export default function Sidebar({ user, currentPath }: SidebarProps) {
  const role = user?.role

  const menuItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: '/', roles: ['PATIENT', 'DOCTOR', 'ADMIN', 'DONOR', 'NGO'] },
    { icon: MessageSquare, label: 'Consultations', path: '/consultations', roles: ['PATIENT', 'DOCTOR', 'ADMIN'] },
    { icon: Heart, label: 'Treatments', path: '/treatments', roles: ['PATIENT', 'DOCTOR', 'ADMIN', 'DONOR'] },
    { icon: DollarSign, label: 'Donations', path: '/donations', roles: ['DONOR', 'ADMIN', 'NGO'] },
    { icon: Package, label: 'Equipment', path: '/equipment', roles: ['PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR'] },
    { icon: Package, label: 'Supplies', path: '/supplies', roles: ['PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR'] },
    { icon: Users, label: 'NGOs', path: '/ngo', roles: ['PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR'] },
    { icon: FileText, label: 'Medical Records', path: '/medical-records', roles: ['PATIENT', 'DOCTOR', 'ADMIN'] },
    { icon: AlertCircle, label: 'Health Alerts', path: '/health-alerts', roles: ['PATIENT', 'DOCTOR', 'ADMIN', 'NGO', 'DONOR'] },
    { icon: Settings, label: 'Profile', path: '/profile', roles: ['PATIENT', 'DOCTOR', 'ADMIN', 'DONOR', 'NGO'] },
  ]

  if (role === 'ADMIN') {
    menuItems.push({ icon: Shield, label: 'Admin Panel', path: '/admin', roles: ['ADMIN'] })
  }

  const filteredItems = menuItems.filter(item => item.roles.includes(role))

  return (
    <aside className="fixed left-0 top-16 h-[calc(100vh-4rem)] w-64 bg-white shadow-lg border-r border-gray-200 overflow-y-auto">
      <nav className="p-4 space-y-2">
        {filteredItems.map((item) => {
          const Icon = item.icon
          const isActive = currentPath === item.path || (item.path !== '/' && currentPath.startsWith(item.path))
          
          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={clsx(
                'flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors duration-200',
                isActive
                  ? 'bg-healthcare-primary text-white'
                  : 'text-gray-700 hover:bg-gray-100'
              )}
            >
              <Icon className="w-5 h-5" />
              <span className="font-medium">{item.label}</span>
            </NavLink>
          )
        })}
      </nav>
    </aside>
  )
}

