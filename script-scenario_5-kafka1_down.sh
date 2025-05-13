#!/bin/bash

set -e  # Exit on error

# 🖥️ Server Endpoints
ADMIN_SERVER="http://localhost:8081"
SERVICE_SERVER="http://localhost:8080"

echo ""
echo "🔸 Step 1: [Initialization Phase] Delete property and refresh config"
echo "------------------------------------------------------------"

echo "🔹 Deleting existing 'feature-on' property (if exists)..."
curl -s -X DELETE "$ADMIN_SERVER/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.feature-on" | jq
echo "✅ Property deleted (if it existed)."

echo ""
echo "🔹 Refreshing properties..."
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

read -p $'\n🔸 Step 2: Check initial feature-on status (expected: OFF)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo -e "\n✅ Initial status checked."
echo "============================================================"


read -p $'\n🔸 Step 3: Stop kafka1 container\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
docker-compose stop kafka1
echo "✅ kafka1 has been stopped."
echo "============================================================"

read -p $'\n🔸 Step 4: Create property (feature-on)\n[Press Enter to continue] ' _
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

read -p $'\n🔸 Step 5: Refresh properties after creation\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
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

read -p $'\n🔸 Step 6: Check final feature-on status (should be ON)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo ""
echo "✅ Checked final status."
echo "============================================================"

read -p $'\n🔸 Step 7: Restart kafka1 container\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
docker-compose start kafka1
echo "✅ kafka1 has been restarted."
echo "============================================================"

echo ""
echo "🎉 completed."
