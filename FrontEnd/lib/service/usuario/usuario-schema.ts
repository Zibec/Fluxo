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

export const emailSchema = z.object({
  emailAntigo: z.string().email('Email inválido'),
  emailNovo: z.string().email('Email inválido'),
}).refine((data) => data.emailAntigo !== data.emailNovo, {
  message: 'O email novo deve ser diferente do antigo',
  path: ['emailNovo'],
})


export const profileSchema = z.object({
  username: z.string().min(2, 'Nome deve ter pelo menos 2 caracteres'),
  formatoDataPreferido: z.string().optional(),
  moedaPreferida: z.string().optional(),
})

export type   createProfileFormData = z.infer<typeof profileSchema>
export type   createSecurityFormData = z.infer<typeof securitySchema>
export type   createEmailFormData = z.infer<typeof emailSchema>