import { z } from "zod";

const InvestimentoFormSchema = z.object({
	id: z.string().optional(),
	usuarioId: z.string().optional(),
	nome: z.string().nonempty("Nome é obrigatório"),
	descricao: z.string().nonempty("Descrição é obrigatória"),
	valorAtual: z.number(),
	contaId: z.string().nonempty("Uma conta deve ser selecionada."),
});

export type createInvestimentoFormData = z.infer<typeof InvestimentoFormSchema>;

export { InvestimentoFormSchema };
