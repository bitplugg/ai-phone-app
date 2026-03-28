#!/bin/bash

set -e

echo "==================================="
echo "  AI Phone App Build Script"
echo "==================================="

cd "$(dirname "$0")"

export JAVA_HOME=~/jdk/jdk-21.0.2
export PATH=$JAVA_HOME/bin:$PATH

if [ ! -d "gradle/wrapper" ]; then
    echo "Error: Gradle wrapper not found"
    exit 1
fi

GRADLE_WRAPPER="$PWD/gradlew"

chmod +x "$GRADLE_WRAPPER"

echo ""
echo "Building debug APK..."
echo ""

./gradlew assembleDebug --no-daemon

if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo ""
    echo "==================================="
    echo "  Build Successful!"
    echo "==================================="
    echo "APK Location: app/build/outputs/apk/debug/app-debug.apk"
    ls -lh app/build/outputs/apk/debug/app-debug.apk
else
    echo "Error: APK not found"
    exit 1
fi
