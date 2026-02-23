import { Toaster } from "@/components/ui/sonner";
import { queryClient } from "@/lib/query-client";
import { QueryClientProvider } from "@tanstack/react-query";
import { createRootRoute, Outlet } from "@tanstack/react-router";
import axios from "axios";
import { lazy, Suspense } from "react";

const isDev = import.meta.env.DEV;

const TanStackRouterDevtools = isDev
  ? lazy(() =>
    import("@tanstack/react-router-devtools").then((mod) => ({
      default: mod.TanStackRouterDevtools,
    }))
  )
  : () => null;

const ReactQueryDevtools = isDev
  ? lazy(() =>
    import("@tanstack/react-query-devtools").then((mod) => ({
      default: mod.ReactQueryDevtools,
    }))
  )
  : () => null;

axios.defaults.withCredentials = true;
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;

const RootLayout = () => (
  <QueryClientProvider client={queryClient}>
    <Outlet />
    <Toaster />
    <Suspense>
      <TanStackRouterDevtools />
      <ReactQueryDevtools />
    </Suspense>
  </QueryClientProvider>
);

export const Route = createRootRoute({ component: RootLayout });
