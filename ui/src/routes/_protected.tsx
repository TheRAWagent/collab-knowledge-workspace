import { Sidebar } from '@/components/sidebar'
import { createFileRoute, Outlet, redirect, useRouter } from '@tanstack/react-router'
import { useState } from 'react'
import { queryClient } from '@/lib/query-client'
import { getGetUserQueryOptions, useGetUser } from '@/modules/users/api'

export const Route = createFileRoute('/_protected')({
  component: RouteComponent,
  beforeLoad: async () => {
    try {
      await queryClient.ensureQueryData(getGetUserQueryOptions())
    } catch (error: any) {
      if (error.response?.status === 404) {
        throw redirect({
          to: '/onboarding',
        })
      }
      if (error.response?.status === 401) {
        
        throw redirect({
          to: '/sign-in',
        })
      }
      throw redirect({
        to: '/sign-in',
      })
    }
  },
})

function RouteComponent() {
  const router = useRouter();
  const {data: userData, isPending} = useGetUser();
  const [sidebarOpen, setSidebarOpen] = useState(true);

  if(isPending){
    return null;
  }

  if(userData?.name === null){
    router.navigate({to: '/onboarding'})
  }

  return <div className="mx-auto max-w-screen-2xl relative">
    <Sidebar open={sidebarOpen} onOpenChange={setSidebarOpen} />
    <div className='lg:pl-64'>
    <Outlet />
    </div>
  </div>
}
