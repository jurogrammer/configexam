#!/bin/bash

set -e  # 에러 발생 시 스크립트 즉시 종료

echo "🔹 Step 1: Deleting existing property by composite key (if exists)..."
echo "------------------------------------------------------------"
curl -s -X DELETE "http://localhost:8081/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.feature-on"
echo -e "\n✅ Property deleted (if it existed)."
echo "============================================================"

echo "🔹 Step 2: Refreshing properties..."
echo "------------------------------------------------------------"
curl -s -X POST http://localhost:8081/v1/properties/refresh \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }'
echo -e "\n✅ Refresh completed."
echo "⏳ Waiting 3 seconds to apply changes..."
sleep 3
echo "============================================================"

echo "🔹 Step 3: Stopping kafka1 container..."
echo "------------------------------------------------------------"
docker-compose stop kafka1
echo -e "\n✅ kafka1 has been stopped."
echo "============================================================"

echo "🔹 Step 4: Creating property (feature-on)..."
echo "------------------------------------------------------------"
curl -s -X POST http://localhost:8081/v1/properties \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local",
           "propertyKey": "switchable.feature-on",
           "propertyValue": "true",
           "description": "test feature"
         }'
echo -e "\n✅ Property created."
echo "============================================================"

echo "🔹 Step 5: Refreshing properties after creation..."
echo "------------------------------------------------------------"
curl -s -X POST http://localhost:8081/v1/properties/refresh \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }'
echo -e "\n✅ Refresh completed."
echo "⏳ Waiting 3 seconds to apply changes..."
sleep 3
echo "============================================================"

echo "🔹 Step 6: Checking feature-on status (should be ON)..."
echo "------------------------------------------------------------"
curl -s -X GET http://localhost:8080/feature-on-status
echo -e "\n✅ Checked final status."
echo "============================================================"

echo "🔹 Step 7: Restarting kafka1 container..."
echo "------------------------------------------------------------"
docker-compose start kafka1
echo -e "\n✅ kafka1 has been restarted."
echo "============================================================"

echo "🎉 Scenario 3 (Kafka1 down) completed."
