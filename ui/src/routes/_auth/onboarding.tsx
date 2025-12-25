import OnboardingCard from '@/modules/users/components/onboarding-card'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/onboarding')({
  component: OnboardingCard,
})
