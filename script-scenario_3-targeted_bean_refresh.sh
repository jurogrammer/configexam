#!/bin/bash

set -e  # Exit on error

# 🖥️ Server Endpoints
ADMIN_SERVER="http://localhost:8081"
SERVICE_SERVER="http://localhost:8080"

echo ""
echo "🔸 Step 1: [Initialization Phase] Delete existing properties and refresh"
echo "------------------------------------------------------------"

echo "🔹 Deleting 'switchable.feature-on' and 'switchable.test-kafka-stream-server'..."
curl -s -X DELETE "$ADMIN_SERVER/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.feature-on" | jq
curl -s -X DELETE "$ADMIN_SERVER/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.test-kafka-stream-server" | jq
echo "✅ Properties deleted (if they existed)."

echo ""
echo "🔹 Refreshing properties..."
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
  -H "Content-Type: application/json" \
  -d '{
        "application": "application",
        "deployPhase": "local"
      }' | jq
echo "✅ Refresh completed."
echo "⏳ Waiting 3 seconds for changes to apply..."
sleep 3
echo "============================================================"

read -p $'\n🔸 Step 2: Check initial feature-on & Kafka Stream state\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
echo "🔸 Feature-on status:"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo ""
echo "------------------------------------------------------------"
echo "🔸 Kafka Stream state (eager & lazy):"
curl -s -X GET "$SERVICE_SERVER/eager-lazy-kafka-stream" | jq
echo "✅ Checked initial states."
echo "============================================================"

read -p $'\n🔸 Step 3: Create property (switchable.feature-on)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X POST "$ADMIN_SERVER/v1/properties" \
  -H "Content-Type: application/json" \
  -d '{
        "application": "application",
        "deployPhase": "local",
        "propertyKey": "switchable.feature-on",
        "propertyValue": "true",
        "description": "test feature"
      }' | jq
echo "✅ Property created."
echo "============================================================"

read -p $'\n🔸 Step 4: Refresh properties after creation\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
echo "⏰ Refresh request time: $(date '+%Y-%m-%d %H:%M:%S')"
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
  -H "Content-Type: application/json" \
  -d '{
        "application": "application",
        "deployPhase": "local"
      }' | jq
echo "✅ Refresh completed."
echo "⏳ Waiting 3 seconds for changes to apply..."
sleep 3
echo "============================================================"

read -p $'\n🔸 Step 5: Check feature-on & Kafka Stream state after targeted refresh\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
echo "🔸 Feature-on status:"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo ""
echo "------------------------------------------------------------"
echo "🔸 Kafka Stream state (eager & lazy):"
curl -s -X GET "$SERVICE_SERVER/eager-lazy-kafka-stream" | jq
echo "✅ Checked states after targeted refresh."
echo "============================================================"

echo ""
echo "🎉 completed."
