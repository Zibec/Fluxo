import { z } from "zod";

const PerfilFormSchema = z.object({
	id: z.string().optional(),
	nome: z.string().nonempty("Nome é obrigatório"),
});

export type createPerfilFormData = z.infer<typeof PerfilFormSchema>;

export { PerfilFormSchema };
