import { useState } from "react";
import { Input } from "@/components/ui/input";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Mail, ArrowLeft, Lock, Eye, EyeOff } from "lucide-react";
import * as z from "zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useRouter } from "@tanstack/react-router";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { initiatePasswordReset, resetPassword } from "@/modules/auth/api";
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/components/ui/input-otp";

// ─── Step 1: collect email ───────────────────────────────────────────────────
const emailSchema = z.object({
  email: z.string().email("Please enter a valid email address"),
});

// ─── Step 2: collect OTP + new password ──────────────────────────────────────
const resetSchema = z.object({
  code: z.string().min(1, "Verification code is required"),
  newPassword: z
    .string()
    .min(8, "Password must be at least 8 characters")
    .regex(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/,
      "Must include uppercase, lowercase, number & special character"
    ),
});

type EmailValues = z.infer<typeof emailSchema>;
type ResetValues = z.infer<typeof resetSchema>;

export default function ForgotPasswordCard() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [step, setStep] = useState<"email" | "reset">("email");
  const [submittedEmail, setSubmittedEmail] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  // ── Step-1 form ─────────────────────────────────────────────────────────────
  const emailForm = useForm<EmailValues>({
    resolver: zodResolver(emailSchema),
    defaultValues: { email: "" },
  });

  // ── Step-2 form ─────────────────────────────────────────────────────────────
  const resetForm = useForm<ResetValues>({
    resolver: zodResolver(resetSchema),
    defaultValues: { code: "", newPassword: "" },
  });

  // ── Handlers ─────────────────────────────────────────────────────────────────
  const handleEmailSubmit = async (values: EmailValues) => {
    setIsLoading(true);
    try {
      await initiatePasswordReset({ email: values.email });
    } catch {
      // intentionally swallow — always show the safe message below
    } finally {
      setIsLoading(false);
    }
    setSubmittedEmail(values.email);
    setStep("reset");
    toast.success(
      "If an account exists, a verification code has been sent to the registered email."
    );
  };

  const handleResetSubmit = async (values: ResetValues) => {
    setIsLoading(true);
    try {
      await resetPassword({
        email: submittedEmail,
        code: values.code,
        newPassword: values.newPassword,
      });
      toast.success("Password reset successful! You can now sign in.");
      router.navigate({ to: "/sign-in" });
    } catch {
      toast.error("Invalid or expired verification code. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  // ── Render ───────────────────────────────────────────────────────────────────
  return (
    <div className="w-[450px] max-w-[450px] transition-all duration-700 ease-out">
      <div className="relative">
        {/* Glassmorphism card */}
        <div className="absolute inset-0 bg-white/10 backdrop-blur-xl rounded-3xl border border-white/20 shadow-2xl" />
        <div className="absolute inset-0 bg-linear-to-br from-white/20 via-white/10 to-transparent rounded-3xl" />

        {/* Content */}
        <div className="relative p-8 flex flex-col gap-6">
          <div className="text-center space-y-1">
            <h1 className="text-2xl font-semibold text-white">
              {step === "email" ? "Forgot Password" : "Enter Verification Code"}
            </h1>
            <p className="text-white/70 text-sm">
              {step === "email"
                ? "Enter your email to receive a verification code"
                : `We sent a code to ${submittedEmail}`}
            </p>
          </div>

          {/* ── Step 1 ── */}
          {step === "email" && (
            <Form {...emailForm}>
              <form
                onSubmit={emailForm.handleSubmit(handleEmailSubmit)}
                className="space-y-5"
              >
                <FormField
                  control={emailForm.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-white/90">Email</FormLabel>
                      <div className="relative">
                        <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-white/50 w-4 h-4" />
                        <FormControl>
                          <Input
                            {...field}
                            id="forgot-password-email"
                            className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                            placeholder="Enter your email"
                            type="email"
                          />
                        </FormControl>
                      </div>
                      <FormMessage className="text-red-400" />
                    </FormItem>
                  )}
                />

                <Button
                  id="send-reset-code-btn"
                  type="submit"
                  disabled={isLoading}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
                >
                  {isLoading ? "Sending..." : "Send Verification Code"}
                </Button>
              </form>
            </Form>
          )}

          {/* ── Step 2 ── */}
          {step === "reset" && (
            <Form {...resetForm}>
              <form
                onSubmit={resetForm.handleSubmit(handleResetSubmit)}
                className="space-y-5"
              >
                <FormField
                  control={resetForm.control}
                  name="code"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-white/90">
                        Verification Code
                      </FormLabel>
                      <div className="relative">
                        <FormControl>
                          <InputOTP maxLength={6} {...field}>
                            <InputOTPGroup className="flex-1">
                              <InputOTPSlot index={0} className="flex-1 bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 h-11" />
                              <InputOTPSlot index={1} className="flex-1 bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 h-11" />
                              <InputOTPSlot index={2} className="flex-1 bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 h-11" />
                              <InputOTPSlot index={3} className="flex-1 bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 h-11" />
                              <InputOTPSlot index={4} className="flex-1 bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 h-11" />
                              <InputOTPSlot index={5} className="flex-1 bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 h-11" />
                            </InputOTPGroup>
                          </InputOTP>
                        </FormControl>
                      </div>
                      <FormMessage className="text-red-400" />
                    </FormItem>
                  )}
                />

                <FormField
                  control={resetForm.control}
                  name="newPassword"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-white/90">
                        New Password
                      </FormLabel>
                      <div className="relative">
                        <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-white/50 w-4 h-4" />
                        <FormControl>
                          <Input
                            {...field}
                            id="new-password-input"
                            type={showPassword ? "text" : "password"}
                            className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                            placeholder="New password"
                          />
                        </FormControl>
                        <button
                          type="button"
                          onClick={() => setShowPassword(!showPassword)}
                          className="absolute right-3 top-1/2 -translate-y-1/2 text-white/50 hover:text-white/70"
                        >
                          {showPassword ? (
                            <EyeOff className="w-4 h-4" />
                          ) : (
                            <Eye className="w-4 h-4" />
                          )}
                        </button>
                      </div>
                      <FormMessage className="text-red-400" />
                    </FormItem>
                  )}
                />

                <div className="flex gap-3">
                  <Button
                    id="back-to-email-btn"
                    type="button"
                    variant="ghost"
                    onClick={() => setStep("email")}
                    className="flex-1 text-white/70 hover:text-white hover:bg-white/10 border border-white/20 h-11 rounded-xl"
                  >
                    Back
                  </Button>
                  <Button
                    id="submit-reset-btn"
                    type="submit"
                    disabled={isLoading}
                    className="flex-1 bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
                  >
                    {isLoading ? "Resetting..." : "Reset Password"}
                  </Button>
                </div>
              </form>
            </Form>
          )}

          {/* Back to sign-in */}
          <div className="text-center">
            <button
              id="back-to-signin-btn"
              onClick={() => router.navigate({ to: "/sign-in" })}
              className="text-white/70 hover:text-white text-sm transition-colors flex items-center justify-center gap-2 w-full"
            >
              <ArrowLeft className="w-4 h-4" />
              Back to Sign In
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
