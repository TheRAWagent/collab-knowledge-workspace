import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { createPageBody } from "../schemas";
import { useCreatePage, useUpdatePage, type PageResponse, getGetPagesQueryOptions } from "../api";
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

export interface PageFormProps {
  workspaceId: string;
  onSuccess?: () => void;
  onCancel?: () => void;
  initialValues?: PageResponse;
  className?: string;
}

export function PageForm({
  workspaceId,
  onSuccess,
  onCancel,
  initialValues,
  className,
}: PageFormProps) {
  const queryClient = useQueryClient();
  const isEdit = !!initialValues;

  const { mutate: createPage, isPending: isCreatePending } = useCreatePage({
    mutation: {
      onSuccess: () => {
        onSuccess?.();
      },
      onSettled: () => {
        queryClient.invalidateQueries({ queryKey: getGetPagesQueryOptions(workspaceId).queryKey });
      }
    },
  });

  const { mutate: updatePage, isPending: isUpdatePending } = useUpdatePage({
    mutation: {
      onSuccess: () => {
        onSuccess?.();
      },
      onSettled: () => {
        queryClient.invalidateQueries({ queryKey: getGetPagesQueryOptions(workspaceId).queryKey });
        // Also invalidate the specific document query if we were viewing it, but for now just list is enough
      }
    },
  });

  const isLoading = isCreatePending || isUpdatePending;

  const form = useForm<z.infer<typeof createPageBody>>({
    resolver: zodResolver(createPageBody),
    defaultValues: {
      title: "",
      icon: "",
    },
  });

  useEffect(() => {
    if (initialValues) {
      form.reset({
        title: initialValues.title || "",
        icon: initialValues.icon || "",
      });
    }
  }, [initialValues, form]);

  function onSubmit(values: z.infer<typeof createPageBody>) {
    if (isEdit && initialValues?.id) {
      updatePage({ workspaceId, pageId: initialValues.id, data: values });
    } else {
      createPage({ workspaceId, data: values });
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className={cn("space-y-6", className)}>
        <FormField
          control={form.control}
          name="title"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Title</FormLabel>
              <FormControl>
                <Input placeholder="Page Title" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        {/* Icon field could be added here, but keeping it simple for now as per request */}

        <div className="flex items-center gap-2">
          <Button type="submit" disabled={isLoading} className="flex-1">
            {isLoading && (
              <div className="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-current border-t-transparent" />
            )}
            {isEdit ? "Update Page" : "Create Page"}
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
