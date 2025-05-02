#!/bin/bash

set -e  # 에러 발생 시 스크립트 즉시 종료

echo ""
echo "🔹 Step 1: Deleting existing property (switchable.feature-on) if exists..."
echo "------------------------------------------------------------"
curl -s -X DELETE "http://localhost:8081/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.feature-on"
curl -s -X DELETE "http://localhost:8081/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.test-kafka-stream-server"
echo -e "\n✅ Property deleted (if it existed)."
echo "============================================================"

echo ""
echo "🔹 Step 2: Refreshing properties (initial state)..."
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

echo ""
echo "🔹 Step 3: Checking initial feature-on & Kafka Stream state..."
echo "------------------------------------------------------------"
echo "🔸 Feature-on status:"
curl -s -X GET http://localhost:8080/feature-on-status
echo -e "\n------------------------------------------------------------"
echo "🔸 Kafka Stream state (eager & lazy):"
curl -s -X GET http://localhost:8080/eager-lazy-kafka-stream
echo -e "\n✅ Checked initial states."
echo "============================================================"

echo ""
echo "🔹 Step 4: Creating property (switchable.feature-on)..."
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

echo ""
echo "🔹 Step 5: Refreshing properties after creation..."
echo "------------------------------------------------------------"
echo "⏰ Refresh request time: $(date '+%Y-%m-%d %H:%M:%S')"
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

echo ""
echo "🔹 Step 6: Checking feature-on & Kafka Stream state after targeted refresh..."
echo "------------------------------------------------------------"
echo "🔸 Feature-on status:"
curl -s -X GET http://localhost:8080/feature-on-status
echo -e "\n------------------------------------------------------------"
echo "🔸 Kafka Stream state (eager & lazy):"
curl -s -X GET http://localhost:8080/eager-lazy-kafka-stream
echo -e "\n✅ Checked states after targeted refresh."
echo "============================================================"

echo ""
echo "🎉 Scenario 5 (Targeted Bean Refresh Test) completed."
