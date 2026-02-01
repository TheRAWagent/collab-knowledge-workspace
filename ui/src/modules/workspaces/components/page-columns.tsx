import type { ColumnDef } from "@tanstack/react-table";
import { Button } from "@/components/ui/button";
import { Edit, Trash2, FileText } from "lucide-react";
import { type DocumentResponse } from "@/modules/pages/api";
import { Link } from "@tanstack/react-router";

export interface PageColumnsProps {
  onEdit: (page: DocumentResponse) => void;
  onDelete: (page: DocumentResponse) => void;
}

export const createPageColumns = ({
  onEdit,
  onDelete,
}: PageColumnsProps): ColumnDef<DocumentResponse>[] => [
    {
      accessorKey: "title",
      header: "Title",
      cell: ({ row }) => {
        const page = row.original;
        return (
          <Link to="/workspaces/$workspaceId/pages/$pageId" params={{pageId: page.id, workspaceId: page.workspaceId}} className="flex items-center gap-2">
            <FileText className="h-4 w-4 text-muted-foreground" />
            <span className="font-medium">{page.title || "Untitled"}</span>
          </Link>
        );
      },
    },
    {
      accessorKey: "updatedAt",
      header: "Last Updated",
      cell: ({ row }) => {
        const dateStr = row.getValue("updatedAt") as string | undefined;
        if (!dateStr) return <span className="text-sm text-muted-foreground">-</span>;

        const date = new Date(dateStr);
        return (
          <span className="text-sm text-muted-foreground">
            {date.toLocaleDateString()} {date.toLocaleTimeString()}
          </span>
        );
      },
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const page = row.original;
        return (
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => onEdit(page)}
              className="h-8 w-8 p-0"
            >
              <Edit className="h-4 w-4" />
              <span className="sr-only">Edit</span>
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => onDelete(page)}
              className="h-8 w-8 p-0 text-destructive hover:text-destructive"
            >
              <Trash2 className="h-4 w-4" />
              <span className="sr-only">Delete</span>
            </Button>
          </div>
        );
      },
    },
  ];
