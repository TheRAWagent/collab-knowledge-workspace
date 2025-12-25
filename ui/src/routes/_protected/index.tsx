import { EmptyUI } from '@/modules/workspaces/components/empty-ui'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_protected/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <EmptyUI />
}
  