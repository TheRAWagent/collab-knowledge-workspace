/**
 * Directory API — strongly typed hooks for directory management.
 * Wraps orval-generated hooks from pages/api.ts.
 */
import { useQueryClient } from "@tanstack/react-query";
import type { QueryClient, UseQueryResult } from "@tanstack/react-query";
import {
  useListDirectories,
  useCreateDirectory,
  useUpdateDirectory,
  useDeleteDirectory,
  useMovePage,
  getListDirectoriesQueryOptions,
} from "@/modules/pages/api";
import type {
  DirectoryResponse,
  DirectoryRequest,
  MovePageRequest
} from "@/modules/pages/api";

// ─── Query Keys ──────────────────────────────────────────────────────────────

export const getGetDirectoriesQueryKey = (workspaceId: string) =>
  getListDirectoriesQueryOptions(workspaceId).queryKey;

// ─── Hooks (wrapped to match expected component signatures) ──────────────────

export function useGetDirectories(workspaceId: string): UseQueryResult<DirectoryResponse[], any> {
  return useListDirectories(workspaceId) as UseQueryResult<DirectoryResponse[], any>;
}

export function useCreateDirectoryMutation(workspaceId: string, options?: any, queryClient?: QueryClient) {
  const qc = queryClient || useQueryClient();
  const mutation = useCreateDirectory(options, qc);

  return {
    ...mutation,
    mutate: (data: DirectoryRequest, mutateOptions?: any) =>
      mutation.mutate({ workspaceId, data }, mutateOptions),
    mutateAsync: (data: DirectoryRequest, mutateOptions?: any) =>
      mutation.mutateAsync({ workspaceId, data }, mutateOptions),
  };
}

export function useUpdateDirectoryMutation(workspaceId: string, options?: any, queryClient?: QueryClient) {
  const qc = queryClient || useQueryClient();
  const mutation = useUpdateDirectory(options, qc);

  return {
    ...mutation,
    mutate: ({ directoryId, data }: { directoryId: string; data: DirectoryRequest }, mutateOptions?: any) =>
      mutation.mutate({ workspaceId, directoryId, data }, mutateOptions),
    mutateAsync: ({ directoryId, data }: { directoryId: string; data: DirectoryRequest }, mutateOptions?: any) =>
      mutation.mutateAsync({ workspaceId, directoryId, data }, mutateOptions),
  };
}

export function useDeleteDirectoryMutation(workspaceId: string, options?: any, queryClient?: QueryClient) {
  const qc = queryClient || useQueryClient();
  const mutation = useDeleteDirectory(options, qc);

  return {
    ...mutation,
    mutate: (directoryId: string, mutateOptions?: any) =>
      mutation.mutate({ workspaceId, directoryId }, mutateOptions),
    mutateAsync: (directoryId: string, mutateOptions?: any) =>
      mutation.mutateAsync({ workspaceId, directoryId }, mutateOptions),
  };
}

export function useMovePageMutation(workspaceId: string, options?: any, queryClient?: QueryClient) {
  const qc = queryClient || useQueryClient();
  const mutation = useMovePage(options, qc);

  return {
    ...mutation,
    mutate: ({ pageId, data }: { pageId: string; data: MovePageRequest }, mutateOptions?: any) =>
      mutation.mutate({ workspaceId, pageId, data }, mutateOptions),
    mutateAsync: ({ pageId, data }: { pageId: string; data: MovePageRequest }, mutateOptions?: any) =>
      mutation.mutateAsync({ workspaceId, pageId, data }, mutateOptions),
  };
}

// ─── Invalidation helper ──────────────────────────────────────────────────────

export function useDirectoryQueryInvalidator(workspaceId: string) {
  const qc = useQueryClient();
  return () =>
    qc.invalidateQueries({ queryKey: getGetDirectoriesQueryKey(workspaceId) });
}

export type { DirectoryResponse, DirectoryRequest, MovePageRequest } from "@/modules/pages/api";
