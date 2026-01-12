#!/bin/bash

# Configuration
SOURCE_DIR=$(pwd)
DEST_DIR="/home/haroon/Documents/projects/QMEETX"

echo "Starting Deep Clean Migration from $SOURCE_DIR to $DEST_DIR"

# Check if destination exists, create if not
if [ ! -d "$DEST_DIR" ]; then
    echo "Creating destination directory..."
    mkdir -p "$DEST_DIR"
else
    echo "Destination directory exists."
fi

# Copy files with exclusions (Deep Clean)
# Excludes: target, .idea, .mvn, .git, .settings, .classpath, .project, *.iml
echo "Copying files..."
rsync -av --progress \
    --exclude 'target' \
    --exclude '.idea' \
    --exclude '.mvn' \
    --exclude 'mvnw' \
    --exclude 'mvnw.cmd' \
    --exclude '.git' \
    --exclude '.settings' \
    --exclude '.classpath' \
    --exclude '.project' \
    --exclude '*.iml' \
    "$SOURCE_DIR/" "$DEST_DIR/"

echo "Migration complete. Please check $DEST_DIR"
