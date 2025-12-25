import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
// import { Input } from "@/components/ui/input"
import { ArrowLeft } from "lucide-react"
import { useRouter } from "@tanstack/react-router"
// import { useForm } from "react-hook-form"
// import { verifyUserBody } from "../schemas"
// import { zodResolver } from "@hookform/resolvers/zod"
// import z from "zod"
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/components/ui/input-otp"

export default function VerifyOtpCard() {
  const router = useRouter();

  // const form = useForm<z.infer<typeof verifyUserBody>>({
  //   resolver: zodResolver(verifyUserBody),
  //   defaultValues: {

  //   }
  // });
  const [isLoading, setIsLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))

    setIsLoading(false)
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
              {/* <p className="text-white font-medium">{formData.email}</p> */}
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">
              <div className="flex justify-center space-x-3">
                <InputOTP maxLength={6}>
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
                {/* {formData.otp.map((digit, index) => (
                    <Input
                      key={index}
                      id={`otp-${index}`}
                      type="text"
                      value={digit}
                      onChange={(e) => handleOtpChange(index, e.target.value)}
                      className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl"
                      maxLength={1}
                    />
                  ))} */}
              </div>

              <Button
                type="submit"
                // disabled={isLoading || formData.otp.some((digit) => !digit)}
                className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
              >
                {isLoading ? "Verifying..." : "Verify Code"}
              </Button>
            </form>

            <div className="text-center">
              <button className="text-white/70 hover:text-white text-sm transition-colors">Resend code</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
