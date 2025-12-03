"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";

const informationsSchema = z.object({
	username: z.string().min(3, "Username deve ter pelo menos 3 caracteres"),
	email: z.string().email("Email inválido"),
	fullName: z.string().min(2, "Nome completo é obrigatório"),
	cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF inválido"),
	phone: z.string().regex(/^\+55 $$\d{2}$$ \d{5}-\d{4}$/, "Telefone inválido"),
	birthDate: z.string().regex(/^\d{2}\/\d{2}\/\d{4}$/, "Data inválida"),
});

type InformationsFormValues = z.infer<typeof informationsSchema>;

interface InformationsSectionProps {
	onBack: () => void;
}

export function InformationsSection({ onBack }: InformationsSectionProps) {
	const { toast } = useToast();

	const form = useForm<InformationsFormValues>({
		resolver: zodResolver(informationsSchema),
		defaultValues: {
			username: "usuario123",
			email: "usuario@gmail.com",
			fullName: "Nome Completo",
			cpf: "123.456.789-00",
			phone: "+55 (11) 98765-4321",
			birthDate: "01/01/1990",
		},
	});

	function onSubmit(data: InformationsFormValues) {
		toast({
			title: "Sucesso",
			description: "Informações atualizadas com sucesso!",
		});
		console.log("Form data:", data);
	}

	return (
		<div className="mx-auto max-w-2xl rounded-3xl bg-muted p-8">
			<h2 className="mb-8 text-center text-2xl font-semibold text-foreground">
				Informações
			</h2>

			<Form {...form}>
				<form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
					<FormField
						control={form.control}
						name="username"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">Username</FormLabel>
								<FormControl>
									<Input
										placeholder="usuario123"
										{...field}
										className="bg-background"
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					<FormField
						control={form.control}
						name="email"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">Email</FormLabel>
								<FormControl>
									<Input
										placeholder="usuario@gmail.com"
										type="email"
										{...field}
										className="bg-background"
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					<FormField
						control={form.control}
						name="fullName"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">Nome Completo</FormLabel>
								<FormControl>
									<Input
										placeholder="Nome Completo"
										{...field}
										className="bg-background"
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					<FormField
						control={form.control}
						name="cpf"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">CPF</FormLabel>
								<FormControl>
									<Input
										placeholder="123.456.789-00"
										{...field}
										className="bg-background"
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					<FormField
						control={form.control}
						name="phone"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">Telefone</FormLabel>
								<FormControl>
									<Input
										placeholder="+55 (11) 98765-4321"
										{...field}
										className="bg-background"
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					<FormField
						control={form.control}
						name="birthDate"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">
									Data de Nascimento
								</FormLabel>
								<FormControl>
									<Input
										placeholder="DD/MM/YYYY"
										{...field}
										className="bg-background"
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					<div className="flex gap-3 pt-4">
						<Button
							type="button"
							variant="outline"
							onClick={onBack}
							className="flex-1"
						>
							Cancelar
						</Button>
						<Button
							type="submit"
							className="flex-1 bg-primary text-primary-foreground hover:opacity-90"
						>
							Salvar
						</Button>
					</div>
				</form>
			</Form>
		</div>
	);
}
