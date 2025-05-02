#!/bin/bash

set -e  # 에러 발생 시 스크립트 즉시 종료

echo "🔹 Step 1: Deleting existing property (switchable.test-kafka-stream-server) if exists..."
echo "------------------------------------------------------------"
curl -s -X DELETE "http://localhost:8081/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.test-kafka-stream-server"
echo -e "\n✅ Property deleted (if it existed)."
echo "============================================================"

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

echo "🔹 Step 3: Checking Kafka Stream state after initialization (should show initial startAt times)..."
echo "------------------------------------------------------------"
curl -s -X GET http://localhost:8080/eager-lazy-kafka-stream
echo -e "\n✅ Checked Kafka Stream state after initialization."
echo "============================================================"

echo "🔹 Step 4: Creating property (switchable.test-kafka-stream-server)..."
echo "------------------------------------------------------------"
curl -s -X POST http://localhost:8081/v1/properties \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local",
           "propertyKey": "switchable.test-kafka-stream-server",
           "propertyValue": "updated-value",
           "description": "testing eager vs lazy refresh"
         }'
echo -e "\n✅ Property created."
echo "============================================================"

echo "🔹 Step 5: Refreshing properties after update..."
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

echo "🔹 Step 6: Checking Kafka Stream startAt times after refresh..."
echo "------------------------------------------------------------"
echo "⏳ Waiting 10 seconds to observe lazy re-initialization"
sleep 10
echo "⏰ Check time: $(date '+%Y-%m-%d %H:%M:%S')"
curl -s -X GET http://localhost:8080/eager-lazy-kafka-stream
echo -e "\n✅ Checked startAt times after refresh."
echo "============================================================"

echo "🎉 Scenario 4 (Kafka Stream eager vs lazy test) completed."
