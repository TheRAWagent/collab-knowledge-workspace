import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { createUserBody } from "../schemas";
import { useCreateUser } from "../api";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { toast } from "sonner";
import { User } from "lucide-react";
import { useRouter } from "@tanstack/react-router";

export default function OnboardingCard() {
  const router = useRouter();
  const { mutate: createUser, isPending } = useCreateUser({
    mutation: {
      onSuccess: () => {
        toast.success("Profile created successfully!");
        router.navigate({to: '/'});
      },
      onError: () => {
        toast.error("Failed to create profile. Please try again.");
      },
    },
  });

  const form = useForm<z.infer<typeof createUserBody>>({
    resolver: zodResolver(createUserBody),
    defaultValues: {
      name: "",
    },
  });

  function onSubmit(values: z.infer<typeof createUserBody>) {
    createUser({ data: values });
  }

  return (
    <div className="w-[450px] max-w-[450px] transition-all duration-700 ease-out h-[480px] lg:pl-60">
      <div className="relative h-full">
        {/* Glass morphism card */}
        <div className="absolute inset-0 bg-white/10 backdrop-blur-xl rounded-3xl border border-white/20 shadow-2xl">
          <div className="absolute inset-0 bg-linear-to-br from-white/20 via-white/10 to-transparent rounded-3xl" />
        </div>

        {/* Content */}
        <div className="relative h-full p-8 flex flex-col">
          <div className="flex-1 flex flex-col justify-center space-y-6">
            <div className="text-center space-y-2">
              <h1 className="text-2xl font-semibold text-white">
                Welcome Aboard
              </h1>
              <p className="text-white/70">Let's start by setting up your profile</p>
            </div>

            <Form {...form}>
              <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem className="space-y-2">
                      <FormLabel className="text-white/90">Full Name</FormLabel>
                      <div className="relative">
                        <User className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                        <FormControl>
                          <Input
                            placeholder="e.g. Jane Doe"
                            className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                            {...field}
                          />
                        </FormControl>
                      </div>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <button
                  type="submit"
                  disabled={isPending}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm flex items-center justify-center gap-2"
                >
                  {isPending ? (
                    <>
                      <div className="h-4 w-4 animate-spin rounded-full border-2 border-white/50 border-t-white" />
                      <span>Creating Profile...</span>
                    </>
                  ) : (
                    "Get Started"
                  )}
                </button>
              </form>
            </Form>
          </div>
        </div>
      </div>
    </div>
  );
}
