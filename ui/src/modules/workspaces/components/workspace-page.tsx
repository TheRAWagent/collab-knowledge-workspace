import { useState } from "react";
import { useGetById } from "../api";
import { useGetPages, useDeletePage, getGetPagesQueryOptions, type DocumentResponse } from "@/modules/pages/api";
import { Button } from "@/components/ui/button";
import { DataTable } from "@/components/ui/data-table";
import { Plus, FileText } from "lucide-react";
import { createPageColumns } from "./page-columns";
import { CreatePageDialog } from "@/modules/pages/components/create-page-dialog";
import { EditPageDialog } from "@/modules/pages/components/edit-page-dialog";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useConfirm } from "@/hooks/use-confirm";

export function WorkspacePage({ workspaceId }: { workspaceId: string }) {
  const { data: workspace, isLoading: isWorkspaceLoading } = useGetById(workspaceId);
  const { data: pagesData, isLoading: isPagesLoading } = useGetPages(workspaceId);

  const queryClient = useQueryClient();
  const { mutate: deletePage } = useDeletePage({
    mutation: {
      onSuccess: () => {
        toast.success("Page deleted");
      },
      onSettled: () => {
        queryClient.invalidateQueries({ queryKey: getGetPagesQueryOptions(workspaceId).queryKey });
      }
    }
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
    if(!ok) return;
    deletePage({ workspaceId, pageId: page.id });
  };

  const handleCreateNew = () => {
    setIsCreateDialogOpen(true);
  };

  const columns = createPageColumns({
    onEdit: handleEdit,
    onDelete: handleDelete,
  });

  if (isWorkspaceLoading || isPagesLoading) {
    return (
      <div className="flex items-center justify-center h-full flex-1">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent" />
      </div>
    );
  }

  if (!workspace?.data) {
    return (
      <div className="flex items-center justify-center h-full">
        <p className="text-muted-foreground">Workspace not found</p>
      </div>
    );
  }

  const pages = pagesData?.data || [];

  return (
    <div className="flex flex-col h-full">
      <CreatePageDialog
        open={isCreateDialogOpen}
        onOpenChange={setIsCreateDialogOpen}
        workspaceId={workspaceId}
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
      <div className="border-b bg-background/95 backdrop-blur supports-backdrop-filter:bg-background/60">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold tracking-tight">
                {workspace.data.name}
              </h1>
              {workspace.data.description && (
                <p className="text-muted-foreground mt-1">
                  {workspace.data.description}
                </p>
              )}
            </div>
            <Button onClick={handleCreateNew} className="gap-2">
              <Plus className="h-4 w-4" />
              Create New Page
            </Button>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-auto">
        <div className="container mx-auto px-4 py-6">
          {pages.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <div className="rounded-full bg-muted p-6 mb-4">
                <FileText className="h-12 w-12 text-muted-foreground" />
              </div>
              <h3 className="text-lg font-semibold mb-2">No pages yet</h3>
              <p className="text-muted-foreground mb-6 max-w-sm">
                Get started by creating your first page in this workspace.
              </p>
              <Button onClick={handleCreateNew} className="gap-2">
                <Plus className="h-4 w-4" />
                Create Your First Page
              </Button>
            </div>
          ) : (
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-semibold">
                  Pages ({pages.length})
                </h2>
              </div>
              <DataTable columns={columns} data={pages} />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
