import VerifyOtpCard from "@/modules/auth/components/verify-otp-card";
import { createFileRoute, Navigate } from "@tanstack/react-router";
import z from "zod";

export const Route = createFileRoute("/_auth/verify-otp/")({
  component: RouteComponent,
  validateSearch: z.object({
    email: z.string().email(),
  }),
  errorComponent: () => (
    <Navigate to="/sign-up" />
  )
});

function RouteComponent() {
  const { email } = Route.useSearch();
  return <VerifyOtpCard email={email} />;
}
