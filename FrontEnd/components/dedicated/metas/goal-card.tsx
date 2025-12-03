"use client";

import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2 } from "lucide-react";
import { createMetaFormData } from "@/lib/service/meta/meta-schema";
import { getCurrencySymbol } from "@/lib/utils";

interface GoalCardProps {
	goal: createMetaFormData;
	onEdit: () => void;
	onDelete: () => void;
	onAddValue?: () => void;
}

export function GoalCard({
	goal,
	onEdit,
	onDelete,
	onAddValue,
}: GoalCardProps) {
	return (
		<Card className="bg-card rounded-lg shadow-sm text-card-foreground">
			<CardContent className="p-6 space-y-4">
				{/* Title and Value */}
				<div className="space-y-2">
					<h3 className="text-lg font-semibold">{goal.tipo}</h3>

					<p className="text-2xl font-bold text-primary">
						{getCurrencySymbol()} {goal.saldoAcumulado || 0} /{" "}
						{getCurrencySymbol()} {goal.valorAlvo.toFixed(2)}
					</p>
				</div>

				{/* Description */}
				<div className="space-y-1">
					<h4 className="text-sm font-medium text-muted-foreground">
						Descrição
					</h4>

					<p className="text-sm text-foreground leading-relaxed">
						{goal.descricao}
					</p>
				</div>

				{/* Buttons */}
				<div className="flex items-center gap-3 pt-2">
					<Button
						variant="outline"
						className="flex-1 text-destructive border-destructive hover:bg-destructive/10"
						onClick={onDelete}
					>
						<Trash2 className="h-4 w-4 mr-2" />
						Excluir
					</Button>

					<Button
						variant="outline"
						className="flex-1 text-primary border-primary hover:bg-primary/10"
						onClick={onEdit}
					>
						<Pencil className="h-4 w-4 mr-2" />
						Editar
					</Button>

					<Button
						variant="outline"
						className="flex-1 text-primary border-primary hover:bg-primary/10"
						onClick={onAddValue}
					>
						<Pencil className="h-4 w-4 mr-2" />
						Realizar Aporte
					</Button>
				</div>
			</CardContent>
		</Card>
	);
}
