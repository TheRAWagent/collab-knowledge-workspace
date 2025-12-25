import { queryClient } from '@/lib/query-client'
import { getGetByIdQueryKey, getGetMembersQueryKey } from '@/modules/workspaces/api'
import { TeamPage } from '@/modules/workspaces/components/team-page'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_protected/workspaces/$workspaceId/team')({
  component: RouteComponent,
  loader: async ({ params }) => {
    queryClient.prefetchQuery({ queryKey: getGetByIdQueryKey(params.workspaceId) });
    queryClient.prefetchQuery({ queryKey: getGetMembersQueryKey(params.workspaceId) });
  }
})

function RouteComponent() {
  const { workspaceId } = Route.useParams();
  return <TeamPage workspaceId={workspaceId} />
}
