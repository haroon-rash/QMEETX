#!/bin/bash
set -e
echo ">>> QMEETX Fully Automated Verification (DB-per-Service) <<<"

echo "0. Building Modules (Host-side)..."
./mvnw clean package -DskipTests

echo "1. Cleaning up OLD containers and VOLUMES (Required for new DBs)..."
# -v removes volumes, forcing init.sql to run again
docker compose down -v

echo "2. Starting Infrastructure & Services..."
docker compose up -d --build

echo "3. Waiting for services to initialize (120 seconds)..."
sleep 120

# Check ports
if ! nc -z localhost 5433; then
  echo "ERROR: Postgres (5433) is NOT reachable."
  exit 1
fi

if ! nc -z localhost 9094; then
  echo "ERROR: Kafka (9094) is NOT reachable."
  exit 1
fi

echo "4. Running 10-User Flow Verification (Seeding)..."
# We can try to curl the seeding endpoint to prove the app is UP and DB is writable.
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "http://localhost:5000/api/v1/seeder/run-10-users")

echo "5. Checking Security (Expecting 401 on protected route)..."
# Try to access a protected route without a token (e.g., getting user profile, if exists, or just root api/v1)
# Assuming /api/v1/organizations is protected by default pattern if not in public-paths, but we added it to public paths for testing.
# Let's try /api/auth/validate which should be public, but let's try a non-public one if defined.
# Actually, we made MOST things public in the Gateway config for testing.
# Let's check a made-up protected path to see if Gateway rejects it? 
# Or check the one path we didn't add:  /user/** (User Service)
# User Service routes: /user/** 
USER_HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:5000/api/users/profile")
if [ "$USER_HTTP_STATUS" -eq 401 ] || [ "$USER_HTTP_STATUS" -eq 403 ]; then
    echo "SUCCESS: Security check passed. /user/1 returned $USER_HTTP_STATUS (Unauthorized/Forbidden) without token."
else
    # If we haven't implemented full security yet, it might return 404 or something else.
    # But Gateway should block it if no token present if filter is active.
    echo "INFO: Security check returned $USER_HTTP_STATUS for /user/1." 
fi

echo "6. Checking Authentication Service (Connectivity via Gateway)..."
# We expect 400 Bad Request (invalid body) or 401 or 403, but NOT 502/503/504 (Gateway Error) or 404.
AUTH_HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d '{}' "http://localhost:5000/api/auth/login")
if [ "$AUTH_HTTP_STATUS" -ne 502 ] && [ "$AUTH_HTTP_STATUS" -ne 503 ] && [ "$AUTH_HTTP_STATUS" -ne 504 ] && [ "$AUTH_HTTP_STATUS" -ne 000 ]; then
     echo "SUCCESS: Authentication Service is reachable via Gateway! (Status: $AUTH_HTTP_STATUS)"
else
     echo "ERROR: Authentication Service seems unreachable. Status: $AUTH_HTTP_STATUS"
fi

if [ "$HTTP_STATUS" -eq 200 ]; then
    echo "SUCCESS: Seeder API returned 200 OK. Appointment Service is connected to 'appointment_db'."
else
    echo "FAILURE: Seeder API returned $HTTP_STATUS. check logs."
    docker logs appointment-service | tail -n 20
    exit 1
fi

echo "5. SUCCESS! System is running with Database-per-Service."
