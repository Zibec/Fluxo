import { z } from 'zod'

export const securitySchema = z.object({
  currentPassword: z.string().min(1, 'Senha atual é obrigatória'),
  newPassword: z
    .string(),
    //.min(8, 'Senha deve ter pelo menos 8 caracteres')
    //.regex(/[A-Z]/, 'Deve conter pelo menos uma letra maiúscula')
    //.regex(/[0-9]/, 'Deve conter pelo menos um número'),
  confirmPassword: z.string(),
}).refine((data) => data.newPassword === data.confirmPassword, {
  message: 'As senhas não coincidem',
  path: ['confirmPassword'],
})

export type   createSecurityFormData = z.infer<typeof securitySchema>