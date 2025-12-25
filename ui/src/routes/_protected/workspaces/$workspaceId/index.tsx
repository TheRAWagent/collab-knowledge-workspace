import { queryClient } from '@/lib/query-client'
import { getGetByIdQueryKey } from '@/modules/workspaces/api'
import { WorkspacePage } from '@/modules/workspaces/components/workspace-page'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_protected/workspaces/$workspaceId/')({
  component: RouteComponent,
  loader: async ({ params }) => {
    queryClient.prefetchQuery({ queryKey: getGetByIdQueryKey(params.workspaceId) });
  }
})

function RouteComponent() {
  const { workspaceId } = Route.useParams();
  return <WorkspacePage workspaceId={workspaceId} />
}
