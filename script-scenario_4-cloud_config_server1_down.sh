#!/bin/bash

set -e  # Exit on error

# 🖥️ Server Endpoints
ADMIN_SERVER="http://localhost:8081"
SERVICE_SERVER="http://localhost:8080"

echo ""
echo "🔸 Step 1: [Initialization Phase] Delete existing property"
echo "------------------------------------------------------------"
curl -s -X DELETE "$ADMIN_SERVER/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.feature-on" \
  | jq .
echo -e "\n✅ Property deleted (if it existed)."
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }' | jq
echo -e "\n✅ Refresh completed."
echo "⏳ Waiting 3 seconds for changes to apply..."
sleep 3
echo "============================================================"

read -p $'\n🔸 Step 2: Check initial feature-on status (expected: OFF)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo -e "\n✅ Initial status checked."
echo "============================================================"

read -p $'\n🔸 Step 3: Stop cloud-config-server1\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
docker-compose stop cloud-config-server1
echo -e "\n✅ cloud-config-server1 has been stopped."
echo "============================================================"

read -p $'\n🔸 Step 4: Create new feature-on property\n[Press Enter to continue] ' _
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
echo -e "\n✅ Property created."
echo "============================================================"

read -p $'\n🔸 Step 5: Refresh properties after creation\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }' | jq
echo -e "\n✅ Refresh completed."
echo "⏳ Waiting 10 seconds to allow up to 3 retry attempts (3s timeout each)..."
sleep 10
echo "============================================================"

read -p $'\n🔸 Step 6: Check final feature-on status (expected: ON)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo -e "\n✅ Final status checked."
echo "============================================================"

read -p $'\n🔸 Step 7: Restart cloud-config-server1\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
docker-compose start cloud-config-server1
echo -e "\n✅ cloud-config-server1 has been restarted."
echo "============================================================"

echo ""
echo "🎉 completed."
