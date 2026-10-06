#!/bin/bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
if ! JAVA_HOME=$(/usr/libexec/java_home -v 1.8 -a arm64 2>/dev/null); then
  printf '%s\n' 'An already-installed Java 8 arm64 JDK is required; nothing was installed.' >&2
  exit 1
fi

BUILD_DIR="$ROOT_DIR/bin/speed-preview"
mkdir -p "$BUILD_DIR"
"$JAVA_HOME/bin/javac" -d "$BUILD_DIR" \
  "$ROOT_DIR/src/mtb/devices/rails/StackShotSpeedCalculator.java" \
  "$ROOT_DIR/src/mtb/devices/rails/SpeedPreview.java"
exec "$JAVA_HOME/bin/java" -cp "$BUILD_DIR" mtb.devices.rails.SpeedPreview
