# PowerShell script to package the Hva application

$javaPath = $env:JAVA_HOME

if (-not $javaPath) {
    Write-Host "JAVA_HOME environment variable is not set. Exiting."
    Exit 1
}

# Construct the full path to the jpackage executable
$jpackagePath = Join-Path -Path $javaPath -ChildPath "bin\jpackage.exe"

# Check if jpackage executable exists
if (-not (Test-Path $jpackagePath)) {
    Write-Host "jpackage executable not found. Make sure JAVA_HOME environment variable is set correctly. Exiting."
    Exit 1
}

# Define temporary folder path
$tempFolder = "$env:TEMP\HvaTemp"

# Check if temporary folder exists
if (Test-Path $tempFolder) {
    # Remove all files and subdirectories from the temporary folder
    Remove-Item -Path $tempFolder\* -Recurse -Force
} else {
    # Create temporary folder if it doesn't exist
    New-Item -ItemType Directory -Path $tempFolder | Out-Null
}

# Copy hva.jar to temporary folder
Copy-Item -Path "target\hva.jar" -Destination $tempFolder -Force

# Execute jpackage to create the installer
Write-Host "Creates a Windows installer in target/installer ..."
& $jpackagePath `
    --name Hva `
    --main-class hva.Hva `
    --input "$tempFolder" `
    --main-jar hva.jar `
    --dest target\installer `
    --type exe `
    --icon src\main\resources\images\eye_256.ico `
    --win-menu `
    --win-shortcut `
    --vendor "Sigurd Stendal" `
    --app-version 4.0

# Remove temporary folder
Remove-Item -Path $tempFolder -Recurse -Force
