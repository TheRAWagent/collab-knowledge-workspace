import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Eye, EyeOff, Mail, Lock } from "lucide-react";
import { useForm } from "react-hook-form";
import z from "zod";
import { createUserBody, createUserBodyPasswordRegExp } from "../schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useRouter } from "@tanstack/react-router";
import { useCreateUser } from "../api";
import { toast } from "sonner";

interface PasswordRequirement {
  label: string;
  test: (password: string) => boolean;
}

const passwordRequirements: PasswordRequirement[] = [
  { label: "At least 8 characters", test: (pwd) => pwd.length >= 8 },
  { label: "One uppercase letter", test: (pwd) => /[A-Z]/.test(pwd) },
  { label: "One lowercase letter", test: (pwd) => /[a-z]/.test(pwd) },
  { label: "One number", test: (pwd) => /\d/.test(pwd) },
  { label: "One special character", test: (pwd) => createUserBodyPasswordRegExp.test(pwd) },
];

export default function SignUpCard() {
  const router = useRouter();
  const signUpSchema = createUserBody
    .extend({
      confirmPassword: z.string().min(1, "Confirm Password is required"),
    })
    .refine((data) => data.password === data.confirmPassword, {
      message: "Passwords do not match",
      path: ["confirmPassword"],
    });
  const form = useForm<z.infer<typeof signUpSchema>>({
    resolver: zodResolver(signUpSchema),
    defaultValues: {
      email: "",
      password: "",
      confirmPassword: "",
    },
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const createUserMutation = useCreateUser({
    mutation: {
      onSuccess: () => toast.success("Account created successfully! You can now sign in."),
      onError: (error) => toast.error("Failed to create account. " + (error))
    }
  });

  const getPasswordStrength = (password: string) => {
    const passedRequirements = passwordRequirements.filter((req) =>
      req.test(password)
    ).length;
    if (passedRequirements === 0) return { strength: 0, label: "", color: "" };
    if (passedRequirements <= 2)
      return { strength: 25, label: "Weak", color: "bg-red-500" };
    if (passedRequirements <= 3)
      return { strength: 50, label: "Fair", color: "bg-yellow-500" };
    if (passedRequirements <= 4)
      return { strength: 75, label: "Good", color: "bg-blue-500" };
    return { strength: 100, label: "Strong", color: "bg-green-500" };
  };

  const handleSubmit = async (values: z.infer<typeof signUpSchema>) => {
    createUserMutation.mutate({ data: values });
  };

  const { password } = form.watch();

  const passwordStrength = getPasswordStrength(password);

  return (
    <div
      className={`w-[450px] max-w-[450px] transition-all duration-700 ease-out h-[680px]`}
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
                {/* <FormField name={"email"} control={form.control} render={({field}) => (
                <FormItem className="space-y-2">
                  <FormLabel htmlFor="name" className="text-white/90">
                    Full Name
                  </FormLabel>
                  <div className="relative">
                    <User className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <FormControl>

                    <Input
                        {...field}
                      className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Enter your full name"
                      />
                      </FormControl>
                  </div>
                </FormItem>
                )} /> */}
                {/* Name to be moved to onboarding */}
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
                <FormField
                  name={"password"}
                  control={form.control}
                  render={({ field }) => (
                    <FormItem className="space-y-2">
                      <FormLabel
                        htmlFor="signup-password"
                        className="text-white/90"
                      >
                        Password
                      </FormLabel>
                      <div className="relative">
                        <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                        <FormControl>
                          <Input
                            {...field}
                            type={showPassword ? "text" : "password"}
                            className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                            placeholder="Create a password"
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

                      {password.length > 0 && (
                        <div className="space-y-2">
                          <div className="flex items-center justify-between">
                            <span className="text-xs text-white/60">
                              Password strength
                            </span>
                            <span
                              className={`text-xs font-medium ${
                                passwordStrength.strength === 100
                                  ? "text-white/90"
                                  : passwordStrength.strength >= 75
                                  ? "text-white/80"
                                  : passwordStrength.strength >= 50
                                  ? "text-white/70"
                                  : "text-white/50"
                              }`}
                            >
                              {passwordStrength.label}
                            </span>
                          </div>
                          <div className="w-full bg-white/10 rounded-full h-1.5">
                            <div
                              className={`h-1.5 rounded-full transition-all duration-300 ${passwordStrength.color}`}
                              style={{ width: `${passwordStrength.strength}%` }}
                            />
                          </div>
                          <div className="space-y-1">
                            {passwordRequirements.map((req, index) => (
                              <div
                                key={index}
                                className="flex items-center space-x-2"
                              >
                                <div
                                  className={`w-1.5 h-1.5 rounded-full ${
                                    req.test(password)
                                      ? "bg-white/80"
                                      : "bg-white/20"
                                  }`}
                                />
                                <span
                                  className={`text-xs ${
                                    req.test(password)
                                      ? "text-white/80"
                                      : "text-white/40"
                                  }`}
                                >
                                  {req.label}
                                </span>
                              </div>
                            ))}
                          </div>
                        </div>
                      )}
                    </FormItem>
                  )}
                />
                <FormField
                  name={"confirmPassword"}
                  control={form.control}
                  render={({ field }) => (
                    <FormItem className="space-y-2">
                      <FormLabel
                        htmlFor="confirm-password"
                        className="text-white/90"
                      >
                        Confirm Password
                      </FormLabel>
                      <div className="relative">
                        <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                        <FormControl>
                          <Input
                            {...field}
                            type={showConfirmPassword ? "text" : "password"}
                            className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                            placeholder="Confirm your password"
                          />
                        </FormControl>
                        <button
                          type="button"
                          onClick={() =>
                            setShowConfirmPassword(!showConfirmPassword)
                          }
                          className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/50 hover:text-white/70"
                        >
                          {showConfirmPassword ? (
                            <EyeOff className="w-4 h-4" />
                          ) : (
                            <Eye className="w-4 h-4" />
                          )}
                        </button>
                      </div>
                      <FormMessage />
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
