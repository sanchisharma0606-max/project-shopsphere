@echo off
cd /d "%~dp0"
echo ===================================================
echo  ShopSphere TCP Socket Client (Interactive CLI)
echo ===================================================
java -cp shopsphere-network\target\shopsphere-network-1.0.0.jar com.shopsphere.network.client.InventorySocketClient
pause
