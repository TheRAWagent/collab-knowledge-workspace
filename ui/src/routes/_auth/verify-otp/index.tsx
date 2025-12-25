import VerifyOtpCard from "@/modules/auth/components/verify-otp-card";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/verify-otp/")({
  component: RouteComponent,
});

function RouteComponent() {
  return <VerifyOtpCard />;
}
