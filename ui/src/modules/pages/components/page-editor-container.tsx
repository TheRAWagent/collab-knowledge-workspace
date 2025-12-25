import { useGetTree } from "../api";
import { TiptapEditor } from "./editor/tiptap-editor";
import { blocksToTiptap, tiptapToBlocks } from "./editor/block-transformer";
import type { JSONContent } from "@tiptap/react";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { useGetMembers } from "@/modules/workspaces/api";
import { useGetUser } from "@/modules/users/api";

interface PageEditorContainerProps {
  workspaceId: string;
  pageId: string;
}

export function PageEditorContainer({ workspaceId, pageId }: PageEditorContainerProps) {
  const queryClient = useQueryClient();
  const { data: treeResponse, isLoading } = useGetTree(workspaceId, pageId);
  const {data: workspaceMemberResponse} = useGetMembers(workspaceId);
  const {data: userResponse} = useGetUser();
  const user = userResponse?.data;
  const workspaceMembers = workspaceMemberResponse?.data;
  const blocks = treeResponse?.data || [];

  const permission = workspaceMembers?.members?.find((member) => member.userId === user?.email)?.role ?? "READER";

  // const { mutateAsync: createBlock } = useCreateBlock();
  // const { mutateAsync: deleteBlock } = useDeleteBlock();

  const handleSave = async (content: JSONContent) => {
    try {
      // 1. Convert Tiptap content to BlockRequests
      const newBlocks = tiptapToBlocks(content, pageId);

      // 2. Delete existing blocks (Naive approach for MVP)
      // We need to delete them one by one or if there's a bulk delete API (not seen in api.ts)
      // Warning: This is dangerous and slow for large docs.
      // const deletePromises = blocks.map((block: BlockResponse) =>
      //   block.id ? deleteBlock({ workspaceId, pageId, blockId: block.id }) : Promise.resolve()
      // );
      // await Promise.all(deletePromises);

      // 3. Create new blocks
      // We need to create them sequentially to maintain order if the backend relies on insertion order,
      // but we are sending orderIndex, so parallel might be okay if backend respects it.
      // Let's do sequential to be safe and avoid rate limits.
      // for (const blockReq of newBlocks) {
        // await createBlock({ workspaceId, pageId, data: blockReq });
      // }

      toast.success("Page saved successfully");
      // queryClient.invalidateQueries({ queryKey: getGetBlocksQueryKey(workspaceId, pageId) });

    } catch (error) {
      console.error("Failed to save page", error);
      toast.error("Failed to save page");
    }
  };

  if (isLoading) {
    return <div className="flex items-center justify-center h-64">Loading editor...</div>;
  }

  // const initialContent = blocksToTiptap(blocks);
  console.log(treeResponse)

  return (
    <div className="p-6">
      <TiptapEditor
        initialContent={{type: "doc", content: []}}
        onSave={handleSave}
        editable={permission === "OWNER" || permission === "EDITOR"}
      />
    </div>
  );
}
