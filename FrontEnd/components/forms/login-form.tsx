import { Label } from "@radix-ui/react-label"
import { TransitionStartFunction, useState, useTransition } from "react"
import { useForm, SubmitHandler } from "react-hook-form"
import { Button } from "../ui/button"
import { Input } from "../ui/input"
import { authService } from "@/lib/service/auth/auth-service"
import { useRouter } from "next/navigation"
import { useToast } from "@/hooks/use-toast"
import { createLoginFormData, LoginFormSchema } from "@/lib/service/auth/auth-schemas"
import { zodResolver } from "@hookform/resolvers/zod"


export function LoginForm({startTransition}: {startTransition: TransitionStartFunction}) {
    const {
        register,
        handleSubmit,
        watch,
        formState: { errors }
    } = useForm<createLoginFormData>({
        resolver: zodResolver(LoginFormSchema)
    })

    const { toast } = useToast()
    const router = useRouter()

    const onSubmit: SubmitHandler<createLoginFormData> = (data) => {
        startTransition(async () => {
            try {
                await authService.login(data.username, data.password)

                console.log("Login bem sucedido")

                toast({
                    title: "Login realizado com sucesso!",
                    description: "Bem-vindo de volta ao Fluxo.",
                })

                router.push('/dashboard')

            } catch (error: any) {
                console.error("Erro ao fazer login:", error)

                toast({
                    title: "Erro no login",
                    description: error?.response?.data?.message ?? "Verifique suas credenciais.",
                    variant: "destructive",
                })
            }
            
        })
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="p-4 md:p-6 space-y-3 md:space-y-4">
            <Label htmlFor="username">Nome</Label>
            <Input {...register("username")} />
            <p>{errors.username?.message}</p>
           
            <Label htmlFor="password">Senha</Label>
            <Input {...register("password")} />
            <p>{errors.password?.message}</p>
            <Button type="submit">Entrar</Button>
        </form>
    )
}