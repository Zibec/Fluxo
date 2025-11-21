import {z} from "zod"

const TransacaoFormSchema = z.object({
    id: z.string()
        .optional(),
    origemAgendamentoId: z.string()
        .optional(),
    descricao: z.string()
        .nonempty("Descrição não pode estar vázio"),
    valor: z.number()
        .min(0, "O Valor da Transação não pode ser menor que zero"),
    data: z.date()
        .optional(),
    status: z.enum(["PENDENTE", "EFETIVADA", "CANCELADA"])
        .default("PENDENTE"),
    categoriaId: z.string()
        .optional(),
    tipo: z.enum(["DESPESA", "RECEITA", "REEMBOLSO"])
        .default("DESPESA"),
    transacaoOriginalId: z.string()
        .optional(),
    pagamentoId: z.string()
        .nonempty("Deve selecionar uma forma de pagamento"),
    avulsa: z.boolean()
        .default(true)
        .optional(),
    perfilId: z.string()
        .optional(),
    usuarioId: z.string()
        .optional()
})

export type createTransacaoFormData = z.infer<typeof TransacaoFormSchema>

export { TransacaoFormSchema }