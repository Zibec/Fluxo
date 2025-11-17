import {z} from "zod"

const DividaFormSchema = z.object({
    id: z.string()
        .optional(),
    nome: z.string()
        .nonempty("Nome da Dívida é obrigatório"),
    valorDivida: z.number()
        .min(0, "Valor devedor não pode ser negativo"),
    valorAcumulado: z.number()
        .optional(),
    status: z.string()
        .optional(),
    contaAssociadaId: z.string()
        .nonempty("Conta associada é obrigatória"),
    dataLimite: z.date()
        .min(new Date(), "Data limite não pode ser no passado")
})

export type createDividaFormData = z.infer<typeof DividaFormSchema>

export { DividaFormSchema }