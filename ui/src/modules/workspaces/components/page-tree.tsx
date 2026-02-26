import { useState, useCallback, useMemo } from "react";
import {
  DndContext,
  DragOverlay,
  PointerSensor,
  useSensor,
  useSensors,
  type DragEndEvent,
  type DragStartEvent,
  type DragOverEvent,
  closestCenter,
} from "@dnd-kit/core";
import {
  SortableContext,
  useSortable,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import {
  ChevronRight,
  Folder,
  FolderOpen,
  FileText,
  Plus,
  MoreHorizontal,
  Edit,
  Trash2,
  FolderPlus,
  GripVertical,
} from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Link } from "@tanstack/react-router";
import { type DocumentResponse } from "@/modules/pages/api";
import {
  type DirectoryResponse,
  useGetDirectories,
  useCreateDirectoryMutation,
  useUpdateDirectoryMutation,
  useDeleteDirectoryMutation,
  useMovePageMutation,
  getGetDirectoriesQueryKey,
} from "../directory-api";
import { CreateDirectoryDialog } from "./create-directory-dialog";
import { EditDirectoryDialog } from "./edit-directory-dialog";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";

// ─── Types ───────────────────────────────────────────────────────────────────

interface PageTreeProps {
  workspaceId: string;
  pages: DocumentResponse[];
  onEditPage: (page: DocumentResponse) => void;
  onDeletePage: (page: DocumentResponse) => void;
  onCreatePage: (directoryId?: string | null) => void;
}

type DraggableItem =
  | { type: "page"; pageId: string }
  | { type: "directory"; directoryId: string };

// ─── Page Item ───────────────────────────────────────────────────────────────

interface PageItemProps {
  page: DocumentResponse;
  indent: number;
  isDragging?: boolean;
  onEdit: (page: DocumentResponse) => void;
  onDelete: (page: DocumentResponse) => void;
}

function PageItem({ page, indent, isDragging, onEdit, onDelete }: PageItemProps) {
  const { attributes, listeners, setNodeRef, transform, transition, isOver } =
    useSortable({
      id: `page-${page.id}`,
      data: { type: "page", pageId: page.id },
    });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={cn(
        "group flex items-center gap-1 rounded-md px-2 py-1.5 text-sm cursor-pointer select-none transition-all",
        isDragging && "opacity-40",
        isOver && "ring-1 ring-primary/50 bg-primary/5"
      )}
      {...attributes}
    >
      {/* Indent spacer */}
      <div style={{ width: indent * 16 }} className="shrink-0" />

      {/* Drag handle */}
      <button
        {...listeners}
        className={cn(
          "flex items-center justify-center h-5 w-5 rounded shrink-0 cursor-grab active:cursor-grabbing text-muted-foreground/40 hover:text-muted-foreground transition-colors",
          "opacity-0 group-hover:opacity-100"
        )}
        onClick={(e) => e.stopPropagation()}
      >
        <GripVertical className="h-3.5 w-3.5" />
      </button>

      {/* Page icon + link */}
      <Link
        to="/workspaces/$workspaceId/pages/$pageId"
        params={{ workspaceId: page.workspaceId, pageId: page.id }}
        className="flex items-center gap-2 flex-1 min-w-0 text-foreground/80 hover:text-foreground transition-colors"
        onClick={(e) => e.stopPropagation()}
      >
        <FileText className="h-3.5 w-3.5 shrink-0 text-muted-foreground" />
        <span className="truncate">{page.title || "Untitled"}</span>
      </Link>

      {/* Actions */}
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            variant="ghost"
            size="sm"
            className={cn(
              "h-6 w-6 p-0 rounded opacity-0 group-hover:opacity-100 shrink-0 transition-opacity"
            )}
            onClick={(e) => e.stopPropagation()}
          >
            <MoreHorizontal className="h-3.5 w-3.5" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-44">
          <DropdownMenuItem onClick={() => onEdit(page)}>
            <Edit className="h-4 w-4 mr-2" />
            Rename
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem
            className="text-destructive focus:text-destructive"
            onClick={() => onDelete(page)}
          >
            <Trash2 className="h-4 w-4 mr-2" />
            Delete
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  );
}

// ─── Directory Item ───────────────────────────────────────────────────────────

interface DirectoryItemProps {
  directory: DirectoryResponse;
  pages: DocumentResponse[];
  allDirectories: DirectoryResponse[];
  indent: number;
  isDragging?: boolean;
  activeDrag: DraggableItem | null;
  overDndId: string | null;
  onEditPage: (page: DocumentResponse) => void;
  onDeletePage: (page: DocumentResponse) => void;
  onCreatePage: (directoryId?: string | null) => void;
  onEditDir: (dir: DirectoryResponse) => void;
  onDeleteDir: (dir: DirectoryResponse) => void;
  onCreateSubDir: (parentId: string) => void;
}

function DirectoryItem({
  directory,
  pages,
  allDirectories,
  indent,
  isDragging,
  activeDrag,
  overDndId,
  onEditPage,
  onDeletePage,
  onCreatePage,
  onEditDir,
  onDeleteDir,
  onCreateSubDir,
}: DirectoryItemProps) {
  const [expanded, setExpanded] = useState(true);

  const { attributes, listeners, setNodeRef, transform, transition } =
    useSortable({
      id: `dir-${directory.id}`,
      data: { type: "directory", directoryId: directory.id },
    });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  const childDirs = allDirectories
    .filter((d) => d.parentId === directory.id)
    .sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0));

  const dirPages = pages
    .filter((p) => p.directoryId === directory.id)
    .sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0));

  const isDropTarget =
    activeDrag?.type === "page" && overDndId === `dir-${directory.id}`;

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={cn(isDragging && "opacity-40")}
      {...attributes}
    >
      {/* Directory row */}
      <div
        className={cn(
          "group flex items-center gap-1 rounded-md px-2 py-1.5 text-sm cursor-pointer select-none transition-all",
          isDropTarget && "ring-1 ring-primary/50 bg-primary/5"
        )}
        onClick={() => setExpanded((e) => !e)}
      >
        {/* Indent */}
        <div style={{ width: indent * 16 }} className="shrink-0" />

        {/* Drag handle for the directory itself */}
        <button
          {...listeners}
          className={cn(
            "flex items-center justify-center h-5 w-5 rounded shrink-0 cursor-grab active:cursor-grabbing text-muted-foreground/40 hover:text-muted-foreground transition-colors",
            "opacity-0 group-hover:opacity-100"
          )}
          onClick={(e) => e.stopPropagation()}
        >
          <GripVertical className="h-3.5 w-3.5" />
        </button>

        {/* Chevron */}
        <ChevronRight
          className={cn(
            "h-3.5 w-3.5 shrink-0 text-muted-foreground transition-transform duration-150",
            expanded && "rotate-90"
          )}
        />

        {/* Folder icon */}
        {expanded ? (
          <FolderOpen className="h-3.5 w-3.5 shrink-0 text-amber-500" />
        ) : (
          <Folder className="h-3.5 w-3.5 shrink-0 text-amber-500" />
        )}

        {/* Name */}
        <span className="flex-1 min-w-0 truncate font-medium text-foreground/80">
          {directory.name}
        </span>

        {/* Badge when collapsed */}
        {(dirPages.length > 0 || childDirs.length > 0) && !expanded && (
          <span className="text-xs text-muted-foreground/60 shrink-0">
            {dirPages.length + childDirs.length}
          </span>
        )}

        {/* Actions */}
        <div
          className="flex items-center opacity-0 group-hover:opacity-100 transition-opacity"
          onClick={(e) => e.stopPropagation()}
        >
          <Button
            variant="ghost"
            size="sm"
            className="h-6 w-6 p-0 rounded"
            title="Add page here"
            onClick={(e) => {
              e.stopPropagation();
              setExpanded(true);
              onCreatePage(directory.id);
            }}
          >
            <Plus className="h-3.5 w-3.5" />
          </Button>

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button
                variant="ghost"
                size="sm"
                className="h-6 w-6 p-0 rounded"
                onClick={(e) => e.stopPropagation()}
              >
                <MoreHorizontal className="h-3.5 w-3.5" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-48">
              <DropdownMenuItem
                onClick={(e) => {
                  e.stopPropagation();
                  onCreateSubDir(directory.id!);
                }}
              >
                <FolderPlus className="h-4 w-4 mr-2" />
                New Sub-directory
              </DropdownMenuItem>
              <DropdownMenuItem
                onClick={(e) => {
                  e.stopPropagation();
                  setExpanded(true);
                  onCreatePage(directory.id);
                }}
              >
                <Plus className="h-4 w-4 mr-2" />
                New Page Here
              </DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                onClick={(e) => {
                  e.stopPropagation();
                  onEditDir(directory);
                }}
              >
                <Edit className="h-4 w-4 mr-2" />
                Rename
              </DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                className="text-destructive focus:text-destructive"
                onClick={(e) => {
                  e.stopPropagation();
                  onDeleteDir(directory);
                }}
              >
                <Trash2 className="h-4 w-4 mr-2" />
                Delete
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>

      {/* Children */}
      {expanded && (
        <div>
          <SortableContext
            items={[
              ...dirPages.map((p) => `page-${p.id}`),
              ...childDirs.map((d) => `dir-${d.id}`),
            ]}
            strategy={verticalListSortingStrategy}
          >
            {dirPages.map((page) => (
              <PageItem
                key={page.id}
                page={page}
                indent={indent + 1}
                isDragging={
                  activeDrag?.type === "page" && activeDrag.pageId === page.id
                }
                onEdit={onEditPage}
                onDelete={onDeletePage}
              />
            ))}

            {childDirs.map((childDir) => (
              <DirectoryItem
                key={childDir.id}
                directory={childDir}
                pages={pages}
                allDirectories={allDirectories}
                indent={indent + 1}
                isDragging={
                  activeDrag?.type === "directory" &&
                  activeDrag.directoryId === childDir.id
                }
                activeDrag={activeDrag}
                overDndId={overDndId}
                onEditPage={onEditPage}
                onDeletePage={onDeletePage}
                onCreatePage={onCreatePage}
                onEditDir={onEditDir}
                onDeleteDir={onDeleteDir}
                onCreateSubDir={onCreateSubDir}
              />
            ))}
          </SortableContext>

          {/* Empty state */}
          {dirPages.length === 0 && childDirs.length === 0 && (
            <div className="py-1" style={{ paddingLeft: (indent + 1) * 16 + 8 }}>
              <span className="text-xs text-muted-foreground/50 italic">
                Empty — drag pages here or add one
              </span>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

// ─── Root Drop Zone ───────────────────────────────────────────────────────────

function RootDropzone({ isOver }: { isOver: boolean }) {
  const { setNodeRef } = useSortable({ id: "root-drop-zone" });
  return (
    <div
      ref={setNodeRef}
      className={cn(
        "h-8 rounded-md flex items-center justify-center text-xs transition-all duration-150 border border-dashed mx-1",
        isOver
          ? "border-primary/60 bg-primary/10 text-primary font-medium"
          : "border-transparent text-transparent"
      )}
    >
      Drop here to move to root
    </div>
  );
}

// ─── Page Tree (Main) ─────────────────────────────────────────────────────────

export function PageTree({
  workspaceId,
  pages,
  onEditPage,
  onDeletePage,
  onCreatePage,
}: PageTreeProps) {
  const queryClient = useQueryClient();

  // ── Server data ───────────────────────────────────────────────────────────
  const { data: dirData, isLoading: isDirsLoading } =
    useGetDirectories(workspaceId);
  const directories = useMemo(
    () => dirData ?? [],
    [dirData]
  );

  const invalidateDirs = useCallback(
    () =>
      queryClient.invalidateQueries({
        queryKey: getGetDirectoriesQueryKey(workspaceId),
      }),
    [queryClient, workspaceId]
  );

  const { mutate: createDir } = useCreateDirectoryMutation(workspaceId, {
    mutation: {
      onSuccess: () => invalidateDirs(),
      onError: () => toast.error("Failed to create directory"),
    },
  });

  const { mutate: updateDir } = useUpdateDirectoryMutation(workspaceId, {
    mutation: {
      onSuccess: () => invalidateDirs(),
      onError: () => toast.error("Failed to rename directory"),
    },
  });

  const { mutate: deleteDir } = useDeleteDirectoryMutation(workspaceId, {
    mutation: {
      onSuccess: () => { invalidateDirs(); },
      onError: () => toast.error("Failed to delete directory"),
    },
  });

  const { mutate: movePageMutate } = useMovePageMutation(workspaceId, {
    mutation: {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: [`/workspace/${workspaceId}/documents`] });
        toast.success("Page moved");
      },
      onError: () => toast.error("Failed to move page"),
    },
  });

  // ── Drag state ────────────────────────────────────────────────────────────
  const [activeDragItem, setActiveDragItem] = useState<DraggableItem | null>(null);
  const [overDndId, setOverDndId] = useState<string | null>(null);

  // ── Pending move (confirmation dialog) ───────────────────────────────────
  const [pendingMove, setPendingMove] = useState<{
    pageId: string;
    toDirId: string | null;
  } | null>(null);

  // ── Dialog state ─────────────────────────────────────────────────────────
  const [createDirOpen, setCreateDirOpen] = useState(false);
  const [createDirParentId, setCreateDirParentId] = useState<string | null>(null);
  const [editingDir, setEditingDir] = useState<DirectoryResponse | null>(null);

  // ── Derived data ──────────────────────────────────────────────────────────
  const rootDirectories = useMemo(
    () =>
      directories
        .filter((d) => d.parentId === null)
        .sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0)),
    [directories]
  );

  const rootPages = useMemo(
    () =>
      pages
        .filter((p) => !p.directoryId)
        .sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0)),
    [pages]
  );

  // ── dnd-kit sensors ───────────────────────────────────────────────────────
  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 8 } })
  );

  const handleDragStart = useCallback(({ active }: DragStartEvent) => {
    const data = active.data.current as { type: string; pageId?: string; directoryId?: string };
    if (data?.type === "page") {
      setActiveDragItem({ type: "page", pageId: data.pageId! });
    } else if (data?.type === "directory") {
      setActiveDragItem({ type: "directory", directoryId: data.directoryId! });
    }
  }, []);

  const handleDragOver = useCallback(({ over }: DragOverEvent) => {
    setOverDndId(over?.id ? String(over.id) : null);
  }, []);

  const handleDragEnd = useCallback(
    ({ active, over }: DragEndEvent) => {
      setActiveDragItem(null);
      setOverDndId(null);

      if (!over || !active || active.id === over.id) return;

      const activeData = active.data.current as {
        type: string;
        pageId?: string;
      };

      if (activeData?.type !== "page") return;

      const pageId = activeData.pageId!;
      const overIdStr = String(over.id);

      // Determine target directory
      let targetDirId: string | null = null;
      if (
        overIdStr === "root-drop-zone" ||
        (overIdStr.startsWith("page-") &&
          rootPages.some((p) => `page-${p.id}` === overIdStr))
      ) {
        targetDirId = null; // root
      } else if (overIdStr.startsWith("dir-")) {
        targetDirId = overIdStr.replace("dir-", "");
      } else if (overIdStr.startsWith("page-")) {
        // dropped near another page — determine which dir it belongs to
        const otherPageId = overIdStr.replace("page-", "");
        const otherPage = pages.find((p) => p.id === otherPageId);
        targetDirId = otherPage?.directoryId ?? null;
      }

      // Find current directory
      const draggingPage = pages.find((p) => p.id === pageId);
      const currentDirId = draggingPage?.directoryId ?? null;

      // Same directory → no cross-move needed (could handle reordering here)
      if (targetDirId === currentDirId) return;

      // Show confirmation
      setPendingMove({ pageId, toDirId: targetDirId });
    },
    [rootPages, pages]
  );

  const handleMoveConfirm = useCallback(() => {
    if (!pendingMove) return;
    movePageMutate({ pageId: pendingMove.pageId, data: { directoryId: pendingMove.toDirId ?? undefined } });
    setPendingMove(null);
  }, [pendingMove, movePageMutate]);

  const handleMoveCancel = useCallback(() => {
    setPendingMove(null);
  }, []);

  // ── Helpers ───────────────────────────────────────────────────────────────
  const getDirName = (dirId: string | null) => {
    if (dirId === null) return "Root";
    return directories.find((d) => d.id === dirId)?.name ?? "Unknown";
  };

  const draggingPage = activeDragItem?.type === "page"
    ? pages.find((p) => p.id === activeDragItem.pageId)
    : null;
  const draggingDir = activeDragItem?.type === "directory"
    ? directories.find((d) => d.id === activeDragItem.directoryId)
    : null;

  return (
    <>
      {/* Create directory dialog */}
      <CreateDirectoryDialog
        open={createDirOpen}
        onOpenChange={setCreateDirOpen}
        workspaceId={workspaceId}
        parentId={createDirParentId}
        onCreated={(name) => {
          createDir({ name, parentId: createDirParentId ?? undefined });
          setCreateDirOpen(false);
        }}
      />

      {/* Edit directory dialog */}
      {editingDir && (
        <EditDirectoryDialog
          open={!!editingDir}
          onOpenChange={(open) => !open && setEditingDir(null)}
          directory={editingDir}
          onUpdated={(name) => {
            updateDir({ directoryId: editingDir.id!, data: { name } });
            setEditingDir(null);
          }}
        />
      )}

      {/* Move confirm dialog */}
      {pendingMove && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          <div
            className="absolute inset-0 bg-black/50 backdrop-blur-sm"
            onClick={handleMoveCancel}
          />
          <div className="relative bg-card border border-border rounded-xl shadow-2xl p-6 max-w-sm w-full mx-4 z-10">
            <div className="flex flex-col gap-4">
              <div>
                <h3 className="text-base font-semibold">Move Page?</h3>
                <p className="text-sm text-muted-foreground mt-1">
                  Move{" "}
                  <strong>
                    {pages.find((p) => p.id === pendingMove.pageId)?.title || "Untitled"}
                  </strong>{" "}
                  to{" "}
                  <strong>{getDirName(pendingMove.toDirId)}</strong>?
                </p>
              </div>
              <div className="flex gap-2">
                <Button variant="outline" className="flex-1" onClick={handleMoveCancel}>
                  Cancel
                </Button>
                <Button className="flex-1" onClick={handleMoveConfirm}>
                  Move
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Tree */}
      <DndContext
        sensors={sensors}
        collisionDetection={closestCenter}
        onDragStart={handleDragStart}
        onDragOver={handleDragOver}
        onDragEnd={handleDragEnd}
      >
        <div className="select-none">
          {/* Toolbar */}
          <div className="flex items-center justify-between mb-3 px-1">
            <span className="text-xs font-semibold text-muted-foreground uppercase tracking-wider">
              Pages
            </span>
            <div className="flex items-center gap-1">
              <Button
                variant="ghost"
                size="sm"
                className="h-7 px-2 gap-1.5 text-xs"
                type="button"
                onClick={() => {
                  setCreateDirParentId(null);
                  setCreateDirOpen(true);
                }}
              >
                <FolderPlus className="h-3.5 w-3.5" />
                New Directory
              </Button>
              <Button
                variant="ghost"
                size="sm"
                className="h-7 px-2 gap-1.5 text-xs"
                type="button"
                onClick={() => onCreatePage(null)}
              >
                <Plus className="h-3.5 w-3.5" />
                New Page
              </Button>
            </div>
          </div>

          {isDirsLoading ? (
            <div className="flex items-center justify-center py-8">
              <div className="h-5 w-5 animate-spin rounded-full border-2 border-primary border-t-transparent" />
            </div>
          ) : (
            <SortableContext
              items={[
                ...rootPages.map((p) => `page-${p.id}`),
                ...rootDirectories.map((d) => `dir-${d.id}`),
                "root-drop-zone",
              ]}
              strategy={verticalListSortingStrategy}
            >
              {/* Root pages */}
              {rootPages.map((page) => (
                <PageItem
                  key={page.id}
                  page={page}
                  indent={0}
                  isDragging={
                    activeDragItem?.type === "page" &&
                    activeDragItem.pageId === page.id
                  }
                  onEdit={onEditPage}
                  onDelete={onDeletePage}
                />
              ))}

              {/* Root directories */}
              {rootDirectories.map((dir) => (
                <DirectoryItem
                  key={dir.id}
                  directory={dir}
                  pages={pages}
                  allDirectories={directories}
                  indent={0}
                  isDragging={
                    activeDragItem?.type === "directory" &&
                    activeDragItem.directoryId === dir.id
                  }
                  activeDrag={activeDragItem}
                  overDndId={overDndId}
                  onEditPage={onEditPage}
                  onDeletePage={onDeletePage}
                  onCreatePage={onCreatePage}
                  onEditDir={(d) => setEditingDir(d)}
                  onDeleteDir={(d) => deleteDir(d.id!, {
                    onSuccess: () => toast.success(`"${d.name}" deleted`),
                  })}
                  onCreateSubDir={(parentId) => {
                    setCreateDirParentId(parentId);
                    setCreateDirOpen(true);
                  }}
                />
              ))}

              {/* Root drop zone */}
              <RootDropzone
                isOver={
                  activeDragItem?.type === "page" &&
                  overDndId === "root-drop-zone"
                }
              />
            </SortableContext>
          )}

          {/* Empty state */}
          {!isDirsLoading && pages.length === 0 && directories.length === 0 && (
            <div className="py-8 text-center">
              <FileText className="h-8 w-8 text-muted-foreground/40 mx-auto mb-3" />
              <p className="text-sm text-muted-foreground">No pages yet</p>
              <p className="text-xs text-muted-foreground/60 mt-0.5">
                Create a page or directory to get started
              </p>
            </div>
          )}
        </div>

        {/* Drag overlay */}
        <DragOverlay>
          {(draggingPage || draggingDir) && (
            <div className="flex items-center gap-2 rounded-md bg-card border border-border shadow-xl px-3 py-2 text-sm font-medium">
              {draggingPage ? (
                <FileText className="h-3.5 w-3.5 text-muted-foreground" />
              ) : (
                <Folder className="h-3.5 w-3.5 text-amber-500" />
              )}
              {draggingPage?.title || draggingDir?.name || "Item"}
            </div>
          )}
        </DragOverlay>
      </DndContext>
    </>
  );
}
