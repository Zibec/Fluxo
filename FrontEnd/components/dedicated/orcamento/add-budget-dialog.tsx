"use client";

import { useContext } from "react";
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
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { orcamentoService } from "@/lib/service/orcamento/orcamento-service";
import { useToast } from "@/hooks/use-toast";
import { DataContext } from "@/hooks/data-context";

interface AddBudgetDialogProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
	setBudgets: (budgets: createOrcamentoFormData[]) => void;
}

export function AddBudgetDialog({
	open,
	onOpenChange,
	setBudgets,
}: AddBudgetDialogProps) {

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

	const { toast } = useToast();

	const { categorias } = useContext(DataContext);

  const handleSave = async () => {
    try {
      await orcamentoService.createOrcamento(getValues())

      toast({
        title: "Orçamento criado",
        description: "O orçamento foi criado com sucesso.",
      })

      reset()
      setBudgets(await orcamentoService.getOrcamentos())
      onOpenChange(false)
    } catch (error: any) {
      const status = error?.response?.status

      if (status === 409) {
        toast({
          variant: "destructive",
          title: "Não foi possível criar o orçamento",
          description: "Já existe um orçamento para essa categoria nesse mês.",
        })
      } else {
        toast({
          variant: "destructive",
          title: "Erro ao criar orçamento",
          description: "Ocorreu um erro ao criar o orçamento. Tente novamente.",
        })
      }
    }
  }

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
							Adicionar Orçamento
						</DialogTitle>
					</DialogHeader>
					<div className="space-y-4 py-4">
						<div className="space-y-2">
							<Label htmlFor="edit-budget-category">Categoria:</Label>
							<input type="hidden" {...register("categoriaId")} />

							<Select onValueChange={(value) => setValue("categoriaId", value)}>
								<SelectTrigger id="edit-budget-category" className="w-full">
									<SelectValue placeholder="Selecione uma categoria" />
								</SelectTrigger>
								<SelectContent>
									{categorias?.map((category) => (
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
							<Label htmlFor="edit-budget-value">Limite:</Label>
							<Input
								id="edit-budget-value"
								type="number"
								placeholder="0.00"
								step="0.01"
								{...register("limite", { valueAsNumber: true })}
							/>
							<p className="text-sm text-red-600">{errors.limite?.message}</p>
						</div>

						<div className="space-y-2">
							<Label htmlFor="edit-budget-deadline">Data Limite:</Label>
							<Input
								id="edit-budget-value"
								placeholder="YYYY-MM"
								{...register("anoMes")}
							/>
							<p className="text-sm text-red-600">{errors.anoMes?.message}</p>
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