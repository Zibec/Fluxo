import { Label } from "@radix-ui/react-label"
import { TransitionStartFunction, useState, useTransition } from "react"
import { useForm, SubmitHandler } from "react-hook-form"
import { Button } from "../ui/button"
import { Input } from "../ui/input"
import { authService } from "@/lib/service/auth-service"
import { useToast } from "../ui/use-toast"
import { useRouter } from "next/navigation"

type FormData = {
  username: string
  password: string
}

export function LoginForm({startTransition}: {startTransition: TransitionStartFunction}) {
    const {
        register,
        handleSubmit,
        watch,
        formState: { errors }
    } = useForm<FormData>()

    const { toast } = useToast()
    const router = useRouter()

    const onSubmit: SubmitHandler<FormData> = (data) => {
        startTransition(async () => {
            try {
                await authService.login(data.username, data.password)

                console.log("Login bem sucedido")

                toast({
                    title: "Login realizado com sucesso!",
                    description: "Bem-vindo de volta ao RPG Manager.",
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
            <Input {...register("username", { required: true })} />
            {errors.username && <span>Este campo é obrigatório</span>}
           
            <Label htmlFor="password">Senha</Label>
            <Input {...register("password", { required: true })} />
            {errors.password && <span>Este campo é obrigatório</span>}
            <Button type="submit">Entrar</Button>
        </form>
    )
}