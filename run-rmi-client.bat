@echo off
cd /d "%~dp0"
echo ===================================================
echo  ShopSphere RMI Client
echo ===================================================
java -cp shopsphere-rmi\target\shopsphere-rmi-1.0.0.jar com.shopsphere.rmi.client.RmiClient
pause
