import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { DataContext } from "@/hooks/data-context";
import { useToast } from "@/hooks/use-toast";
import { createOrcamentoFormData } from "@/lib/service/orcamento/orcamento-schema";
import { orcamentoService } from "@/lib/service/orcamento/orcamento-service";
import Link from "next/link";
import { useContext, useEffect, useState } from "react";

interface BudgetSectionProps {
	orcamentos: createOrcamentoFormData[];
}

export function BudgetSection({ orcamentos }: BudgetSectionProps) {
	const { categorias } = useContext(DataContext);
	const [spents, setSpents] = useState<number[]>([]);
	const {toast} = useToast()

	useEffect(() => {
		const fetchSpents = async () => {
			const response = await Promise.all(
				orcamentos.map(async (budget) => {
					const spent = await orcamentoService.getOrcamentoTotalSpent(
						budget.categoriaId,
						`${budget.ano}-${budget.mes.toString().padStart(2, "0")}`,
					);
					return spent;
				}),
			);
			setSpents(response);
		};
		orcamentos && fetchSpents();
	}, [orcamentos]);

	useEffect(() => {
		if (!spents.length) return;

		orcamentos.forEach((budget, index) => {
			const percentage = (spents[index] / budget.limite) * 100;

			if (percentage > 0 && percentage <= 30) {
				toast({
					title: `Orçamento de ${getCategoriaNameById(budget.categoriaId)} 30% restante!`,
				});
			}

			if (percentage <= 0) {
				toast({
					title: `Orçamento de ${getCategoriaNameById(budget.categoriaId)} estourado!`,
				});
			}
		});
	}, [spents]);


	const getCategoriaNameById = (id: string) => {
		if(Array.isArray(categorias)) {
			const categoria = categorias.find((cat) => cat.id === id);
			return categoria?.nome;
		}
	};


	return (
		<Card
			className="transition-colors"
			style={{
				backgroundColor: "var(--card)",
				color: "var(--card-foreground)",
				borderColor: "var(--border)",
			}}
		>
			<CardHeader>
				<Link href="/dashboard/orcamentos">
					<CardTitle
						className="text-xl font-semibold cursor-pointer transition-colors"
						style={{
							color: "var(--foreground)",
						}}
					>
						Orçamentos
					</CardTitle>
				</Link>
			</CardHeader>

			<CardContent className="space-y-4">
				{orcamentos &&
					orcamentos.map((budget, index) => {
						const percentage = (spents[index] / budget.limite) * 100;

						return (
							<div key={budget.categoriaId + budget.mes} className="space-y-2">
								<div className="flex items-center justify-between">
									<span
										className="text-sm font-medium"
										style={{ color: "var(--muted-foreground)" }}
									>
										{getCategoriaNameById(budget.categoriaId) ||
											"Categoria Desconhecida"}
									</span>
									<span
										className="text-sm"
										style={{ color: "var(--muted-foreground)" }}
									>
										R$ {spents[index]} / R$ {budget.limite}
									</span>
								</div>

								<Progress
									value={percentage}
									className="h-2"
									style={{
										backgroundColor: "var(--muted)",
									}}
								/>
							</div>
						);
					})}
			</CardContent>
		</Card>
	);
}
