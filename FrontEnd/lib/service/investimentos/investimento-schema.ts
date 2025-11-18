import {z} from 'zod'

const InvestimentoFormSchema = z.object({
    id: z.string()
        .optional(),
    usuarioId: z.string()
        .optional(),
    nome: z.string()
        .nonempty("Nome é obrigatório"),
    descricao: z.string()
        .nonempty("Descrição é obrigatória"),
    valorAtual: z.number(),
})

const HistoricoInvestimentoSchema = z.object({
    id: z.string()
        .optional(),
    investimentoId: z.string()
        .optional(),
    valorAtualizado: z.number()
        .min(0, "Valor atualizado não pode ser negativo"),
    data: z.date()
})

export type createInvestimentoFormData = z.infer<typeof InvestimentoFormSchema>
export type createHistoricoInvestimentoData = z.infer<typeof HistoricoInvestimentoSchema>

export { InvestimentoFormSchema, HistoricoInvestimentoSchema }