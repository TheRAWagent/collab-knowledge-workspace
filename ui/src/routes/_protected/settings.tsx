import { SettingsPage } from '@/modules/users/pages/settings-page'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_protected/settings')({
  component: SettingsPage,
})
