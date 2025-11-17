import {z} from 'zod'

const AgendamentoFormSchema = z.object({
    id: z.string()
    .optional(),
    descricao: z.string()
        .nonempty("Nome da Categoria não pode ser vazio"),
    valor: z.number()
        .min(0, "O valor não pode ser negativo"),
    frequencia: z.enum(['DIARIA', 'SEMANAL', 'MENSAL', 'ANUAL'])
        .default('MENSAL'),
    proximaData: z.date()
        .min(new Date(), "A data deve ser no futuro"), // Data mínima é hoje ou mais tarde
    perfilId: z.string(),
    contaId: z.string()
})

export type createAgendamentoFormData = z.infer<typeof AgendamentoFormSchema>

export { AgendamentoFormSchema }