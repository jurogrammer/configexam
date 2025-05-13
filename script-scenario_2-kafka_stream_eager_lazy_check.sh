#!/bin/bash

set -e  # Exit on error

# 🖥️ Server Endpoints
ADMIN_SERVER="http://localhost:8081"
SERVICE_SERVER="http://localhost:8080"

echo ""
echo "🔸 Step 1: [Initialization Phase] Delete property and refresh config"
echo "------------------------------------------------------------"

echo "🔹 Deleting existing property (switchable.test-kafka-stream-server) if exists..."
curl -s -X DELETE "$ADMIN_SERVER/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.test-kafka-stream-server" | jq
echo "✅ Property deleted (if it existed)."

echo ""
echo "🔹 Refreshing properties (initial state)..."
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }' | jq
echo "✅ Refresh completed."
echo "⏳ Waiting 3 seconds to apply changes..."
sleep 3
echo "============================================================"

read -p $'\n🔸 Step 2: Check Kafka Stream state after initialization\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X GET "$SERVICE_SERVER/eager-lazy-kafka-stream" | jq
echo "✅ Checked Kafka Stream state after initialization."
echo "============================================================"

read -p $'\n🔸 Step 3: Create property (switchable.test-kafka-stream-server)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X POST "$ADMIN_SERVER/v1/properties" \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local",
           "propertyKey": "switchable.test-kafka-stream-server",
           "propertyValue": "updated-value",
           "description": "testing eager vs lazy refresh"
         }' | jq
echo "✅ Property created."
echo "============================================================"

read -p $'\n🔸 Step 4: Refresh properties after update\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
echo "⏰ Refresh request time: $(date '+%Y-%m-%d %H:%M:%S')"
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }' | jq
echo "✅ Refresh completed."
echo "⏳ Waiting 3 seconds to apply changes..."
sleep 3
echo "============================================================"

read -p $'\n🔸 Step 5: Check Kafka Stream startAt times after refresh\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
echo "⏰ Check time: $(date '+%Y-%m-%d %H:%M:%S')"
curl -s -X GET "$SERVICE_SERVER/eager-lazy-kafka-stream" | jq
echo "✅ Checked startAt times after refresh."
echo "============================================================"

echo ""
echo "🎉 completed."
