import { ResponsiveDialog } from "@/components/responsive-dialog";
import { PageForm } from "./page-form";

interface CreatePageDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  workspaceId: string;
  /** Called after successful creation */
  onCreated?: () => void;
}

export function CreatePageDialog({
  open,
  onOpenChange,
  workspaceId,
  onCreated,
}: CreatePageDialogProps) {
  return (
    <ResponsiveDialog
      open={open}
      onOpenChange={onOpenChange}
      title="Create Page"
      description="Create a new page in this workspace."
    >
      <PageForm
        workspaceId={workspaceId}
        onSuccess={() => {
          onCreated?.();
          onOpenChange(false);
        }}
        onCancel={() => onOpenChange(false)}
      />
    </ResponsiveDialog>
  );
}
