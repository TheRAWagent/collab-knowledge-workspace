import { getUser } from "@/modules/users/api";
import { createFileRoute, Outlet } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth")({
  component: RouteComponent,
  beforeLoad: async () => {
    try{

      const userResponse = await getUser();
      if (userResponse.status === 200) {
        return {
          redirect: "/",
        };
      }
    }catch(e){
      console.log(e);
    }
  }
});

function RouteComponent() {
  return (
    <div className="min-h-screen relative flex flex-col items-center justify-center p-4 overflow-hidden">
      <div
        className="absolute inset-0 bg-cover bg-center bg-no-repeat"
        style={{
          backgroundImage:
            "url('https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Fractal%20Glass%20-%204.jpg-m0AGFq8cSUKl8bpkwATMUJsUJokLwH.jpeg')",
        }}
      />

      {/* Subtle overlay for better contrast */}
      <div className="absolute inset-0 bg-black/20" />

      <div className="flex-1 flex items-center justify-center">
          <Outlet />
        </div>
    </div>
  );
}
