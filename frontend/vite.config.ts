import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import * as fs from "node:fs";
import autoprefixer from "autoprefixer";
import tailwind from 'tailwindcss';

export default defineConfig({
    plugins: [
        vue()
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        },
    },
    server: {
        https: {
            key: fs.readFileSync('certs/key.pem'),
            cert: fs.readFileSync('certs/cert.pem')
        }
    },
    css: {
        postcss: {
            plugins: [tailwind(), autoprefixer()]
        }
    }
})
