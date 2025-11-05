import {z} from 'zod'

const CategoriaFormSchema = z.object({
    id: z.string()
    .optional(),
    nome: z.string()
        .nonempty("Nome da Categoria não pode ser vazio")
})

export type createCategoriaFormData = z.infer<typeof CategoriaFormSchema>

export { CategoriaFormSchema }