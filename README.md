# AMS Backend

Spring Boot backend for the Appointment Marking System, matching the API surface the
[frontend's mock layer](../AMS-%20Front%20End/Appointment-Marking-System/src/lib/api.js) already expects
(`registerPatient`, `login`, `getDoctors`, `addAvailability`, `bookAppointment`, etc).

## Stack

- Java 17, Spring Boot 3.3, Maven
- Spring Web, Spring Data JPA, Spring Security (JWT, stateless)
- PostgreSQL

## Setup

1. Create the database:
   ```sql
   CREATE DATABASE ams_db;
   ```
2. Update `src/main/resources/application.properties` with your Postgres username/password if they
   differ from the defaults (`postgres` / `postgres`).
3. Run it:
   ```
   mvn spring-boot:run
   ```
   The API starts on `http://localhost:8080`. Tables are created automatically
   (`spring.jpa.hibernate.ddl-auto=update`) and seeded with demo accounts on first run (see `DataSeeder`):

   | Role    | Email                 | Password   |
   |---------|-----------------------|------------|
   | Admin   | admin@example.com     | admin123   |
   | Doctor  | silva@example.com     | doctor123  |
   | Doctor  | perera@example.com    | doctor123  |
   | Patient | patient@example.com   | patient123 |

## Auth

`POST /api/auth/login` and `/api/auth/register` return a JWT in the response body. Send it on every
subsequent request as `Authorization: Bearer <token>`.

## Endpoints

| Method | Path                                   | Access                | Notes |
|--------|-----------------------------------------|------------------------|-------|
| POST   | `/api/auth/register`                    | public                | creates a PATIENT |
| POST   | `/api/auth/login`                       | public                | |
| GET    | `/api/doctors?search=&specialization=`  | public                | |
| GET    | `/api/doctors/{id}`                     | public                | |
| POST   | `/api/doctors`                          | ADMIN                 | |
| PUT    | `/api/doctors/{id}`                     | ADMIN                 | |
| DELETE | `/api/doctors/{id}`                     | ADMIN                 | |
| GET    | `/api/availability/doctor/{doctorId}?onlyFree=` | authenticated  | |
| POST   | `/api/availability/doctor/{doctorId}`   | ADMIN, DOCTOR         | |
| DELETE | `/api/availability/{id}`                | ADMIN, DOCTOR         | |
| POST   | `/api/appointments`                     | PATIENT               | books a slot |
| GET    | `/api/appointments/patient/{patientId}` | authenticated          | |
| GET    | `/api/appointments/doctor/{doctorId}`   | authenticated          | |
| GET    | `/api/appointments`                     | ADMIN                  | all appointments |
| PATCH  | `/api/appointments/{id}/status`         | ADMIN, DOCTOR          | body: `{ "status": "CONFIRMED" }` |
| PATCH  | `/api/appointments/{id}/cancel`         | authenticated          | |
| GET    | `/api/admin/stats`                      | ADMIN                  | |
| GET    | `/api/admin/patients`                   | ADMIN                  | |

CORS is open for `http://localhost:3000` (the Next.js frontend's dev origin) — change
`app.cors.allowed-origins` in `application.properties` if that changes.

## Wiring up the frontend

Replace the localStorage-backed functions in `src/lib/api.js` with calls to this API (e.g. via `axios`
or `fetch`), keeping the same function names/signatures so the pages don't need to change. Store the JWT
returned from login/register (e.g. alongside the existing `abs_current_user` value) and attach it as a
Bearer token on every request.
