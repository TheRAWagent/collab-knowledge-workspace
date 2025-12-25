import { useGetById, useGetMembers } from "../api";
import { DataTable } from "@/components/ui/data-table";
import { Users } from "lucide-react";
import { createMemberColumns } from "./member-columns";

export function TeamPage({ workspaceId }: { workspaceId: string }) {
  const { data: workspace, isLoading: isWorkspaceLoading } = useGetById(workspaceId);
  const { data: membersData, isLoading: isMembersLoading } = useGetMembers(workspaceId);

  const columns = createMemberColumns();

  if (isWorkspaceLoading || isMembersLoading) {
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
  const members = membersData?.data?.members || [];

  return (
    <div className="flex flex-col h-full">
      {/* Header */}
      <div className="border-b bg-background/95 backdrop-blur supports-backdrop-filter:bg-background/60">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold tracking-tight">
                Team Members
              </h1>
              <p className="text-muted-foreground mt-1">
                Manage members and permissions for {workspace.data.name}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-auto">
        <div className="container mx-auto px-4 py-6">
          {members.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <div className="rounded-full bg-muted p-6 mb-4">
                <Users className="h-12 w-12 text-muted-foreground" />
              </div>
              <h3 className="text-lg font-semibold mb-2">No members yet</h3>
              <p className="text-muted-foreground mb-6 max-w-sm">
                This workspace doesn't have any members yet.
              </p>
            </div>
          ) : (
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-semibold">
                  Members ({members.length})
                </h2>
              </div>
              <DataTable columns={columns} data={members} />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
