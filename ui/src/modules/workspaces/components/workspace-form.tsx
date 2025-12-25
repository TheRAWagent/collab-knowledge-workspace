import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { createBody } from "../schemas";
import { getGetByIdQueryOptions, getListQueryOptions, useCreate, useUpdate, type WorkspaceResponse } from "../api";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { useEffect } from "react";
import { useQueryClient } from "@tanstack/react-query";

export interface WorkspaceFormProps {
  onSuccess?: () => void;
  onCancel?: () => void;
  initialValues?: WorkspaceResponse;
  className?: string;
}

export function WorkspaceForm({
  onSuccess,
  onCancel,
  initialValues,
  className,
}: WorkspaceFormProps) {
  const queryClient = useQueryClient();


  const isEdit = !!initialValues;

  const { mutate: createWorkspace, isPending: isCreatePending } = useCreate({
    mutation: {
      onSuccess: () => {
        onSuccess?.();
      },
      onSettled: () => queryClient.invalidateQueries({queryKey: getListQueryOptions().queryKey})
    },
  });

  const { mutate: updateWorkspace, isPending: isUpdatePending } = useUpdate({
    mutation: {
      onSuccess: () => {
        onSuccess?.();
      },
      onSettled: () => {
        queryClient.invalidateQueries({queryKey: getListQueryOptions().queryKey})
        queryClient.invalidateQueries({queryKey: getGetByIdQueryOptions(initialValues!.id).queryKey});
      }
    },
  });

  const isLoading = isCreatePending || isUpdatePending;

  const form = useForm<z.infer<typeof createBody>>({
    resolver: zodResolver(createBody),
    defaultValues: {
      name: "",
      description: "",
    },
  });

  useEffect(() => {
    if (initialValues) {
      form.reset({
        name: initialValues.name,
        description: initialValues.description,
      });
    }
  }, [initialValues, form]);

  function onSubmit(values: z.infer<typeof createBody>) {
    if (isEdit && initialValues) {
      updateWorkspace({ id: initialValues.id, data: values });
    } else {
      createWorkspace({ data: values });
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className={cn("space-y-6", className)}>
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Name</FormLabel>
              <FormControl>
                <Input placeholder="Workspace Name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="description"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Description</FormLabel>
              <FormControl>
                <textarea
                  className={cn(
                    "file:text-foreground placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground dark:bg-input/30 border-input w-full min-w-0 rounded-md border bg-transparent px-3 py-2 text-base shadow-xs transition-[color,box-shadow] outline-none disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm",
                    "focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px]",
                    "aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive",
                    "min-h-[100px]"
                  )}
                  placeholder="Workspace Description"
                  {...field}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <div className="flex items-center gap-2">
          <Button type="submit" disabled={isLoading} className="flex-1">
            {isLoading && (
              <div className="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-current border-t-transparent" />
            )}
            {isEdit ? "Update Workspace" : "Create Workspace"}
          </Button>
          {onCancel && (
            <Button type="button" variant="outline" onClick={onCancel} className="flex-1">
              Cancel
            </Button>
          )}
        </div>
      </form>
    </Form>
  );
}
