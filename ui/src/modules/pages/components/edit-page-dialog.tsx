import { ResponsiveDialog } from "@/components/responsive-dialog";
import { PageForm } from "./page-form";
import { type DocumentResponse } from "../api";

interface EditPageDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  workspaceId: string;
  page: DocumentResponse;
}

export function EditPageDialog({
  open,
  onOpenChange,
  workspaceId,
  page,
}: EditPageDialogProps) {
  return (
    <ResponsiveDialog
      open={open}
      onOpenChange={onOpenChange}
      title="Edit Page"
      description="Update the details of this page."
    >
      <PageForm
        workspaceId={workspaceId}
        initialValues={page}
        onSuccess={() => onOpenChange(false)}
        onCancel={() => onOpenChange(false)}
      />
    </ResponsiveDialog>
  );
}
