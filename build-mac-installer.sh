#!/bin/bash

# Check if JAVA_HOME environment variable is set
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME environment variable is not set. Exiting."
    exit 1
fi

# Construct the full path to the jpackage executable
jpackagePath="$JAVA_HOME/bin/jpackage"

# Check if jpackage executable exists
if [ ! -x "$jpackagePath" ]; then
    echo "jpackage executable not found. Make sure JAVA_HOME environment variable is set correctly. Exiting."
    exit 1
fi

# Define temporary folder path
tempFolder=$(mktemp -d)

# Copy hva.jar to temporary folder
cp target/hva.jar "$tempFolder"

# Execute jpackage to create the installer
echo "Creates a Mac installer in target/installer ..."
"$jpackagePath" \
    --name Hva \
    --main-class hva.Hva \
    --input "$tempFolder" \
    --main-jar hva.jar \
    --dest target/installer \
    --type dmg \
    --icon src/main/icon/eye.icns \
    --vendor "Sigurd Stendal" \
    --app-version 4.0

# Remove temporary folder
rm -rf "$tempFolder"
