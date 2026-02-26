import { defineConfig } from "orval";

//@ts-ignore
const baseUrl: string = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export default defineConfig({
  "auth-zod": {
    input: `${baseUrl}/api-docs/auth`,
    output: {
      client: "zod",
      target: "./src/modules/auth/schemas.ts",
    },
  },
  "auth-tanstack-query": {
    input: `${baseUrl}/api-docs/auth`,
    output: {
      mode: "single",
      client: "react-query",
      target: "./src/modules/auth/api.ts",
      baseUrl: '/auth',
      httpClient: "axios",
      override: {
        mutator: {
          path: "./src/lib/axios.ts",
          name: "customInstance",
        }
      }  
    },
  },

  "workspace-zod": {
    input: `${baseUrl}/api-docs/workspace`,
    output: {
      client: "zod",
      target: "./src/modules/workspaces/schemas.ts",
    },
  },

  "workspace-tanstack-query": {
    input: `${baseUrl}/api-docs/workspace`,
    output: {
      mode: "single",
      client: "react-query",
      target: "./src/modules/workspaces/api.ts",
      baseUrl: '/workspaces',
      httpClient: "axios",
      override: {
        mutator: {
          path: "./src/lib/axios.ts",
          name: "customInstance",
        }
      }  
    },
  },

  "page-zod": {
    input: `${baseUrl}/api-docs/page`,
    output: {
      client: "zod",
      target: "./src/modules/pages/schemas.ts",
    },
  },

  "page-tanstack-query": {
    input: `${baseUrl}/api-docs/page`,
    output: {
      mode: "single",
      client: "react-query",
      target: "./src/modules/pages/api.ts",
      baseUrl: '/workspace',
      httpClient: "axios",
      override: {
        mutator: {
          path: "./src/lib/axios.ts",
          name: "customInstance",
        }
      }  
    },
  },

  "user-zod": {
    input: `${baseUrl}/api-docs/user`,
    output: {
      client: "zod",
      target: "./src/modules/users/schemas.ts",
    },
  },

  "user-tanstack-query": {
    input: `${baseUrl}/api-docs/user`,
    output: {
      mode: "single",
      client: "react-query",
      target: "./src/modules/users/api.ts",
      baseUrl: '/user',
      httpClient: "axios",
      override: {
        mutator: {
          path: "./src/lib/axios.ts",
          name: "customInstance",
        }
      }  
    },
  },
});
