#!/bin/bash

# script-scenario_2-cloud_config_server1_down.sh
# 시나리오 2: cloud-config-server1 다운 시 설정 변경 및 반영 확인

set -e  # 에러 발생 시 스크립트 즉시 종료

echo ""
echo "🔹 Step 1: Deleting existing property by composite key (if exists)..."
echo "------------------------------------------------------------"
curl -s -X DELETE "http://localhost:8081/v1/properties/by-composite-key?application=application&deployPhase=local&propertyKey=switchable.feature-on"
echo -e "\n✅ Property deleted (if it existed)."
echo "============================================================"

echo ""
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

echo ""
echo "🔹 Step 3: Checking initial feature-on status (should be OFF)..."
echo "------------------------------------------------------------"
curl -s -X GET http://localhost:8080/feature-on-status
echo -e "\n✅ Checked initial status."
echo "============================================================"

echo ""
echo "🔹 Step 4: Stopping cloud-config-server1..."
echo "------------------------------------------------------------"
docker-compose stop cloud-config-server1
echo -e "\n✅ cloud-config-server1 has been stopped."
echo "============================================================"

echo ""
echo "🔹 Step 5: Creating property (feature-on)..."
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
echo "🔹 Step 6: Refreshing properties after creation..."
echo "------------------------------------------------------------"
curl -s -X POST http://localhost:8081/v1/properties/refresh \
     -H "Content-Type: application/json" \
     -d '{
           "application": "application",
           "deployPhase": "local"
         }'
echo -e "\n✅ Refresh completed."
echo "⏳ Waiting 10 seconds to allow up to 3 retry attempts (3s timeout each)..."
sleep 10
echo "============================================================"

echo ""
echo "🔹 Step 7: Checking feature-on status (should be ON)..."
echo "------------------------------------------------------------"
curl -s -X GET http://localhost:8080/feature-on-status
echo -e "\n✅ Checked final status."
echo "============================================================"

echo ""
echo "🔹 Step 8: Starting cloud-config-server1 again..."
echo "------------------------------------------------------------"
docker-compose start cloud-config-server1
echo -e "\n✅ cloud-config-server1 has been restarted."
echo "============================================================"
