import { ResponsiveDialog } from "@/components/responsive-dialog";
import { WorkspaceForm } from "./workspace-form";

interface CreateWorkspaceDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function CreateWorkspaceDialog({
  open,
  onOpenChange,
}: CreateWorkspaceDialogProps) {
  return (
    <ResponsiveDialog open={open} onOpenChange={onOpenChange} title="Create Workspace" description="Create a new workspace to collaborate with your team.">
      <WorkspaceForm
        onSuccess={() => onOpenChange(false)}
        onCancel={() => onOpenChange(false)}
      />
    </ResponsiveDialog>
  );
}
