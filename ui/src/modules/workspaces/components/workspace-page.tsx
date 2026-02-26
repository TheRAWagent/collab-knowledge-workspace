import { useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { useGetById } from "../api";
import {
  useDeletePage,
  getGetPagesQueryOptions,
  type DocumentResponse,
  useGetPages,
} from "@/modules/pages/api";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { CreatePageDialog } from "@/modules/pages/components/create-page-dialog";
import { EditPageDialog } from "@/modules/pages/components/edit-page-dialog";
import { toast } from "sonner";
import { useConfirm } from "@/hooks/use-confirm";
import { PageTree } from "./page-tree";

export function WorkspacePage({ workspaceId }: { workspaceId: string }) {
  const { data: workspace, isLoading: isWorkspaceLoading } =
    useGetById(workspaceId);

  const { data: pagesData, isLoading: isPagesLoading } = useGetPages(workspaceId);

  const queryClient = useQueryClient();
  const { mutate: deletePage } = useDeletePage({
    mutation: {
      onSuccess: () => {
        toast.success("Page deleted");
      },
      onSettled: () => {
        queryClient.invalidateQueries({
          queryKey: getGetPagesQueryOptions(workspaceId).queryKey,
        });
      },
    },
  });

  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
  const [editingPage, setEditingPage] = useState<DocumentResponse | null>(null);

  const [RemoveConfirmation, confirmRemove] = useConfirm(
    "Are you sure you want to delete this page?",
    "This action cannot be undone. All associated data will be permanently deleted."
  );

  const handleEdit = (page: DocumentResponse) => {
    setEditingPage(page);
  };

  const handleDelete = async (page: DocumentResponse) => {
    const ok = await confirmRemove();
    if (!ok) return;
    deletePage({ workspaceId, pageId: page.id });
  };

  const handleCreateNew = (_directoryId?: string | null) => {
    setIsCreateDialogOpen(true);
  };

  const handlePageCreated = () => {
    setIsCreateDialogOpen(false);
  };

  if (isWorkspaceLoading || isPagesLoading) {
    return (
      <div className="flex items-center justify-center h-full flex-1">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent" />
      </div>
    );
  }

  if (!workspace) {
    return (
      <div className="flex items-center justify-center h-full">
        <p className="text-muted-foreground">Workspace not found</p>
      </div>
    );
  }

  const pages = pagesData ?? [];

  return (
    <div className="flex flex-col h-full">
      <CreatePageDialog
        open={isCreateDialogOpen}
        onOpenChange={setIsCreateDialogOpen}
        workspaceId={workspaceId}
        onCreated={handlePageCreated}
      />

      {editingPage && (
        <EditPageDialog
          open={!!editingPage}
          onOpenChange={(open) => !open && setEditingPage(null)}
          workspaceId={workspaceId}
          page={editingPage}
        />
      )}

      <RemoveConfirmation />

      {/* Header */}
      <div className="border-b bg-background/95 backdrop-blur supports-backdrop-filter:bg-background/60 shrink-0">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold tracking-tight">
                {workspace.name}
              </h1>
              {workspace.description && (
                <p className="text-muted-foreground text-sm mt-0.5">
                  {workspace.description}
                </p>
              )}
            </div>
            <Button onClick={() => handleCreateNew(null)} className="gap-2" size="sm">
              <Plus className="h-4 w-4" />
              New Page
            </Button>
          </div>
        </div>
      </div>

      {/* Content — tree view */}
      <div className="flex-1 overflow-auto">
        <div className="container mx-auto px-4 py-6 max-w-3xl">
          <div className="rounded-xl border border-border bg-card p-4 shadow-sm">
            <PageTree
              workspaceId={workspaceId}
              pages={pages}
              onEditPage={handleEdit}
              onDeletePage={handleDelete}
              onCreatePage={handleCreateNew}
            />
          </div>
        </div>
      </div>
    </div>
  );
}
