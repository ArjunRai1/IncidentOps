# IncidentOps

IncidentOps is a production ready full-stack incident management system built using Spring Boot and React. It provides authentication, incident lifecycle management, comments, timeline tracking and lays the foundation for AI-assisted incident analysis.

---

# Tech Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL
- Redis
- Flyway
- Docker

## Frontend

- React
- Vite
- React Router
- Axios
- Tailwind CSS
- Context API

---

# Features Implemented

## Authentication

### User Registration

A new user registers using their email and password.

Flow:

1. User enters registration details.
2. Backend validates the request.
3. User information is stored temporarily.
4. An OTP is generated.
5. OTP is stored in Redis with an expiry.
6. OTP is emailed to the user.
7. Frontend redirects to the verification page.

---

### Email Verification

Flow:

1. User enters the OTP.
2. Backend validates the OTP against Redis.
3. If valid:
   - User is created in PostgreSQL.
   - Redis entry is removed.
4. User is redirected to Login.

---

### Login

Flow:

1. User enters email and password.
2. Credentials are authenticated.
3. JWT token is generated.
4. Token is returned to the frontend.
5. Frontend stores the token.
6. Every subsequent API request automatically includes the JWT.

---

### Protected Routes

Authenticated users can access:

- Dashboard
- Incidents
- Profile

Unauthenticated users are redirected to Login.

---

### Current User

Frontend loads the authenticated user using:

```
GET /api/v1/auth/me
```

Returned information:

- User ID
- Email
- Role

---

### Forgot Password

Flow:

1. User enters registered email.
2. Backend generates a password reset OTP.
3. OTP is stored in Redis.
4. OTP is emailed.
5. Frontend redirects to Reset Password.

---

### Reset Password

Flow:

1. User enters:
   - OTP
   - New Password
2. Backend validates OTP.
3. Password is updated.
4. Redis entry is deleted.
5. User is redirected to Login.

---

# Incident Management

## Incident List

Implemented:

- List incidents
- Server-side pagination
- Server-side searching
- Status filtering
- Priority filtering
- Sorting
- Loading state
- Empty state
- Error handling

All filtering and sorting are performed by the backend.

---

## Create Incident

Users can create a new incident.

Captured information:

- Title
- Description
- Priority
- Assignee (if provided)

Status is automatically initialized as **OPEN** by the backend.

---

## View Incident

Displays complete incident information including:

- Title
- Description
- Status
- Priority
- Creator
- Assignee
- Created timestamp
- Updated timestamp

---

## Update Incident

Users can update incident details.

Editable fields:

- Title
- Description
- Priority
- Status
- Assignee

Save operation is available only when editing.

---

## Comments

Implemented:

- View comments
- Add new comments

Comments are displayed chronologically with:

- Author
- Timestamp
- Comment text

Comments cannot be edited.

---

## Timeline

Displays incident history.

Each event contains:

- Event type
- Description
- Timestamp

Timeline is read-only.

---

# Profile

Implemented:

Displays authenticated user's:

- User ID
- Email
- Role

Information is retrieved from:

```
GET /api/v1/auth/me
```

---

# Frontend Architecture

## API Layer

Dedicated API modules:

- authApi
- incidentApi
- commentApi
- timelineApi
- profileApi

---

## Authentication

Implemented using React Context.

Responsibilities:

- Login
- Logout
- Authentication state
- Current user
- Route protection

---

## Routing

Public Routes

- Login
- Register
- Verify OTP
- Forgot Password
- Reset Password

Protected Routes

- Dashboard
- Incidents
- Profile

---

## Reusable Components

Implemented reusable components:

- Button
- Card
- Loader
- EmptyState

---

# Backend Infrastructure

Implemented

- PostgreSQL persistence
- Flyway migrations
- Redis
- JWT authentication
- Spring Security
- Docker Compose

Redis is currently used for:

- Registration OTP
- Password Reset OTP

---

# Current Application Flow

```
Register
      │
      ▼
OTP Verification
      │
      ▼
Login
      │
      ▼
Dashboard
      │
      ├──────────────► Profile
      │
      └──────────────► Incidents
                         │
                         ├── View
                         ├── Create
                         ├── Update
                         ├── Comments
                         └── Timeline
```

---

# Work Remaining

## AI Module

Planned:

- AI Assistant page
- RAG Chat interface
- Integration with Spring AI backend
- Incident summarization
- Root cause suggestions
- Similar incident retrieval

---

## UI Improvements

Remaining:

- Status badges
- Priority badges
- Consistent spacing
- Responsive improvements
- Form consistency

---

## Backend Improvements

Planned:

- Redis caching for incident queries
- Cache invalidation after updates
- Role-based UI rendering
- Additional audit improvements

---

## Deployment

Remaining:

- Backend deployment
- Frontend deployment
- Production configuration
- End-to-end testing

---

# Project Status

| Module | Status |
|---------|--------|
| Authentication | ✅ Complete |
| Registration OTP | ✅ Complete |
| Password Reset | ✅ Complete |
| JWT Authentication | ✅ Complete |
| Profile | ✅ Complete |
| Incident CRUD | ✅ Complete |
| Comments | ✅ Complete |
| Timeline | ✅ Complete |
| Search | ✅ Complete |
| Filters | ✅ Complete |
| Sorting | ✅ Complete |
| Pagination | ✅ Complete |
| AI Module | ⏳ Planned |
| Deployment | ⏳ Pending |
| Final UI Polish | ⏳ Pending |
