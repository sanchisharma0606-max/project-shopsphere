$ErrorActionPreference = 'Stop'
$mavenZip = "$env:USERPROFILE\apache-maven-3.9.9-bin.zip"
$mavenDir = "$env:USERPROFILE\maven"
$mvnExe = "$env:USERPROFILE\maven\apache-maven-3.9.9\bin\mvn.cmd"

if (-not (Test-Path $mvnExe)) {
    Write-Host 'Downloading Apache Maven 3.9.9...'
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip' -OutFile $mavenZip
    Write-Host 'Extracting to' $mavenDir '...'
    if (-not (Test-Path $mavenDir)) {
        New-Item -ItemType Directory -Path $mavenDir -Force | Out-Null
    }
    Expand-Archive -Path $mavenZip -DestinationPath $mavenDir -Force
    if (Test-Path $mavenZip) {
        Remove-Item $mavenZip -Force
    }
}

Write-Host "Maven ready at: $mvnExe"
& $mvnExe -version
