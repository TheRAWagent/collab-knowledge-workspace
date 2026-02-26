import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
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

const directorySchema = z.object({
  name: z.string().min(1, "Name is required").max(100, "Name is too long"),
});

export interface DirectoryFormProps {
  onSubmit: (name: string) => void;
  onCancel?: () => void;
  initialName?: string;
  isLoading?: boolean;
  className?: string;
  submitLabel?: string;
}

export function DirectoryForm({
  onSubmit,
  onCancel,
  initialName,
  isLoading,
  className,
  submitLabel = "Create",
}: DirectoryFormProps) {
  const form = useForm<z.infer<typeof directorySchema>>({
    resolver: zodResolver(directorySchema),
    defaultValues: { name: initialName ?? "" },
  });

  useEffect(() => {
    if (initialName !== undefined) {
      form.reset({ name: initialName });
    }
  }, [initialName, form]);

  function handleSubmit(values: z.infer<typeof directorySchema>) {
    onSubmit(values.name);
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(handleSubmit)}
        className={cn("space-y-4", className)}
      >
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Directory Name</FormLabel>
              <FormControl>
                <Input placeholder="e.g. Design Docs" autoFocus {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <div className="flex items-center gap-2 pt-2">
          <Button type="submit" disabled={isLoading} className="flex-1">
            {isLoading && (
              <div className="mr-2 h-4 w-4 animate-spin rounded-full border-2 border-current border-t-transparent" />
            )}
            {submitLabel}
          </Button>
          {onCancel && (
            <Button
              type="button"
              variant="outline"
              onClick={onCancel}
              className="flex-1"
            >
              Cancel
            </Button>
          )}
        </div>
      </form>
    </Form>
  );
}
