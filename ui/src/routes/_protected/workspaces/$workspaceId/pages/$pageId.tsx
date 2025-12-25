import { createFileRoute } from '@tanstack/react-router'
import { PageEditorContainer } from '@/modules/pages/components/page-editor-container'

export const Route = createFileRoute(
  '/_protected/workspaces/$workspaceId/pages/$pageId',
)({
  component: RouteComponent,
})

function RouteComponent() {
  const { workspaceId, pageId } = Route.useParams()
  return <PageEditorContainer workspaceId={workspaceId} pageId={pageId} />
}
