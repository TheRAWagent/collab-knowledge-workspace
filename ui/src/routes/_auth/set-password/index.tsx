import SetPasswordCard from '@/modules/auth/components/set-password-card'
import { createFileRoute } from '@tanstack/react-router'
import z from 'zod'

const searchSchema = z.object({
  email: z.string().email(),
})

export const Route = createFileRoute('/_auth/set-password/')({
  validateSearch: (search) => searchSchema.parse(search),
  component: RouteComponent,
})

function RouteComponent() {
  const { email } = Route.useSearch()
  return <SetPasswordCard email={email} />
}
