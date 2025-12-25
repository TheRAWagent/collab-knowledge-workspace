import { FolderPlus } from "lucide-react";

export function EmptyUI() {
  return (
    <div className="h-full min-h-screen w-full flex flex-col items-center justify-center p-8 text-center animate-in fade-in zoom-in duration-500 bg-background">
      <div className="bg-muted p-6 rounded-full mb-6">
        <FolderPlus className="w-12 h-12 text-muted-foreground" />
      </div>

      <h2 className="text-2xl font-semibold tracking-tight mb-2 text-foreground">
        No Workspace Selected
      </h2>

      <p className="text-muted-foreground max-w-sm mb-8 leading-relaxed">
        Get started by creating a new workspace to organize your knowledge, or open an existing one to continue where you left off.
      </p>

      <p className="text-muted-foreground max-w-sm mb-8 leading-relaxed">
        To get started, create a new workspace or open an existing one.
      </p>
    </div>
  );
}
