@echo off
cd /d "%~dp0"
echo ===================================================
echo  ShopSphere Binary Object Serialization Demo
echo ===================================================
java -cp shopsphere-network\target\shopsphere-network-1.0.0.jar com.shopsphere.network.serialization.SerializationDemo
pause
