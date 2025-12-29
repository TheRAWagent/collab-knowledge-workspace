import { defineConfig } from "orval";

//@ts-ignore
const baseUrl = import.meta.env.BASE_URL as string;

export default defineConfig({
  'page-service': {
    input: {
      target: `${baseUrl}/api-docs/page`,
    },
    output: {
      mode: "single",
      client: "axios",
      target: "src/page-service.ts",
      baseUrl: "http://ckw-page-service:8080",
      httpClient: "axios",

    }
  }
});
