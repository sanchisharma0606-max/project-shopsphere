@echo off
where mysql >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" (
        set "PATH=%PATH%;C:\Program Files\MySQL\MySQL Server 8.0\bin"
    )
)
echo ===================================================
echo  ShopSphere MySQL Database Setup & Seed Import
echo ===================================================
echo.
set /p MYSQL_USER="Enter MySQL user [default: root]: "
if "%MYSQL_USER%"=="" set MYSQL_USER=root

set /p MYSQL_PASS="Enter MySQL password (leave empty if none): "

echo.
echo Step 1: Creating database and tables from database\shopsphere.sql...
if "%MYSQL_PASS%"=="" (
    mysql -u %MYSQL_USER% < database\shopsphere.sql
) else (
    mysql -u %MYSQL_USER% -p%MYSQL_PASS% < database\shopsphere.sql
)

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to execute database\shopsphere.sql. Please verify credentials.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Step 2: Populating initial seed data from database\seed-data.sql...
if "%MYSQL_PASS%"=="" (
    mysql -u %MYSQL_USER% shopsphere < database\seed-data.sql
) else (
    mysql -u %MYSQL_USER% -p%MYSQL_PASS% shopsphere < database\seed-data.sql
)

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to execute database\seed-data.sql.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ===================================================
echo  SUCCESS: ShopSphere database initialized with seed data!
echo ===================================================
echo Default Admin: admin@shopsphere.com / Password@123
echo Default Customer: rahul@example.com / Password@123
echo ===================================================
pause
