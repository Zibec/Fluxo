"use client";

import {
	Dialog,
	DialogContent,
	DialogHeader,
	DialogTitle,
	DialogFooter,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { createMetaFormData } from "@/lib/service/meta/meta-schema";
import { useToast } from "@/components/ui/use-toast";
import { useEffect, useState } from "react";
import { createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas";
import {
	Select,
	SelectContent,
	SelectItem,
	SelectTrigger,
	SelectValue,
} from "@/components/ui/select";
import { contasService } from "@/lib/service/contas-cartoes/contas-cartoes-service";
import { dividaService } from "@/lib/service/dividas/divida-service";
import { createDividaFormData } from "@/lib/service/dividas/divida-schema";

interface AddDividaDialogProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
	id: string;
	setDivida: React.Dispatch<
		React.SetStateAction<createDividaFormData[] | undefined>
	>;
}

export function AddValueDividaDialog({
	open,
	onOpenChange,
	id,
	setDivida,
}: AddDividaDialogProps) {
	const [value, setValue] = useState(0);
	const { toast } = useToast();

	const handleSave = async () => {
		await dividaService.addPayment(id, value);

		toast({
			title: "Aporte realizado",
			description: "O aporte foi realizado com sucesso.",
		});

		setDivida(await dividaService.getAllDividas());
		onOpenChange(false);
	};

	const handleCancel = () => {
		onOpenChange(false);
	};

	return (
		<Dialog open={open} onOpenChange={onOpenChange}>
			<DialogContent className="sm:max-w-[500px]">
				<DialogHeader>
					<DialogTitle className="text-xl font-semibold">
						Realizar Aporte
					</DialogTitle>
				</DialogHeader>
				<div className="space-y-2">
					<Label htmlFor="descricao">Valor:</Label>
					<Input
						id="descricao"
						placeholder="Ex: Viagem de férias"
						value={value}
						onChange={(e) => setValue(Number(e.target.value))}
						type="number"
					/>
				</div>
				<DialogFooter className="gap-2">
					<Button variant="outline" onClick={handleCancel}>
						Cancelar
					</Button>
					<Button
						className="bg-blue-600 hover:bg-blue-700"
						onClick={handleSave}
					>
						Salvar
					</Button>
				</DialogFooter>
			</DialogContent>
		</Dialog>
	);
}
