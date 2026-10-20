@echo off
cd /d "%~dp0"
echo ===================================================
echo  ShopSphere RMI Registry & Server (Port 1099)
echo ===================================================
java -cp shopsphere-rmi\target\shopsphere-rmi-1.0.0.jar com.shopsphere.rmi.server.RmiServer
pause
