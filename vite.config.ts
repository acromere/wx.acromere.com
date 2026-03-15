// https://vite.dev/config/

import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  root: "client",
  build: {
    outDir: '../dist',
    emptyOutDir: true,
  },
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: 'setupTests.js',
  },
})
