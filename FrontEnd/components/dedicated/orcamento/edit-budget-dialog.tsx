"use client";

import { useState, useEffect } from "react";
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
	createOrcamentoFormData,
	OrcamentoFormSchema,
} from "@/lib/service/orcamento/orcamento-schema";
import { categoriasService } from "@/lib/service/categoria/categoria-service";
import { createCategoriaFormData } from "@/lib/service/categoria/categoria-schemas";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { orcamentoService } from "@/lib/service/orcamento/orcamento-service";
import { useToast } from "@/hooks/use-toast";

interface EditBudgetDialogProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
	budget: createOrcamentoFormData;
	setBudgets: (budgets: createOrcamentoFormData[]) => void;
}

export function EditBudgetDialog({
	open,
	onOpenChange,
	budget,
	setBudgets,
}: EditBudgetDialogProps) {
	const [categories, setCategories] = useState<createCategoriaFormData[]>([]);
	const { toast } = useToast();
	const {
		register,
		handleSubmit,
		watch,
		getValues,
		setValue,
		reset,
		formState: { errors },
	} = useForm<createOrcamentoFormData>({
		resolver: zodResolver(OrcamentoFormSchema),
	});

	useEffect(() => {
		if (budget) {
			reset(budget);
			console.log(budget);
			setValue(
				"anoMes",
				`${budget.ano}-${budget.mes.toString().padStart(2, "0")}`,
			);
		}
	}, [budget]);

	useEffect(() => {
		const fetchCategories = async () => {
			categoriasService.getAllCategorias().then((data) => {
				setCategories(data);
			});
		};

		fetchCategories();
	}, []);

	const handleSave = async () => {
		await orcamentoService
			.updateOrcamento(getValues())
			.then(async () => {
				toast({
					title: "Orçamento atualizado",
					description: "O orçamento foi atualizado com sucesso.",
				});

				reset();
				setBudgets(await orcamentoService.getOrcamentos());
			})
			.catch(() => {
				toast({
					title: "Erro ao atualizar orçamento",
					description:
						"Ocorreu um erro ao atualizar o orçamento. Tente novamente.",
				});
			});

		onOpenChange(false);
	};

	const handleCancel = () => {
		reset();
		onOpenChange(false);
	};

	return (
		<Dialog open={open} onOpenChange={onOpenChange}>
			<DialogContent className="sm:max-w-[500px]">
				<form onSubmit={handleSubmit(handleSave)}>
					<DialogHeader>
						<DialogTitle className="text-xl font-semibold">
							Editar Orçamento
						</DialogTitle>
					</DialogHeader>
					<div className="space-y-4 py-4">
						<div className="space-y-2">
							<Label htmlFor="edit-budget-category">Categoria:</Label>
							<input type="hidden" {...register("categoriaId")} />

							<Select
								onValueChange={(value) => setValue("categoriaId", value)}
								defaultValue={budget?.categoriaId?.toString()}
							>
								<SelectTrigger id="edit-budget-category" className="w-full">
									<SelectValue placeholder="Selecione uma categoria" />
								</SelectTrigger>
								<SelectContent>
									{categories &&
										categories.map((category) => (
											<SelectItem key={category.id} value={category.id}>
												{category.nome}
											</SelectItem>
										))}
								</SelectContent>
							</Select>
							<p className="text-sm text-red-600">
								{errors.categoriaId?.message}
							</p>
						</div>

						<div className="space-y-2">
							<Label htmlFor="edit-budget-value">Valor Limite:</Label>
							<Input
								id="edit-budget-value"
								type="number"
								placeholder="0.00"
								step="0.01"
								{...register("limite", { valueAsNumber: true })}
							/>
							<p className="text-sm text-red-600">{errors.limite?.message}</p>
						</div>
					</div>
					<DialogFooter className="gap-2">
						<Button variant="outline" onClick={handleCancel}>
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
