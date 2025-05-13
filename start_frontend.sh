#!/bin/bash

# Remove existing lock file and node_modules (optional, but ensures a clean install)
rm -rf package-lock.json node_modules || true

# Force reinstallation of dependencies
npm install --force --arch=arm64 --platform=linux

# Start the development server
npm run dev
