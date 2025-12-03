"use client";

import { useEffect, useState } from "react";
import { DividaCard } from "@/components/dedicated/dividas/divida-card";
import { EditDividaDialog } from "@/components/dedicated/dividas/edit-divida-dialog";
import { Button } from "@/components/ui/button";
import { AddDividaDialog } from "@/components/dedicated/dividas/add-divida-dialog";
import { useToast } from "@/hooks/use-toast";
import { AddValueDividaDialog } from "@/components/dedicated/dividas/add-value-divida-dialog";
import { createDividaFormData } from "@/lib/service/dividas/divida-schema";
import { dividaService } from "@/lib/service/dividas/divida-service";

export default function MetasPage() {
	const [editDialogOpen, setEditDialogOpen] = useState(false);
	const [addDialogOpen, setAddDialogOpen] = useState(false);
	const [addValueDialogOpen, setAddValueDialogOpen] = useState(false);
	const [selectedDivida, setSelectedDivida] =
		useState<createDividaFormData | null>(null);
	const [dividas, setDividas] = useState<createDividaFormData[]>();

	const { toast } = useToast();

	useEffect(() => {
		const fetchDividas = async () => {
			const fetchedDividas = await dividaService.getAllDividas();
			setDividas(fetchedDividas);
		};
		fetchDividas();
	}, []);

	const handleEditDivida = (dividaId: string) => {
		const divida = dividas?.find((g) => g.id === dividaId);
		if (divida) {
			setSelectedDivida(divida);
			setEditDialogOpen(true);
		}
	};

	const handleAddValueDivida = (dividaId: string) => {
		const divida = dividas?.find((g) => g.id === dividaId);
		if (divida) {
			setSelectedDivida(divida);
			setAddValueDialogOpen(true);
		}
	};

	const handleDeleteDivida = async (dividaId: string) => {
		await dividaService.deleteDivida(dividaId);

		setDividas((prev) => prev?.filter((g) => g.id !== dividaId));

		toast({
			title: "Divida deletada",
			description: "A dívida foi deletada com sucesso.",
		});
	};

	return (
		<div className="min-h-screen bg-background text-foreground">
			<main className="max-w-7xl mx-auto px-6 py-8">
				<div className="justify-between">
					<h1 className="text-3xl font-bold mb-6 text-foreground">
						Minhas Dividas
					</h1>

					<Button
						className="mb-6 bg-primary text-primary-foreground hover:bg-primary/90"
						onClick={() => setAddDialogOpen(true)}
					>
						Adicionar Divida
					</Button>
				</div>

				<div className="space-y-4">
					{dividas?.map((divida) => (
						<DividaCard
							key={divida.id}
							divida={divida}
							onEdit={() => handleEditDivida(divida.id)}
							onDelete={() => handleDeleteDivida(divida.id)}
							onAddValue={() => handleAddValueDivida(divida.id)}
						/>
					))}
				</div>
			</main>

			<EditDividaDialog
				open={editDialogOpen}
				onOpenChange={setEditDialogOpen}
				divida={selectedDivida}
				setDivida={setDividas}
			/>

			<AddDividaDialog
				open={addDialogOpen}
				onOpenChange={setAddDialogOpen}
				setDivida={setDividas}
			/>

			<AddValueDividaDialog
				open={addValueDialogOpen}
				onOpenChange={setAddValueDialogOpen}
				id={selectedDivida?.id || ""}
				setDivida={setDividas}
			/>
		</div>
	);
}
