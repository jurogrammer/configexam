#!/bin/bash

set -e  # Exit on error

# 🖥️ Server Endpoints
ADMIN_SERVER="http://localhost:8081"
SERVICE_SERVER="http://localhost:8080"

echo ""
echo "🔸 Step 1: [Initialization Phase] Clean existing config and refresh"
echo "------------------------------------------------------------"

echo "🔹 Deleting existing 'feature-on' property (if exists)..."
curl -s -X DELETE "$ADMIN_SERVER/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.feature-on"
echo -e "\n✅ Property deleted (if it existed)."

echo ""
echo "🔹 Refreshing properties..."
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }'
echo -e "\n✅ Refresh completed."
echo "⏳ Waiting 3 seconds for changes to apply..."
sleep 3
echo "============================================================"

read -p $'\n🔸 Step 2: Check initial feature-on status (expected: OFF)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo -e "\n✅ Initial status checked."
echo "============================================================"

read -p $'\n🔸 Step 3: Create new feature-on property\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X POST "$ADMIN_SERVER/v1/properties" \
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

read -p $'\n🔸 Step 4: Refresh properties after creation\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X POST "$ADMIN_SERVER/v1/properties/refresh" \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }'
echo -e "\n✅ Refresh completed."
echo "⏳ Waiting 3 seconds for changes to apply..."
sleep 3
echo "============================================================"

read -p $'\n🔸 Step 5: Check final feature-on status (expected: ON)\n[Press Enter to continue] ' _
echo "------------------------------------------------------------"
curl -s -X GET "$SERVICE_SERVER/feature-on-status"
echo -e "\n✅ Final status checked."
echo "============================================================"

echo ""
echo "🎉 completed."
