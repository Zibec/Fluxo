import {z} from 'zod'

const RegisterFormSchema = z.object({
    username: z.string()
        .nonempty("Nome de usuário é obrigatório"),
    email: z.string().email()
        .regex(/^[^\s@]+@[^\s@]+\.[^\s@]+$/, "Email inválido")
        .nonempty("Email é obrigatório"),
    password: z.string()
        .nonempty("Senha é obrigatória")
        .min(4, "Senha deve ter no mínimo 4 caracteres")
})

const LoginFormSchema = z.object({
  username: z.string()
        .nonempty("Nome de usuário é obrigatório"),
  password: z.string()
        .nonempty("Senha é obrigatória")
        .min(4, "Senha deve ter no mínimo 4 caracteres")
})

export type createLoginFormData = z.infer<typeof LoginFormSchema>
export type createRegisterFormData = z.infer<typeof RegisterFormSchema>

export { RegisterFormSchema, LoginFormSchema }