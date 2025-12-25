import SignUpCard from '@/modules/auth/components/sign-up-card'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/sign-up/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <SignUpCard />
}
