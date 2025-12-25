import { ResponsiveDialog } from "@/components/responsive-dialog";
import { WorkspaceForm } from "./workspace-form";
import { type WorkspaceResponse } from "../api";

interface UpdateWorkspaceDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  initialValues: WorkspaceResponse;
}

export function UpdateWorkspaceDialog({
  open,
  onOpenChange,
  initialValues,
}: UpdateWorkspaceDialogProps) {
  return (
    <ResponsiveDialog open={open} onOpenChange={onOpenChange} title="Update Workspace" description="Update the details of your workspace.">
      <WorkspaceForm
        initialValues={initialValues}
        onSuccess={() => onOpenChange(false)}
        onCancel={() => onOpenChange(false)}
      />
    </ResponsiveDialog>
  );
}
