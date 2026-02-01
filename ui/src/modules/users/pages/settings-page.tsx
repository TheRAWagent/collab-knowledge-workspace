import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { useTheme } from "@/hooks/use-theme";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useGetUser, useUpdateUser } from "@/modules/users/api";
import { toast } from "sonner";
import { Loader2, Moon, Sun, Laptop } from "lucide-react";
import { useEffect } from "react";

const profileFormSchema = z.object({
  name: z.string().min(2, {
    message: "Name must be at least 2 characters.",
  }),
});

type ProfileFormValues = z.infer<typeof profileFormSchema>;

function ProfileForm() {
  const { data: userResponse, isLoading: isLoadingUser } = useGetUser();
  const { mutate: updateUser, isPending: isUpdating } = useUpdateUser();

  const form = useForm<ProfileFormValues>({
    resolver: zodResolver(profileFormSchema),
    defaultValues: {
      name: "",
    },
  });

  useEffect(() => {
    if (userResponse?.data) {
      form.reset({
        name: userResponse.data.name || "",
      });
    }
  }, [userResponse, form]);

  function onSubmit(data: ProfileFormValues) {
    updateUser(
      { data },
      {
        onSuccess: () => {
          toast.success("Profile updated successfully");
        },
        onError: () => {
          toast.error("Failed to update profile");
        },
      }
    );
  }

  if (isLoadingUser) {
    return <div className="flex justify-center p-8"><Loader2 className="h-6 w-6 animate-spin" /></div>;
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 max-w-xl">
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Name</FormLabel>
              <FormControl>
                <Input placeholder="Your name" {...field} />
              </FormControl>
              <FormDescription>
                This is your public display name.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        {/* Email - Read Only */}
        <div className="space-y-2">
          <FormLabel>Email</FormLabel>
          <Input disabled value={userResponse?.data?.email || ""} />
          <FormDescription>
            Email address cannot be changed currently.
          </FormDescription>
        </div>

        <Button type="submit" disabled={isUpdating}>
          {isUpdating && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
          Update profile
        </Button>
      </form>
    </Form>
  );
}

function AppearanceForm() {
  const { theme, setTheme } = useTheme();

  return (
    <div className="space-y-4 max-w-xl">
      <div>
        <h3 className="text-lg font-medium">Theme</h3>
        <p className="text-sm text-muted-foreground">
          Customize the appearance of the application.
        </p>
      </div>
      <div className="grid grid-cols-3 gap-4">
        <Button
          variant={theme === 'light' ? 'default' : 'outline'}
          className="h-24 flex flex-col gap-2"
          onClick={() => setTheme('light')}
        >
          <Sun className="h-6 w-6" />
          <span className="font-semibold">Light</span>
        </Button>
        <Button
          variant={theme === 'dark' ? 'default' : 'outline'}
          className="h-24 flex flex-col gap-2"
          onClick={() => setTheme('dark')}
        >
          <Moon className="h-6 w-6" />
          <span className="font-semibold">Dark</span>
        </Button>
        <Button
          variant={theme === 'system' ? 'default' : 'outline'}
          className="h-24 flex flex-col gap-2"
          onClick={() => setTheme('system')}
        >
          <Laptop className="h-6 w-6" />
          <span className="font-semibold">System</span>
        </Button>
      </div>
    </div>
  )
}

export function SettingsPage() {
  return (
    <div className="container mx-auto py-10 px-4 md:px-8">
      <div className="space-y-6">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Settings</h2>
          <p className="text-muted-foreground">
            Manage your account settings and preferences.
          </p>
        </div>
        <div className="flex flex-col space-y-8 lg:flex-row lg:space-x-12 lg:space-y-0">
          <div className="flex-1 lg:max-w-4xl">
            <Tabs defaultValue="profile" className="space-y-6">
              <TabsList>
                <TabsTrigger value="profile">Profile</TabsTrigger>
                <TabsTrigger value="appearance">Appearance</TabsTrigger>
              </TabsList>
              <TabsContent value="profile" className="space-y-6">
                <div>
                  <h3 className="text-lg font-medium">Profile</h3>
                  <p className="text-sm text-muted-foreground">
                    Update your personal information.
                  </p>
                </div>
                <div className="p-6 border rounded-lg">
                  <ProfileForm />
                </div>
              </TabsContent>
              <TabsContent value="appearance" className="space-y-6">
                <div className="p-6 border rounded-lg">
                  <AppearanceForm />
                </div>
              </TabsContent>
            </Tabs>
          </div>
        </div>
      </div>
    </div>
  );
}
