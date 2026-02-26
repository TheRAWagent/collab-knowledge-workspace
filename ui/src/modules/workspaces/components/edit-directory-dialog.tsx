import { ResponsiveDialog } from "@/components/responsive-dialog";
import { DirectoryForm } from "./directory-form";
import { type DirectoryResponse } from "../directory-api";

interface EditDirectoryDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  directory: DirectoryResponse;
  /** Called with the new name when the user submits; caller handles API call */
  onUpdated?: (name: string) => void;
}

export function EditDirectoryDialog({
  open,
  onOpenChange,
  directory,
  onUpdated,
}: EditDirectoryDialogProps) {
  function handleSubmit(name: string) {
    onUpdated?.(name);
    onOpenChange(false);
  }

  return (
    <ResponsiveDialog
      open={open}
      onOpenChange={onOpenChange}
      title="Rename Directory"
      description="Update the name of this directory."
    >
      <DirectoryForm
        initialName={directory.name}
        onSubmit={handleSubmit}
        onCancel={() => onOpenChange(false)}
        submitLabel="Rename"
      />
    </ResponsiveDialog>
  );
}
