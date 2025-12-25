import AuthenticationCard from '@/modules/auth/components/sign-in-card'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/sign-in/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <AuthenticationCard />
}
