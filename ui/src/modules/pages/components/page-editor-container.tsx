import { TiptapEditor } from "./editor/tiptap-editor";
import { useGetMembers, type WorkspaceMember } from "@/modules/workspaces/api";
import { useGetUser } from "@/modules/users/api";

interface PageEditorContainerProps {
  workspaceId: string;
  pageId: string;
}

export function PageEditorContainer({ workspaceId }: PageEditorContainerProps) {
  // const queryClient = useQueryClient();
  // const { data: treeResponse, isLoading } = useGetTree(workspaceId, pageId);
  const { data: workspaceMemberResponse, isLoading } = useGetMembers(workspaceId);
  const { data: userResponse } = useGetUser();
  const user = userResponse;
  const workspaceMembers = workspaceMemberResponse;
  // const blocks = treeResponse?.data || [];

  const permission = workspaceMembers?.members?.find((member: WorkspaceMember) => member.userId === user?.email)?.role ?? "READER";

  if (isLoading) {
    return <div className="flex items-center justify-center h-64">Loading editor...</div>;
  }

  return (
    <div className="p-6">
      <TiptapEditor
        initialContent={{ type: "doc", content: [] }}
        onSave={() => { }}
        editable={permission === "OWNER" || permission === "EDITOR"}
      />
    </div>
  );
}
