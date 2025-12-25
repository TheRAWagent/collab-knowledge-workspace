import { Toaster } from "@/components/ui/sonner";
import { queryClient } from "@/lib/query-client";
import { QueryClientProvider } from "@tanstack/react-query";
import { createRootRoute, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import axios from "axios";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

axios.defaults.withCredentials = true;

const RootLayout = () => (
  <QueryClientProvider client={queryClient}>
    <Outlet />
    <Toaster />
    <TanStackRouterDevtools />
    <ReactQueryDevtools />
  </QueryClientProvider>
);

export const Route = createRootRoute({ component: RootLayout });
