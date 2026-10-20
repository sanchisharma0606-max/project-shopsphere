@echo off
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    mvn %*
) else (
    "%USERPROFILE%\maven\apache-maven-3.9.9\bin\mvn.cmd" %*
)
