@echo off
cd /d "%~dp0"
echo ===================================================
echo  ShopSphere - Push Code to GitHub Repository
echo  Target: https://github.com/sanchisharma0606-max/project-shopsphere.git
echo ===================================================
echo.
git push -u origin main
echo.
if %ERRORLEVEL% EQU 0 (
    echo ===================================================
    echo  SUCCESS: All code pushed to GitHub successfully!
    echo ===================================================
) else (
    echo [ERROR] Push failed or authentication cancelled.
)
pause
