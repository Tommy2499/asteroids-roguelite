import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import svgr from 'vite-plugin-svgr'

// https://vite.dev/config/
export default defineConfig({
  base: '/asteroids/',
  plugins: [
    react(),
    svgr(),
  ],
  server: {
    host: true,
    port: 80,
    allowedHosts: ['137.184.232.147'],
  }
})
