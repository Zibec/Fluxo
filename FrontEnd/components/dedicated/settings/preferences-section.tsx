"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui/form";
import {
	Select,
	SelectContent,
	SelectItem,
	SelectTrigger,
	SelectValue,
} from "@/components/ui/select";
import { useToast } from "@/hooks/use-toast";

const preferencesSchema = z.object({
	currency: z.string().min(1, "Selecione uma moeda"),
	dateFormat: z.string().min(1, "Selecione um formato de data"),
});

type PreferencesFormValues = z.infer<typeof preferencesSchema>;

interface PreferencesSectionProps {
	onBack: () => void;
}

export function PreferencesSection({ onBack }: PreferencesSectionProps) {
	const { toast } = useToast();

	const form = useForm<PreferencesFormValues>({
		resolver: zodResolver(preferencesSchema),
		defaultValues: {
			currency: "BRL",
			dateFormat: "DD/MM/YYYY",
		},
	});

	function onSubmit(data: PreferencesFormValues) {
		toast({
			title: "Sucesso",
			description: "Preferências atualizadas com sucesso!",
		});
		console.log("Form data:", data);
	}

	return (
		<div className="mx-auto max-w-2xl rounded-3xl bg-muted p-8">
			<h2 className="mb-8 text-center text-2xl font-semibold text-foreground">
				Preferências
			</h2>

			<Form {...form}>
				<form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
					<FormField
						control={form.control}
						name="currency"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">Moeda</FormLabel>
								<Select
									onValueChange={field.onChange}
									defaultValue={field.value}
								>
									<FormControl>
										<SelectTrigger className="bg-background">
											<SelectValue placeholder="Selecione uma moeda" />
										</SelectTrigger>
									</FormControl>
									<SelectContent>
										<SelectItem value="BRL">BRL - Real Brasileiro</SelectItem>
										<SelectItem value="USD">USD - Dólar Americano</SelectItem>
										<SelectItem value="EUR">EUR - Euro</SelectItem>
										<SelectItem value="GBP">GBP - Libra Esterlina</SelectItem>
									</SelectContent>
								</Select>
								<FormMessage />
							</FormItem>
						)}
					/>

					<FormField
						control={form.control}
						name="dateFormat"
						render={({ field }) => (
							<FormItem>
								<FormLabel className="text-foreground">
									Formato de Data
								</FormLabel>
								<Select
									onValueChange={field.onChange}
									defaultValue={field.value}
								>
									<FormControl>
										<SelectTrigger className="bg-background">
											<SelectValue placeholder="Selecione um formato" />
										</SelectTrigger>
									</FormControl>
									<SelectContent>
										<SelectItem value="DD/MM/YYYY">DD/MM/YYYY</SelectItem>
										<SelectItem value="MM/DD/YYYY">MM/DD/YYYY</SelectItem>
										<SelectItem value="YYYY-MM-DD">YYYY-MM-DD</SelectItem>
										<SelectItem value="DD-MM-YYYY">DD-MM-YYYY</SelectItem>
									</SelectContent>
								</Select>
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
