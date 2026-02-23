import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'
import tailwindcss from '@tailwindcss/vite'
import { tanstackRouter } from '@tanstack/router-plugin/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    tanstackRouter({
      target: 'react',
      autoCodeSplitting: true,
    }),
    react({
      babel: {
        plugins: [['babel-plugin-react-compiler', {
          compilationMode: 'annotation' // Only compile "use memo" functions
        }]],
      },
    }),
    tailwindcss({
      optimize: {
        minify: true
      }
    })
  ],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  build: {
    // Target modern browsers for smaller output
    target: 'es2022',
    // Enable minification with terser-level optimizations
    cssMinify: 'lightningcss',
    rollupOptions: {
      output: {
        manualChunks(id) {
          // Split large vendor libraries into separate cacheable chunks
          if (id.includes('node_modules')) {
            // TipTap editor extensions — only loaded on editor pages
            if (id.includes('@tiptap') || id.includes('prosemirror')) {
              return 'vendor-editor';
            }
            // Yjs / Hocuspocus (collaboration) — only loaded with editor
            if (id.includes('yjs') || id.includes('y-prosemirror') || id.includes('hocuspocus') || id.includes('lib0')) {
              return 'vendor-collab';
            }
            // highlight.js language grammars
            if (id.includes('highlight.js') || id.includes('lowlight')) {
              return 'vendor-highlight';
            }
            // Radix UI primitives
            if (id.includes('@radix-ui')) {
              return 'vendor-radix';
            }
            // TanStack (router + query + table)
            if (id.includes('@tanstack')) {
              return 'vendor-tanstack';
            }
            // React core
            if (id.includes('react-dom') || id.includes('react/')) {
              return 'vendor-react';
            }
          }
        },
      },
    },
  },
})

