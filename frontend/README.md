# HealthPal Frontend

Modern React + TypeScript frontend for the HealthPal digital healthcare platform.

## Technology Stack

- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **State Management**: React Query (TanStack Query)
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **UI Components**: Custom components with Tailwind CSS
- **Icons**: Lucide React
- **Charts**: Recharts
- **Forms**: React Hook Form
- **Notifications**: React Toastify

## Getting Started

### Prerequisites

- Node.js 18+ and npm/yarn
- Backend API running on `http://localhost:8080`

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Create a `.env` file (optional):
   ```env
   VITE_API_URL=http://localhost:8080/api
   ```

4. Start the development server:
   ```bash
   npm run dev
   ```

The frontend will be available at `http://localhost:3000`

### Build for Production

```bash
npm run build
```

The built files will be in the `dist` directory.

## Project Structure

```
frontend/
├── src/
│   ├── components/          # Reusable UI components
│   │   └── Layout/         # Layout components (Header, Sidebar)
│   ├── contexts/           # React contexts (Auth)
│   ├── pages/              # Page components
│   │   ├── Auth/          # Login, Register
│   │   ├── Dashboard/     # Main dashboard
│   │   ├── Consultations/ # Consultation management
│   │   ├── Treatments/    # Treatment sponsorship
│   │   ├── Donations/     # Donation tracking
│   │   ├── Inventory/     # Equipment & Supplies
│   │   ├── NGO/           # NGO management
│   │   ├── MedicalRecords/# Medical records
│   │   ├── HealthAlerts/  # Health alerts
│   │   ├── Profile/       # User profile
│   │   └── Admin/         # Admin dashboard
│   ├── services/           # API services
│   │   └── api/           # API client and service modules
│   ├── App.tsx            # Main app component with routing
│   ├── main.tsx           # Entry point
│   └── index.css          # Global styles
├── index.html
├── package.json
├── tsconfig.json
├── tailwind.config.js
└── vite.config.ts
```

## Features

### Authentication
- User login and registration
- Role-based access control (Patient, Doctor, Donor, NGO, Admin)
- JWT token management

### Core Features
- **Dashboard**: Overview with stats and recent activity
- **Consultations**: Book, view, and manage remote consultations with messaging
- **Treatments**: Create and track treatment sponsorships with donation progress
- **Donations**: Make and track donations with transparency
- **Inventory**: Browse equipment and supplies with expiry tracking
- **NGOs**: View and register NGOs with verification status
- **Medical Records**: View and manage medical records
- **Health Alerts**: View public health alerts
- **Profile**: Manage user profile information

### UI/UX
- Responsive design (mobile, tablet, desktop)
- Modern, clean interface
- Role-based navigation
- Real-time updates with React Query
- Toast notifications for user feedback
- Loading states and error handling

## API Integration

All API calls are made through service modules in `src/services/api/`. The base API client is configured with:
- Automatic JWT token injection
- Request/response interceptors
- Error handling and user feedback

## Environment Variables

- `VITE_API_URL`: Backend API base URL (default: `http://localhost:8080/api`)

## Development

The app uses:
- **Hot Module Replacement (HMR)** for fast development
- **TypeScript** for type safety
- **ESLint** for code quality (if configured)

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

MIT License - See LICENSE file in project root

