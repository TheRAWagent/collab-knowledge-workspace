import { ResponsiveDialog } from "@/components/responsive-dialog";
import { DirectoryForm } from "./directory-form";

interface CreateDirectoryDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  workspaceId: string;
  parentId?: string | null;
  /** Called with the directory name when the user submits; caller handles API call */
  onCreated?: (name: string) => void;
}

export function CreateDirectoryDialog({
  open,
  onOpenChange,
  parentId: _parentId,
  onCreated,
}: CreateDirectoryDialogProps) {
  function handleSubmit(name: string) {
    onCreated?.(name);
    onOpenChange(false);
  }

  return (
    <ResponsiveDialog
      open={open}
      onOpenChange={onOpenChange}
      title="Create Directory"
      description="Create a new directory to organize your pages."
    >
      <DirectoryForm
        onSubmit={handleSubmit}
        onCancel={() => onOpenChange(false)}
        submitLabel="Create Directory"
      />
    </ResponsiveDialog>
  );
}
