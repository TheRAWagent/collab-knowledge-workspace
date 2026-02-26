import ForgotPasswordCard from "@/modules/auth/components/forgot-password-card";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth/forgot-password/")({
  component: RouteComponent,
});

function RouteComponent() {
  return <ForgotPasswordCard />;
}
