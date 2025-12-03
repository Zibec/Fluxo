"use client";

import { useContext, useState } from "react";
import {
	Dialog,
	DialogContent,
	DialogHeader,
	DialogTitle,
	DialogFooter,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
	Select,
	SelectContent,
	SelectItem,
	SelectTrigger,
	SelectValue,
} from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import {
	createTransacaoFormData,
	TransacaoFormSchema,
} from "@/lib/service/transacao/transacao-schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { Controller, useForm } from "react-hook-form";
import { transacaoService } from "@/lib/service/transacao/transacao-service";
import { useToast } from "@/hooks/use-toast";
import { formatCardNumber } from "@/lib/utils";
import { DataContext } from "@/hooks/data-context";

interface AddExpenseDialogProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
}

export function AddExpenseDialog({
	open,
	onOpenChange,
}: AddExpenseDialogProps) {
	const { categorias, perfis, contas, cartoes } = useContext(DataContext);

	const {
		handleSubmit,
		control,
		reset,
		getValues,
		formState: { errors },
	} = useForm<createTransacaoFormData>({
		resolver: zodResolver(TransacaoFormSchema),
	});

	const { toast } = useToast();
	const [tipo, setTipo] = useState<"DESPESA" | "RECEITA" | "REEMBOLSO">(
		"DESPESA",
	);
	const [isConta, setIsConta] = useState(true);

	const handleSaveExpense = () => {
		const data: createTransacaoFormData = getValues();

		data.status = "PENDENTE";
		data.data = new Date();
		data.tipo = tipo;
		data.avulsa = true;

		transacaoService
			.createTransacao(data)
			.then(() => {
				toast({
					title: "Transação realizada com sucessso",
					description: "A transação foi realizada com sucesso.",
				});

				reset();
				onOpenChange(false);
			})
			.catch((error) => {
				console.log(error);
			});
	};

	const handleCancelExpense = () => {
		reset();
		onOpenChange(false);
	};

	return (
		<Dialog open={open} onOpenChange={onOpenChange}>
			<DialogContent className="sm:max-w-[500px]">
				<DialogHeader>
					<DialogTitle className="text-xl font-semibold">
						Adicionar Despesa
					</DialogTitle>
				</DialogHeader>
				<form
					className="space-y-4 py-4"
					onSubmit={handleSubmit(handleSaveExpense)}
				>
					<Controller
						control={control}
						name="descricao"
						render={({ field: { onChange, value }, fieldState: { error } }) => (
							<div className="space-y-2">
								<Label htmlFor="description">Descrição</Label>
								<Input
									id="description"
									placeholder="Ex: Almoço no restaurante"
									value={value}
									onChange={onChange}
								/>
								<p>{error?.message}</p>
							</div>
						)}
					/>

					<Controller
						control={control}
						name="valor"
						render={({ field: { onChange, value }, fieldState: { error } }) => (
							<div className="space-y-2">
								<Label htmlFor="value">Valor</Label>
								<Input
									id="value"
									type="number"
									step="0.01"
									placeholder="0.00"
									value={value}
									onChange={(e) => onChange(+e.target.value)}
								/>
								<p>{error?.message}</p>
							</div>
						)}
					/>

					<Controller
						control={control}
						name="categoriaId"
						render={({ field: { onChange, value }, fieldState: { error } }) => (
							<div className="space-y-2">
								<Label htmlFor="category">Categoria</Label>
								<Select value={value} onValueChange={onChange}>
									<SelectTrigger id="category">
										<SelectValue placeholder="Selecione uma categoria" />
									</SelectTrigger>
									<SelectContent>
										{categorias &&
											categorias.map((category) => (
												<SelectItem key={category.id} value={category.id}>
													{category.nome}
												</SelectItem>
											))}
									</SelectContent>
								</Select>
								<p>{error?.message}</p>
							</div>
						)}
					/>

					<Controller
						control={control}
						name="perfilId"
						render={({ field: { onChange, value }, fieldState: { error } }) => (
							<div className="space-y-2">
								<Label htmlFor="profile">Perfil</Label>
								<Select value={value} onValueChange={onChange}>
									<SelectTrigger id="profile">
										<SelectValue placeholder="Selecione um perfil" />
									</SelectTrigger>
									<SelectContent>
										{perfis &&
											perfis.map((profile) => (
												<SelectItem key={profile.id} value={profile.id}>
													{profile.nome}
												</SelectItem>
											))}
									</SelectContent>
								</Select>
								<p>{error?.message}</p>
							</div>
						)}
					/>

					<Label htmlFor="paymentMethod">Tipo de Transacão</Label>
					<div className="flex flex-row gap-4">
						<Button
							onClick={() => setTipo("DESPESA")}
							variant={tipo === "DESPESA" ? "default" : "secondary"}
						>
							Despesa
						</Button>

						<Button
							onClick={() => setTipo("RECEITA")}
							variant={tipo === "RECEITA" ? "default" : "secondary"}
						>
							Receita
						</Button>
						<p>{errors.tipo?.message}</p>
					</div>

					<Label htmlFor="paymentMethod">Forma de Pagamento</Label>
					<div className="flex flex-row gap-4">
						<Button
							onClick={() => setIsConta(true)}
							variant={isConta ? "default" : "secondary"}
						>
							Conta
						</Button>

						<Button
							onClick={() => setIsConta(false)}
							variant={isConta ? "secondary" : "default"}
						>
							Cartão
						</Button>
						<p>{errors.pagamentoId?.message}</p>
					</div>

					{isConta ? (
						<Controller
							control={control}
							name="pagamentoId"
							render={({
								field: { onChange, value },
								fieldState: { error },
							}) => (
								<div className="space-y-2">
									<Select onValueChange={onChange} value={value}>
										<SelectTrigger id="paymentMethod">
											<SelectValue placeholder="Selecione uma forma de pagamento" />
										</SelectTrigger>
										<SelectContent>
											{contas &&
												contas.map((method) => (
													<SelectItem key={method.id} value={method.id}>
														{method.nome}
													</SelectItem>
												))}
										</SelectContent>
									</Select>
									<p>{error?.message}</p>
								</div>
							)}
						/>
					) : (
						<Controller
							control={control}
							name="pagamentoId"
							render={({
								field: { onChange, value },
								fieldState: { error },
							}) => (
								<div className="space-y-2">
									<Select onValueChange={onChange} value={value}>
										<SelectTrigger id="paymentMethod">
											<SelectValue placeholder="Selecione uma forma de pagamento" />
										</SelectTrigger>
										<SelectContent>
											{cartoes &&
												cartoes.map((method) => (
													<SelectItem key={method.id} value={method.id}>
														{formatCardNumber(method.numero)}
													</SelectItem>
												))}
										</SelectContent>
									</Select>
									<p>{error?.message}</p>
								</div>
							)}
						/>
					)}

					<DialogFooter className="gap-2">
						<Button variant="outline" onClick={handleCancelExpense}>
							Cancelar
						</Button>
						<Button className="bg-blue-600 hover:bg-blue-700" type="submit">
							Salvar
						</Button>
					</DialogFooter>
				</form>
			</DialogContent>
		</Dialog>
	);
}
