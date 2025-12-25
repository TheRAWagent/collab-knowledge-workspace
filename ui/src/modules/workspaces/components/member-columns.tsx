import type { ColumnDef } from "@tanstack/react-table";
import { Button } from "@/components/ui/button";
import { Shield, Eye } from "lucide-react";
import { type WorkspaceMember, WorkspaceMemberRole } from "../api";
import { Badge } from "@/components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";

export interface MemberColumnsProps {
  onViewPermissions?: (member: WorkspaceMember) => void;
}

const getRoleBadgeVariant = (role: WorkspaceMemberRole) => {
  switch (role) {
    case WorkspaceMemberRole.OWNER:
      return "default";
    case WorkspaceMemberRole.EDITOR:
      return "secondary";
    case WorkspaceMemberRole.READER:
      return "outline";
    default:
      return "outline";
  }
};

const getRolePermissions = (role: WorkspaceMemberRole): string[] => {
  switch (role) {
    case WorkspaceMemberRole.OWNER:
      return [
        "Full workspace access",
        "Manage workspace settings",
        "Add/remove members",
        "Assign roles",
        "Create, edit, and delete pages",
        "View all content",
      ];
    case WorkspaceMemberRole.EDITOR:
      return [
        "Create and edit pages",
        "View all content",
        "Cannot manage workspace settings",
        "Cannot manage members",
      ];
    case WorkspaceMemberRole.READER:
      return [
        "View all content",
        "Cannot create or edit pages",
        "Cannot manage workspace settings",
        "Cannot manage members",
      ];
    default:
      return ["Unknown role"];
  }
};

export const createMemberColumns = (): ColumnDef<WorkspaceMember>[] => [
  {
    accessorKey: "userId",
    header: "User ID",
    cell: ({ row }) => {
      const userId = row.getValue("userId") as string;
      return (
        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded-full bg-primary/10 text-primary text-xs font-semibold">
            {userId.substring(0, 2).toUpperCase()}
          </div>
          <span className="font-medium">{userId}</span>
        </div>
      );
    },
  },
  {
    accessorKey: "role",
    header: "Role",
    cell: ({ row }) => {
      const role = row.getValue("role") as WorkspaceMemberRole;
      return (
        <Badge variant={getRoleBadgeVariant(role)} className="gap-1">
          <Shield className="h-3 w-3" />
          {role}
        </Badge>
      );
    },
  },
  {
    id: "permissions",
    header: "Permissions",
    cell: ({ row }) => {
      const member = row.original;
      const permissions = getRolePermissions(member.role);

      return (
        <Dialog>
          <DialogTrigger asChild>
            <Button
              variant="ghost"
              size="sm"
              className="h-8 w-8 p-0"
              title="View permissions"
            >
              <Eye className="h-4 w-4" />
              <span className="sr-only">View permissions</span>
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>
                {member.role} Permissions
              </DialogTitle>
              <DialogDescription>
                Permissions for user {member.userId}
              </DialogDescription>
            </DialogHeader>
            <div className="space-y-2 mt-4">
              {permissions.map((permission, index) => (
                <div
                  key={index}
                  className="flex items-start gap-2 text-sm"
                >
                  <div className="mt-1 h-1.5 w-1.5 rounded-full bg-primary shrink-0" />
                  <span>{permission}</span>
                </div>
              ))}
            </div>
          </DialogContent>
        </Dialog>
      );
    },
  },
];
