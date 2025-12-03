import { z } from "zod";

const ContaFormSchema = z.object({
	id: z.string().optional(),
	nome: z.string().nonempty("Nome da Conta é obrigatório"),
	saldo: z.number().min(0, "Saldo inicial não pode ser negativo"),
	tipo: z.enum(["Corrente", "Poupança", "Carteira"]),
	banco: z.string().nonempty("Banco é obrigatório"),
});

const CartaoFormSchema = z.object({
	id: z.string().optional(),
	numero: z
		.string()
		.min(16, "Número do cartão deve ter 16 dígitos")
		.max(16, "Número do cartão deve ter 16 dígitos"),
	titular: z
		.string()
		.nonempty("Nome do titular é obrigatório")
		.max(50, "Nome do titular deve ter no máximo 50 caracteres"),
	validade: z
		.string()
		.regex(
			/^(?:\d{4})-(0[1-9]|1[0-2])$/,
			"Validade deve estar no formato AA-YY",
		),
	cvv: z
		.string()
		.min(3, "CVV deve ter 3 dígitos")
		.max(4, "CVV deve ter 4 dígitos"),
	saldo: z.number().optional(),
	limite: z.number().min(0, "Limite do cartão não pode ser negativo"),
	dataFechamentoFatura: z
		.date()
		.min(new Date(), "A data não pode ser antes do dia de hoje."),
	dataVencimentoFatura: z
		.date()
		.min(new Date(), "A data não pode ser antes do dia de hoje."),
});

export type createContaFormData = z.infer<typeof ContaFormSchema>;
export type createCartaoFormData = z.infer<typeof CartaoFormSchema>;

export { ContaFormSchema, CartaoFormSchema };
