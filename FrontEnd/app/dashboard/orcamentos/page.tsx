"use client";

import { useEffect, useState } from "react";
import { BudgetDetailCard } from "@/components/dedicated/orcamento/budget-detail-card";
import { AddBudgetDialog } from "@/components/dedicated/orcamento/add-budget-dialog";
import { createOrcamentoFormData } from "@/lib/service/orcamento/orcamento-schema";
import { orcamentoService } from "@/lib/service/orcamento/orcamento-service";
import { Button } from "@/components/ui/button";
import { EditBudgetDialog } from "@/components/dedicated/orcamento/edit-budget-dialog";

export default function OrcamentosPage() {
	const [editDialogOpen, setEditDialogOpen] = useState(false);
	const [addDialogOpen, setAddDialogOpen] = useState(false);
	const [selectedBudget, setSelectedBudget] =
		useState<createOrcamentoFormData>();
	const [budgets, setBudgets] = useState<createOrcamentoFormData[]>([]);

	useEffect(() => {
		const fetchBudgets = async () => {
			const response = await orcamentoService.getOrcamentos();
			setBudgets(response);
		};
		fetchBudgets();
	}, []);

	const handleEdit = (budgetId: string) => {
		const budget = budgets.find((b) => b.categoriaId === budgetId);
		if (budget) {
			setSelectedBudget(budget);
			setEditDialogOpen(true);
		}
	};

	return (
		<div className="min-h-screen bg-background">
			<main className="max-w-7xl mx-auto px-6 py-8">
				<h1 className="text-3xl font-bold mb-6 text-foreground">
					Meus Orçamentos
				</h1>

				<Button
					className="mb-6 bg-primary text-primary-foreground hover:bg-primary/90"
					onClick={() => setAddDialogOpen(true)}
				>
					Adicionar Orçamento
				</Button>

				<div className="space-y-6">
					{budgets?.map((budget) => (
							<BudgetDetailCard
								key={budget.categoriaId + "/" + budget.mes}
								budget={budget}
								onEdit={() => handleEdit(budget.categoriaId)}
							/>
						))}
				</div>
			</main>

			<EditBudgetDialog
				open={editDialogOpen}
				onOpenChange={setEditDialogOpen}
				budget={selectedBudget}
				setBudgets={setBudgets}
			/>

			<AddBudgetDialog
				open={addDialogOpen}
				onOpenChange={setAddDialogOpen}
				setBudgets={setBudgets}
			/>
		</div>
	);
}
