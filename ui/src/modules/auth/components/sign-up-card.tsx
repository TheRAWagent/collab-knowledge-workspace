import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Mail } from "lucide-react";
import { useForm } from "react-hook-form";
import z from "zod";
import { CreateUserBody } from "../schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from "@/components/ui/form";
import { useRouter } from "@tanstack/react-router";
import { toast } from "sonner";
import { useMutation } from "@tanstack/react-query";
import { createUser } from "@/modules/users/api";

export default function SignUpCard() {
  const router = useRouter();
  const signUpSchema = CreateUserBody.omit({ password: true });

  const form = useForm<z.infer<typeof signUpSchema>>({
    resolver: zodResolver(signUpSchema),
    defaultValues: {
      email: "",
    },
  });

  const createUserMutation = useMutation({
    mutationFn: async (data: z.infer<typeof signUpSchema>) => {
      await createUser({ email: data.email });
    },
    onSuccess: (_data, vars) => {
      toast.success("Account created successfully!")
      router.navigate({ to: "/verify-otp", search: { email: vars.email } });
    },
    onError: (error) => toast.error("Failed to create account. " + (error))
  });

  const handleSubmit = async (values: z.infer<typeof signUpSchema>) => {
    createUserMutation.mutate(values);
  };

  return (
    <div
      className={`w-[450px] max-w-[450px] transition-all duration-700 ease-out h-[380px]`}
    >
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
                Create Account
              </h1>
              <p className="text-white/70">Join us today</p>
            </div>
            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(handleSubmit)}
                className="space-y-4"
              >
                <FormField
                  name={"email"}
                  control={form.control}
                  render={({ field }) => (
                    <FormItem className="space-y-2">
                      <FormLabel
                        htmlFor="signup-email"
                        className="text-white/90"
                      >
                        Email
                      </FormLabel>
                      <div className="relative">
                        <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                        <FormControl>
                          <Input
                            {...field}
                            className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                            placeholder="Enter your email"
                          />
                        </FormControl>
                      </div>
                    </FormItem>
                  )}
                />

                <Button
                  type="submit"
                  disabled={createUserMutation.isPending}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm disabled:opacity-50"
                >
                  {createUserMutation.isPending
                    ? "Creating account..."
                    : "Sign Up"}
                </Button>
              </form>
            </Form>

            <div className="text-center">
              <button
                onClick={() => router.navigate({ to: "/sign-in" })}
                className="text-white/70 hover:text-white text-sm transition-colors"
              >
                Already have an account? Sign in
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
