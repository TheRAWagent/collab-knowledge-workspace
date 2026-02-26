import { Button } from "@/components/ui/button"
import { ArrowLeft } from "lucide-react"
import { useRouter } from "@tanstack/react-router"
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/components/ui/input-otp"
import { useMutation } from "@tanstack/react-query"
import { verifyEmail, useResendVerification } from "@/modules/users/api"
import { toast } from "sonner"
import { useForm } from "react-hook-form"
import { VerifyEmailBody } from "@/modules/users/schemas"
import { zodResolver } from "@hookform/resolvers/zod"
import type z from "zod"
import { Form, FormField } from "@/components/ui/form"

export default function VerifyOtpCard({ email }: { email: string }) {
  const router = useRouter();

  const verifyUserMutation = useMutation({
    mutationFn: async (code: string) => {
      await verifyEmail({ code, email });
    },
    onSuccess: () => {
      toast.success("Email verified successfully");
      router.navigate({ to: "/set-password", search: { email } });
    },
    onError: () => {
      toast.error("Email verification failed");
    }
  })

  const resendCodeMutation = useResendVerification({
    mutation: {
      onSuccess: () => {
        toast.success("Verification code sent");
      },
      onError: () => {
        toast.error("Failed to resend verification code");
      }
    }
  });

  const form = useForm<z.infer<typeof VerifyEmailBody>>({
    resolver: zodResolver(VerifyEmailBody),
    defaultValues: {
      email,
      code: ""
    },
  })

  const handleSubmit = async (data: z.infer<typeof VerifyEmailBody>) => {
    verifyUserMutation.mutate(data.code)
  }

  return (
    <div className={`w-[450px] max-w-[450px] transition-all duration-700 ease-out h-[380px]`}>
      <div className="relative h-full">
        {/* Glass morphism card */}
        <div className="absolute inset-0 bg-white/10 backdrop-blur-xl rounded-3xl border border-white/20 shadow-2xl">
          <div className="absolute inset-0 bg-linear-to-br from-white/20 via-white/10 to-transparent rounded-3xl" />
        </div>

        {/* Content */}
        <div className="relative h-full p-8 flex flex-col">
          <div className="flex-1 flex flex-col justify-center space-y-6">
            <button
              onClick={() => router.navigate({ to: "/sign-in" })}
              className="absolute top-6 left-6 text-white/70 hover:text-white transition-colors"
            >
              <ArrowLeft className="w-5 h-5" />
            </button>

            <div className="text-center space-y-2">
              <h1 className="text-2xl font-semibold text-white">Verify Your Email</h1>
              <p className="text-white/70">Enter the 6-digit code sent to</p>
              <p className="text-white font-medium">{email}</p>
            </div>

            <Form {...form}>

              <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-6">
                <div className="flex justify-center space-x-3">
                  <FormField
                    name="code"
                    control={form.control}
                    render={({ field }) => (
                      <InputOTP maxLength={6} minLength={6} {...field}>
                        <InputOTPGroup>
                          <InputOTPSlot className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl" index={0} />
                        </InputOTPGroup>
                        <InputOTPGroup>
                          <InputOTPSlot className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl" index={1} />
                        </InputOTPGroup>
                        <InputOTPGroup>
                          <InputOTPSlot className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl" index={2} />
                        </InputOTPGroup>
                        <InputOTPGroup>
                          <InputOTPSlot className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl" index={3} />
                        </InputOTPGroup>
                        <InputOTPGroup>
                          <InputOTPSlot className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl" index={4} />
                        </InputOTPGroup>
                        <InputOTPGroup>
                          <InputOTPSlot className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl" index={5} />
                        </InputOTPGroup>
                      </InputOTP>
                    )}
                  />
                </div>

                <Button
                  type="submit"
                  disabled={verifyUserMutation.isPending}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
                >
                  {verifyUserMutation.isPending ? "Verifying..." : "Verify Code"}
                </Button>
              </form>
            </Form>

            <div className="text-center">
              <button
                onClick={() => resendCodeMutation.mutate({ data: { email, code: "000000" } })}
                disabled={resendCodeMutation.isPending}
                className="text-white/70 hover:text-white text-sm transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {resendCodeMutation.isPending ? "Sending..." : "Resend code"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

