"use client"

import { useState } from "react"
import { Menu, X, Plus, Settings, LogOut, ChevronLeft, ChevronRight } from "lucide-react"
import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"
import { useList, type WorkspaceResponse } from "@/modules/workspaces/api"
import { useGetUser } from "@/modules/users/api"
import { CreateWorkspaceDialog } from "@/modules/workspaces/components/create-workspace-dialog"
import { Link } from "@tanstack/react-router"

interface SidebarProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function Sidebar({ open, onOpenChange }: SidebarProps) {
  const [expandedWorkspace, setExpandedWorkspace] = useState<string | null>(null);
  const { data: userResponse } = useGetUser();
  const { data: workspaceResponse } = useList({}, {});
  const [collapsed, setCollapsed] = useState(false);
  const [createWorkspaceDialogOpen, setCreateWorkspaceDialogOpen] = useState(false);

  const isWorkspaceActive = (workspace: WorkspaceResponse) => expandedWorkspace === workspace.id;

  return (
    <>
      <CreateWorkspaceDialog open={createWorkspaceDialogOpen} onOpenChange={setCreateWorkspaceDialogOpen} />

      {/* Mobile toggle button */}
      <Button
        variant="ghost"
        size="icon"
        className="fixed left-4 top-4 z-50 lg:hidden"
        onClick={() => onOpenChange(!open)}
      >
        {open ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
      </Button>

      {/* Overlay for mobile */}
      {open && <div className="fixed inset-0 z-30 bg-black/50 lg:hidden" onClick={() => onOpenChange(false)} />}

      {/* Sidebar */}
      <aside
        className={cn(
          "absolute left-0 top-0 z-40 h-screen border-r border-sidebar-border bg-sidebar text-sidebar-foreground transition-all duration-300 ease-in-out hidden lg:flex lg:flex-col",
          collapsed ? "w-20" : "w-64",
        )}
      >
        {/* Header */}
        <div className="border-b border-sidebar-border px-6 py-6">
          <div className={cn("flex items-center gap-3", collapsed && "justify-center")}>
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground font-bold shrink-0">
              {userResponse?.data.name?.charAt(0)}
            </div>
            {!collapsed && (
              <div>
                <h1 className="text-lg font-semibold">{userResponse?.data.name}</h1>
              </div>
            )}
          </div>
        </div>

        {/* Workspaces */}
        <div className="flex-1 overflow-y-auto px-3 py-4">
          {!collapsed && (
            <div className="mb-4">
              <p className="px-3 text-xs font-semibold text-sidebar-foreground/60 uppercase">Workspaces</p>
            </div>
          )}

          <div className="space-y-2">
            {workspaceResponse?.data.content?.map((workspace) => (
              <div key={workspace.id}>
                <button
                  onClick={() => setExpandedWorkspace(expandedWorkspace === workspace.id ? null : workspace.id)}
                  className={cn(
                    "flex items-center gap-3 rounded-lg px-3 py-3 text-sm font-medium transition-colors",
                    collapsed ? "justify-center w-full" : "w-full",
                    isWorkspaceActive(workspace)
                      ? "bg-sidebar-primary text-sidebar-primary-foreground"
                      : "text-sidebar-foreground hover:bg-sidebar-accent",
                  )}
                  title={collapsed ? workspace.name : undefined}
                >
                  <span className="text-lg shrink-0">D</span>
                  {!collapsed && (
                    <>
                      <span className="flex-1 text-left">{workspace.name}</span>
                      {collapsed ? null : (
                        <ChevronRight
                          className={cn(
                            "ml-auto h-4 w-4 shrink-0 transition-transform duration-200",
                            isWorkspaceActive(workspace) && "rotate-90",
                          )}
                        />
                      )}
                    </>
                  )}
                </button>

                {/* Workspace Pages - only show when expanded */}
                {!collapsed && expandedWorkspace === workspace.id && (
                  <div className="mt-2 space-y-1 pl-8 flex flex-col">
                    <Link to={"/workspaces/$workspaceId"} params={{ workspaceId: workspace.id }} className="w-full rounded px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent text-left transition-colors">
                      📄 Overview
                    </Link>
                    {/* <button className="w-full rounded px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent text-left transition-colors">
                      📋 Projects
                    </button> */}
                    <Link to={"/workspaces/$workspaceId/team"} params={{ workspaceId: workspace.id }} className="w-full rounded px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent text-left transition-colors">
                      👥 Team
                    </Link>
                    {/* <button className="w-full rounded px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent text-left transition-colors">
                      📊 Analytics
                    </button> */}
                  </div>
                )}
              </div>
            ))}
          </div>

          {/* Create new workspace */}
          {!collapsed && (
            <button className="mt-4 w-full flex items-center justify-center gap-2 rounded-lg border border-sidebar-border px-3 py-3 text-sm font-medium text-sidebar-foreground transition-colors hover:bg-sidebar-accent" onClick={() => setCreateWorkspaceDialogOpen(true)}>
              <Plus className="h-4 w-4" />
              New Workspace
            </button>
          )}
        </div>

        {/* Footer */}
        <div className="border-t border-sidebar-border px-3 py-4 space-y-2">
          <div className={cn("space-y-2", collapsed && "flex flex-col items-center gap-2")}>
            <button
              onClick={() => setCreateWorkspaceDialogOpen(true)}
              className={cn(
                "flex items-center gap-3 rounded-lg px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent transition-colors",
                collapsed && "h-10 w-10 justify-center p-0",
              )}
              title={collapsed ? "Add workspace" : undefined}
            >
              <Plus className="h-4 w-4 shrink-0" />
              {!collapsed && "New Workspace"}
            </button>

            <button
              className={cn(
                "flex items-center gap-3 rounded-lg px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent transition-colors",
                collapsed && "h-10 w-10 justify-center p-0",
              )}
              title={collapsed ? "Settings" : undefined}
            >
              <Settings className="h-4 w-4 shrink-0" />
              {!collapsed && "Settings"}
            </button>

            <button
              className={cn(
                "flex items-center gap-3 rounded-lg px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent transition-colors",
                collapsed && "h-10 w-10 justify-center p-0",
              )}
              title={collapsed ? "Logout" : undefined}
            >
              <LogOut className="h-4 w-4 shrink-0" />
              {!collapsed && "Logout"}
            </button>
          </div>

          {collapsed && <div className="my-2 h-px bg-sidebar-border" />}

          <div className={cn("flex items-center", collapsed ? "justify-center" : "justify-between pt-2")}>
            {!collapsed && (
              <div className="flex h-8 w-8 items-center justify-center rounded-full bg-sidebar-primary text-sidebar-primary-foreground text-xs font-semibold">
                {userResponse?.data.name?.charAt(0)}
              </div>
            )}
            <button
              onClick={() => setCollapsed(!collapsed)}
              className="flex items-center justify-center rounded-lg px-3 py-2 text-sm text-sidebar-foreground/70 hover:bg-sidebar-accent transition-colors"
              title={collapsed ? "Expand sidebar" : "Collapse sidebar"}
            >
              {collapsed ? <ChevronRight className="h-4 w-4" /> : <ChevronLeft className="h-4 w-4" />}
            </button>
          </div>
        </div>
      </aside>
    </>
  )
}
