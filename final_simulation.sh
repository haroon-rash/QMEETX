#!/bin/bash
GATEWAY_URL="http://localhost:5000"

echo "--- Starting 10-User Simulation ---"

# Step 1: Book 10 Appointments
echo "Booking 10 appointments..."
for i in {1..10}
do
    CUSTOMER_ID=$((2000 + i))
    SLOT_ID=$((30 + i))
    curl -s -X POST "${GATEWAY_URL}/api/v1/appointments/book?slotId=${SLOT_ID}&customerId=${CUSTOMER_ID}" -i | head -n 1
done

echo "Waiting for Kafka events to process bookings..."
sleep 3

# Step 2: Check-in 10 Appointments
echo "Checking in 10 appointments..."
# We need appointment IDs. If this is a fresh run since seeder, IDs might be 11-20 (since 1-10 were from previous tests).
# Let's fetch the latest 10 appointments.
APT_IDS=$(docker exec qmeetx-postgres psql -U root -d appointment_db -t -c "SELECT id FROM appointments ORDER BY id DESC LIMIT 10;" | xargs)

for APT_ID in $APT_IDS
do
    echo "Checking in Appointment $APT_ID..."
    curl -s -X POST "${GATEWAY_URL}/api/v1/appointments/${APT_ID}/check-in" -i | head -n 1
done

echo "Waiting for Queue Service to process auto-joins..."
sleep 5

echo "--- Simulation Complete ---"
echo "Verifying Queue Tokens in Database..."
docker exec qmeetx-postgres psql -U root -d queue_db -c "SELECT count(*) FROM tokens WHERE queue_id=1;"
docker exec qmeetx-postgres psql -U root -d appointment_db -c "SELECT id, status FROM appointments ORDER BY id DESC LIMIT 10;"
