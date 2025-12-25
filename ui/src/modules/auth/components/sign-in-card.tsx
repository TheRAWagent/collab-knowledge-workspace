import { useState } from "react";
import { Input } from "@/components/ui/input";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Eye, EyeOff, Mail, Lock } from "lucide-react";
import { loginBody } from "../schemas";
import z from "zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from "@/components/ui/form";
import { useRouter } from "@tanstack/react-router";
import { useLogin } from "../api";

export default function AuthenticationCard() {
  const router = useRouter();

  const form = useForm<z.infer<typeof loginBody>>({
    resolver: zodResolver(loginBody),
    defaultValues: {
      email: "",
      password: "",
    },
  });
  const [showPassword, setShowPassword] = useState(false);

  const createUserMutation = useLogin({
    mutation: {
      onSuccess: () => {
        router.navigate({ to: "/" });
      },
    }
  });

  const handleSubmit = (values: z.infer<typeof loginBody>) => {
    createUserMutation.mutate({ data: values });
  };

  return (
    <div
      className={`w-[450px] max-w-[450px] transition-all duration-700 ease-out h-[480px]`}
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
                Welcome Back
              </h1>
              <p className="text-white/70">Sign in to your account</p>
            </div>
            <Form {...form}>
              <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4">
                <FormField
                  control={form.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem className="space-y-2">
                      <FormLabel htmlFor="email" className="text-white/90">
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
                <FormField
                  control={form.control}
                  name="password"
                  render={({ field }) => (
                    <FormItem className="space-y-2">
                      <FormLabel htmlFor="password" className="text-white/90">
                        Password
                      </FormLabel>
                      <div className="relative">
                        <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                        <FormControl>
                          <Input
                            {...field}
                            type={showPassword ? "text" : "password"}
                            className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                            placeholder="Enter your password"
                          />
                        </FormControl>
                        <button
                          type="button"
                          onClick={() => setShowPassword(!showPassword)}
                          className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/50 hover:text-white/70"
                        >
                          {showPassword ? (
                            <EyeOff className="w-4 h-4" />
                          ) : (
                            <Eye className="w-4 h-4" />
                          )}
                        </button>
                      </div>
                    </FormItem>
                  )}
                />

                <div className="text-right">
                  <button
                    type="button"
                    onClick={() => router.navigate({ to: "/forgot-password" })}
                    className="text-white/70 hover:text-white text-sm transition-colors"
                  >
                    Forgot password?
                  </button>
                </div>

                <button
                  type="submit"
                  disabled={createUserMutation.isPending}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
                >
                  {createUserMutation.isPending ? "Signing in..." : "Sign In"}
                </button>
              </form>
            </Form>
            <div className="text-center">
              <button
                onClick={() => router.navigate({ to: "/sign-up" })}
                className="text-white/70 hover:text-white text-sm transition-colors"
              >
                {"Don't have an account? Sign up"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
