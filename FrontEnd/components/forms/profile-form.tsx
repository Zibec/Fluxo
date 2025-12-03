"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import {
	Card,
	CardContent,
	CardDescription,
	CardHeader,
	CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
	Form,
	FormControl,
	FormDescription,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";
import { useEffect } from "react";
import { api } from "@/lib/axios";
import { usuarioService } from "@/lib/service/usuario/usuario-service";
import {
	createProfileFormData,
	profileSchema,
} from "@/lib/service/usuario/usuario-schema";
import {
	Select,
	SelectContent,
	SelectGroup,
	SelectItem,
	SelectTrigger,
	SelectValue,
} from "../ui/select";
import { Router, useRouter } from "next/router";

export function ProfileForm() {
	const { toast } = useToast();
	const form = useForm<createProfileFormData>({
		resolver: zodResolver(profileSchema),
		defaultValues: {
			username: "",
			moedaPreferida: " ",
			formatoDataPreferido: "dd-MM-yyyy",
		},
	});

	function onSubmit(data: createProfileFormData) {
		usuarioService
			.alterarPreferencias(data)
			.then(() => {
				toast({
					title: "Perfil atualizado",
					description: "Suas informações foram salvas com sucesso.",
				});
				console.log(data);
				window.location.reload();
			})
			.catch((error) => {
				toast({
					title: "Erro ao atualizar perfil",
					description:
						error.message || "Ocorreu um erro ao salvar suas informações.",
					variant: "destructive",
				});
			});
	}

	useEffect(() => {
		const fluxoUser = localStorage.getItem("fluxo_user");
		if (fluxoUser) {
			const user = JSON.parse(fluxoUser);
			form.reset(user);
		}
	}, []);

	return (
		<Card>
			<CardHeader>
				<CardTitle>Informações do Perfil</CardTitle>
				<CardDescription>Atualize suas informações pessoais</CardDescription>
			</CardHeader>
			<CardContent>
				<Form {...form}>
					<form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
						<div className="grid grid-cols-1 gap-6">
							<FormField
								control={form.control}
								name="username"
								render={({ field }) => (
									<FormItem>
										<FormLabel>Nome de Usuário</FormLabel>
										<FormControl>
											<Input placeholder="Seu nome de usuário" {...field} />
										</FormControl>
										<FormMessage />
									</FormItem>
								)}
							/>
							<FormField
								control={form.control}
								name="formatoDataPreferido"
								render={({ field }) => (
									<FormItem>
										<FormLabel>Formato de Data Preferido</FormLabel>
										<FormControl>
											<Select
												onValueChange={field.onChange}
												value={field.value}
											>
												<SelectTrigger>
													<SelectValue placeholder="Selecione um formato de data" />
												</SelectTrigger>
												<SelectContent>
													<SelectGroup>
														<SelectItem value="dd-MM-yyyy">
															DD/MM/YYYY
														</SelectItem>
														<SelectItem value="MM-dd-yyyy">
															MM/DD/YYYY
														</SelectItem>
														<SelectItem value="yyyy-MM-dd">
															YYYY/MM/DD
														</SelectItem>
													</SelectGroup>
												</SelectContent>
											</Select>
										</FormControl>
										<FormMessage />
									</FormItem>
								)}
							/>
							<FormField
								control={form.control}
								name="moedaPreferida"
								render={({ field }) => (
									<FormItem>
										<FormLabel>Moeda Preferida</FormLabel>
										<FormControl>
											<Select
												onValueChange={field.onChange}
												value={field.value}
											>
												<SelectTrigger>
													<SelectValue placeholder="Selecione uma moeda" />
												</SelectTrigger>
												<SelectContent>
													<SelectGroup>
														<SelectItem value="BRL">
															Real Brasileiro (BRL)
														</SelectItem>
														<SelectItem value="USD">
															Dólar Americano (USD)
														</SelectItem>
														<SelectItem value="EUR">Euro (EUR)</SelectItem>
													</SelectGroup>
												</SelectContent>
											</Select>
										</FormControl>
										<FormMessage />
									</FormItem>
								)}
							/>
						</div>

						<Button type="submit" className="w-full sm:w-auto">
							Salvar Alterações
						</Button>
					</form>
				</Form>
			</CardContent>
		</Card>
	);
}
