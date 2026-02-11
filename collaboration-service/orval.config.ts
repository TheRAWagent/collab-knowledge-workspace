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
      baseUrl: "/",
      httpClient: "axios",

    }
  }
});
