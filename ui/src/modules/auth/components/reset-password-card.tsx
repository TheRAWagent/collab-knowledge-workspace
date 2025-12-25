import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Eye, EyeOff, Mail, Lock, User, ArrowLeft, Check, Shield, X } from "lucide-react"

type AuthStep = "login" | "signup" | "forgot-password" | "reset-password" | "otp" | "success"
type AuthMode = "login" | "signup"

interface PasswordRequirement {
  label: string
  test: (password: string) => boolean
}

const passwordRequirements: PasswordRequirement[] = [
  { label: "At least 8 characters", test: (pwd) => pwd.length >= 8 },
  { label: "One uppercase letter", test: (pwd) => /[A-Z]/.test(pwd) },
  { label: "One lowercase letter", test: (pwd) => /[a-z]/.test(pwd) },
  { label: "One number", test: (pwd) => /\d/.test(pwd) },
  { label: "One special character", test: (pwd) => /[!@#$%^&*(),.?":{}|<>]/.test(pwd) },
]

export default function AuthenticationCard() {
  const [step, setStep] = useState<AuthStep>("login")
  const [mode, setMode] = useState<AuthMode>("login")
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
    otp: ["", "", "", "", "", ""],
  })

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
  }

  const handleOtpChange = (index: number, value: string) => {
    if (value.length <= 1 && /^\d*$/.test(value)) {
      const newOtp = [...formData.otp]
      newOtp[index] = value
      setFormData((prev) => ({ ...prev, otp: newOtp }))

      // Auto-focus next input
      if (value && index < 5) {
        const nextInput = document.getElementById(`otp-${index + 1}`)
        nextInput?.focus()
      }
    }
  }

  const getPasswordStrength = (password: string) => {
    const passedRequirements = passwordRequirements.filter((req) => req.test(password)).length
    if (passedRequirements === 0) return { strength: 0, label: "", color: "" }
    if (passedRequirements <= 2) return { strength: 25, label: "Weak", color: "bg-red-500" }
    if (passedRequirements <= 3) return { strength: 50, label: "Fair", color: "bg-yellow-500" }
    if (passedRequirements <= 4) return { strength: 75, label: "Good", color: "bg-blue-500" }
    return { strength: 100, label: "Strong", color: "bg-green-500" }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1500))

    if (step === "login" || step === "signup") {
      setStep("otp")
    } else if (step === "forgot-password") {
      setStep("reset-password")
    } else if (step === "reset-password") {
      setStep("success")
    } else if (step === "otp") {
      setStep("success")
    }

    setIsLoading(false)
  }

  const switchMode = (newMode: AuthMode) => {
    setMode(newMode)
    setStep(newMode)
    setFormData({ email: "", password: "", confirmPassword: "", name: "", otp: ["", "", "", "", "", ""] })
  }

  const resetToLogin = () => {
    setStep("login")
    setMode("login")
    setFormData({ email: "", password: "", confirmPassword: "", name: "", otp: ["", "", "", "", "", ""] })
  }

  const goToForgotPassword = () => {
    setStep("forgot-password")
    setFormData((prev) => ({ ...prev, password: "", confirmPassword: "", name: "", otp: ["", "", "", "", "", ""] }))
  }

  const getCardHeight = () => {
    switch (step) {
      case "login":
        return "h-[480px]"
      case "signup":
        return "h-[680px]"
      case "forgot-password":
        return "h-[380px]"
      case "reset-password":
        return "h-[520px]"
      case "otp":
        return "h-[380px]"
      case "success":
        return "h-[320px]"
      default:
        return "h-[480px]"
    }
  }

  const passwordStrength = getPasswordStrength(formData.password)
  const isSignupValid =
    step === "signup" &&
    formData.name &&
    formData.email &&
    formData.password &&
    formData.confirmPassword &&
    formData.password === formData.confirmPassword &&
    passwordRequirements.every((req) => req.test(formData.password))

  return (
    <div className={`w-[450px] max-w-[450px] transition-all duration-700 ease-out ${getCardHeight()}`}>
      <div className="relative h-full">
        {/* Glass morphism card */}
        <div className="absolute inset-0 bg-white/10 backdrop-blur-xl rounded-3xl border border-white/20 shadow-2xl">
          <div className="absolute inset-0 bg-linear-to-br from-white/20 via-white/10 to-transparent rounded-3xl" />
        </div>

        {/* Content */}
        <div className="relative h-full p-8 flex flex-col">
          {step === "login" && (
            <div className="flex-1 flex flex-col justify-center space-y-6">
              <div className="text-center space-y-2">
                <h1 className="text-2xl font-semibold text-white">Welcome Back</h1>
                <p className="text-white/70">Sign in to your account</p>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="email" className="text-white/90">
                    Email
                  </Label>
                  <div className="relative">
                    <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="email"
                      type="email"
                      value={formData.email}
                      onChange={(e) => handleInputChange("email", e.target.value)}
                      className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Enter your email"
                      required
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="password" className="text-white/90">
                    Password
                  </Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="password"
                      type={showPassword ? "text" : "password"}
                      value={formData.password}
                      onChange={(e) => handleInputChange("password", e.target.value)}
                      className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Enter your password"
                      required
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/50 hover:text-white/70"
                    >
                      {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                    </button>
                  </div>
                </div>

                <div className="text-right">
                  <button
                    type="button"
                    onClick={goToForgotPassword}
                    className="text-white/70 hover:text-white text-sm transition-colors"
                  >
                    Forgot password?
                  </button>
                </div>

                <Button
                  type="submit"
                  disabled={isLoading}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
                >
                  {isLoading ? "Signing in..." : "Sign In"}
                </Button>
              </form>

              <div className="text-center">
                <button
                  onClick={() => switchMode("signup")}
                  className="text-white/70 hover:text-white text-sm transition-colors"
                >
                  {"Don't have an account? Sign up"}
                </button>
              </div>
            </div>
          )}

          {step === "signup" && (
            <div className="flex-1 flex flex-col justify-center space-y-6">
              <div className="text-center space-y-2">
                <h1 className="text-2xl font-semibold text-white">Create Account</h1>
                <p className="text-white/70">Join us today</p>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="name" className="text-white/90">
                    Full Name
                  </Label>
                  <div className="relative">
                    <User className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="name"
                      type="text"
                      value={formData.name}
                      onChange={(e) => handleInputChange("name", e.target.value)}
                      className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Enter your full name"
                      required
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="signup-email" className="text-white/90">
                    Email
                  </Label>
                  <div className="relative">
                    <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="signup-email"
                      type="email"
                      value={formData.email}
                      onChange={(e) => handleInputChange("email", e.target.value)}
                      className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Enter your email"
                      required
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="signup-password" className="text-white/90">
                    Password
                  </Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="signup-password"
                      type={showPassword ? "text" : "password"}
                      value={formData.password}
                      onChange={(e) => handleInputChange("password", e.target.value)}
                      className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Create a password"
                      required
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/50 hover:text-white/70"
                    >
                      {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                    </button>
                  </div>

                  {formData.password && (
                    <div className="space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="text-xs text-white/60">Password strength</span>
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
                          <div key={index} className="flex items-center space-x-2">
                            <div
                              className={`w-1.5 h-1.5 rounded-full ${
                                req.test(formData.password) ? "bg-white/80" : "bg-white/20"
                              }`}
                            />
                            <span
                              className={`text-xs ${req.test(formData.password) ? "text-white/80" : "text-white/40"}`}
                            >
                              {req.label}
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>

                <div className="space-y-2">
                  <Label htmlFor="confirm-password" className="text-white/90">
                    Confirm Password
                  </Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="confirm-password"
                      type={showConfirmPassword ? "text" : "password"}
                      value={formData.confirmPassword}
                      onChange={(e) => handleInputChange("confirmPassword", e.target.value)}
                      className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Confirm your password"
                      required
                    />
                    <button
                      type="button"
                      onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                      className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/50 hover:text-white/70"
                    >
                      {showConfirmPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                    </button>
                  </div>
                  {formData.confirmPassword && formData.password !== formData.confirmPassword && (
                    <p className="text-xs text-red-400">Passwords do not match</p>
                  )}
                </div>

                <Button
                  type="submit"
                  disabled={isLoading || !isSignupValid}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm disabled:opacity-50"
                >
                  {isLoading ? "Creating account..." : "Sign Up"}
                </Button>
              </form>

              <div className="text-center">
                <button
                  onClick={() => switchMode("login")}
                  className="text-white/70 hover:text-white text-sm transition-colors"
                >
                  Already have an account? Sign in
                </button>
              </div>
            </div>
          )}

          {step === "forgot-password" && (
            <div className="flex-1 flex flex-col justify-center space-y-6">
              <button
                onClick={resetToLogin}
                className="absolute top-6 left-6 text-white/70 hover:text-white transition-colors"
              >
                <ArrowLeft className="w-5 h-5" />
              </button>

              <div className="text-center space-y-2">
                <h1 className="text-2xl font-semibold text-white">Reset Password</h1>
                <p className="text-white/70">Enter your email to receive reset instructions</p>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="reset-email" className="text-white/90">
                    Email
                  </Label>
                  <div className="relative">
                    <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="reset-email"
                      type="email"
                      value={formData.email}
                      onChange={(e) => handleInputChange("email", e.target.value)}
                      className="pl-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Enter your email"
                      required
                    />
                  </div>
                </div>

                <Button
                  type="submit"
                  disabled={isLoading}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
                >
                  {isLoading ? "Sending..." : "Send Reset Link"}
                </Button>
              </form>
            </div>
          )}

          {step === "reset-password" && (
            <div className="flex-1 flex flex-col justify-center space-y-6">
              <button
                onClick={() => setStep("forgot-password")}
                className="absolute top-6 left-6 text-white/70 hover:text-white transition-colors"
              >
                <ArrowLeft className="w-5 h-5" />
              </button>

              <div className="text-center space-y-2">
                <div className="w-12 h-12 bg-white/20 backdrop-blur-sm border border-white/30 rounded-full flex items-center justify-center mx-auto">
                  <Shield className="w-6 h-6 text-white" />
                </div>
                <h1 className="text-2xl font-semibold text-white">Create New Password</h1>
                <p className="text-white/70">Enter your new password below</p>
              </div>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="new-password" className="text-white/90">
                    New Password
                  </Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="new-password"
                      type={showPassword ? "text" : "password"}
                      value={formData.password}
                      onChange={(e) => handleInputChange("password", e.target.value)}
                      className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Enter new password"
                      required
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/50 hover:text-white/70"
                    >
                      {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                    </button>
                  </div>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="confirm-new-password" className="text-white/90">
                    Confirm New Password
                  </Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-white/50 w-4 h-4" />
                    <Input
                      id="confirm-new-password"
                      type={showConfirmPassword ? "text" : "password"}
                      value={formData.confirmPassword}
                      onChange={(e) => handleInputChange("confirmPassword", e.target.value)}
                      className="pl-10 pr-10 bg-white/10 border-white/20 text-white placeholder:text-white/50 focus:border-white/40 focus:ring-white/20"
                      placeholder="Confirm new password"
                      required
                    />
                    <button
                      type="button"
                      onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                      className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/50 hover:text-white/70"
                    >
                      {showConfirmPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                    </button>
                  </div>
                  {formData.confirmPassword && formData.password !== formData.confirmPassword && (
                    <p className="text-xs text-red-400">Passwords do not match</p>
                  )}
                </div>

                <Button
                  type="submit"
                  disabled={isLoading || !formData.password || formData.password !== formData.confirmPassword}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm disabled:opacity-50"
                >
                  {isLoading ? "Updating..." : "Update Password"}
                </Button>
              </form>
            </div>
          )}

          {step === "otp" && (
            <div className="flex-1 flex flex-col justify-center space-y-6">
              <button
                onClick={() => setStep(mode)}
                className="absolute top-6 left-6 text-white/70 hover:text-white transition-colors"
              >
                <ArrowLeft className="w-5 h-5" />
              </button>

              <div className="text-center space-y-2">
                <h1 className="text-2xl font-semibold text-white">Verify Your Email</h1>
                <p className="text-white/70">Enter the 6-digit code sent to</p>
                <p className="text-white font-medium">{formData.email}</p>
              </div>

              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="flex justify-center space-x-3">
                  {formData.otp.map((digit, index) => (
                    <Input
                      key={index}
                      id={`otp-${index}`}
                      type="text"
                      value={digit}
                      onChange={(e) => handleOtpChange(index, e.target.value)}
                      className="w-12 h-12 text-center text-lg font-semibold bg-white/10 border-white/20 text-white focus:border-white/40 focus:ring-white/20 rounded-xl"
                      maxLength={1}
                    />
                  ))}
                </div>

                <Button
                  type="submit"
                  disabled={isLoading || formData.otp.some((digit) => !digit)}
                  className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
                >
                  {isLoading ? "Verifying..." : "Verify Code"}
                </Button>
              </form>

              <div className="text-center">
                <button className="text-white/70 hover:text-white text-sm transition-colors">Resend code</button>
              </div>
            </div>
          )}

          {step === "success" && (
            <div className="flex-1 flex flex-col justify-center items-center space-y-6">
              <button
                onClick={resetToLogin}
                className="absolute top-6 right-6 text-white/70 hover:text-white transition-colors p-1 rounded-full hover:bg-white/10"
              >
                <X className="w-5 h-5" />
              </button>

              <div className="w-16 h-16 bg-white/20 backdrop-blur-sm border border-white/30 rounded-full flex items-center justify-center">
                <Check className="w-8 h-8 text-white" />
              </div>

              <div className="text-center space-y-2">
                <h1 className="text-2xl font-semibold text-white">
                  {step === "success" && mode === "signup" ? "Welcome!" : "Success!"}
                </h1>
                <p className="text-white/70">
                  {step === "success" && mode === "signup"
                    ? "Your account has been verified successfully"
                    : "Your password has been reset successfully"}
                </p>
              </div>

              <Button
                onClick={() => window.open("https://www.youtube.com/@diecastbydollar", "_blank")}
                className="w-full bg-white/20 hover:bg-white/30 text-white border border-white/30 hover:border-white/40 h-11 rounded-xl font-medium transition-all duration-200 backdrop-blur-sm"
              >
                Subscribe on YouTube
              </Button>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
