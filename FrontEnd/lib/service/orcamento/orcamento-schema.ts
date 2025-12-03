import { z } from "zod";

const OrcamentoFormSchema = z.object({
	categoriaId: z.string().nonempty("Categoria é obrigatória"),
	anoMes: z
		.string()
		.regex(/^\d{4}-(0[1-9]|1[0-2])$/, "Formato deve ser YYYY-MM")
		.nonempty("Ano e mês são obrigatórios"),
	ano: z.number().optional(),
	mes: z.number().optional(),
	limite: z.number().min(0, "Limite não pode ser negativo"),
});

export type createOrcamentoFormData = z.infer<typeof OrcamentoFormSchema>;

export { OrcamentoFormSchema };
