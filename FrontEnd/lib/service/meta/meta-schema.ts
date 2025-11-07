import {z} from 'zod'

const MetaFormSchema = z.object({
    id: z.string()
        .optional(),
    usuarioId: z.string()
        .optional(),
    tipo: z.enum(["POUPANCA", "REDUCAO_DIVIDA"]),
    status: z.enum(["ATIVA", "CONCLUIDA"]),
    descricao: z.string()
        .nonempty("Descrição é obrigatória"),
    valorAlvo: z.number()
        .min(0, "Valor alvo não pode ser negativo"),
    saldoAcumulado: z.number()
        .min(0, "Saldo acumulado não pode ser negativo")
        .default(0)
        .optional(),
    prazo: z.date()
        .optional()
})

export type createMetaFormData = z.infer<typeof MetaFormSchema>

export { MetaFormSchema }