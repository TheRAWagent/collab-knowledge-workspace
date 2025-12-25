import { ResponsiveDialog } from "@/components/responsive-dialog";
import { PageForm } from "./page-form";

interface CreatePageDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  workspaceId: string;
}

export function CreatePageDialog({
  open,
  onOpenChange,
  workspaceId,
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
        onSuccess={() => onOpenChange(false)}
        onCancel={() => onOpenChange(false)}
      />
    </ResponsiveDialog>
  );
}
