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
import { Calendar } from "@/components/ui/calendar";
import {
	Popover,
	PopoverContent,
	PopoverTrigger,
} from "@/components/ui/popover";
import { CalendarIcon } from "lucide-react";
import { format } from "date-fns";
import {
	createMetaFormData,
	MetaFormSchema,
} from "@/lib/service/meta/meta-schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { metaService } from "@/lib/service/meta/meta-service";
import { useToast } from "@/components/ui/use-toast";

interface AddGoalDialogProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
	setMeta: (metas: createMetaFormData[]) => void;
}

export function AddGoalDialog({
	open,
	onOpenChange,
	setMeta,
}: AddGoalDialogProps) {
	const {
		register,
		handleSubmit,
		watch,
		getValues,
		setValue,
		formState: { errors },
	} = useForm<createMetaFormData>({
		resolver: zodResolver(MetaFormSchema),
	});

	const { toast } = useToast();

	const handleSave = async () => {
		await metaService.createMeta(getValues());

		toast({
			title: "Meta criada",
			description: "A meta foi criada com sucesso.",
		});

		setMeta && setMeta(await metaService.getAllMetas());
		onOpenChange(false);
	};

	const handleCancel = () => {
		onOpenChange(false);
	};

	return (
		<Dialog open={open} onOpenChange={onOpenChange}>
			<DialogContent className="sm:max-w-[500px]">
				<form className="space-y-4 py-4" onSubmit={handleSubmit(handleSave)}>
					<DialogHeader>
						<DialogTitle className="text-xl font-semibold">
							Adicionar Meta
						</DialogTitle>
					</DialogHeader>
					<div className="space-y-2">
						<Label htmlFor="descricao">Descrição:</Label>
						<Input
							id="descricao"
							placeholder="Ex: Viagem de férias"
							{...register("descricao")}
						/>
						{errors.descricao && (
							<p className="text-sm text-red-600">{errors.descricao.message}</p>
						)}
					</div>

					<div className="space-y-2">
						<Label htmlFor="valorAlvo">Valor Alvo:</Label>
						<div className="relative">
							<span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-500">
								R$
							</span>
							<Input
								id="valorAlvo"
								type="number"
								placeholder="0.00"
								step="0.01"
								className="pl-10"
								{...register("valorAlvo", { valueAsNumber: true })}
							/>
							{errors.valorAlvo && (
								<p className="text-sm text-red-600">
									{errors.valorAlvo.message}
								</p>
							)}
						</div>
					</div>

					<div className="space-y-2">
						<Label>Prazo:</Label>

						<input type="hidden" {...register("prazo")} />

						<Popover>
							<PopoverTrigger asChild>
								<Button
									variant="outline"
									className="w-full justify-start text-left font-normal bg-transparent"
								>
									<CalendarIcon className="mr-2 h-4 w-4" />
									{watch("prazo") ? (
										format(watch("prazo"), "yyyy-MM-dd")
									) : (
										<span className="text-neutral-500">Selecione uma data</span>
									)}
								</Button>
							</PopoverTrigger>
							<PopoverContent className="w-auto p-0" align="start">
								<Calendar
									mode="single"
									selected={watch("prazo")}
									onSelect={(date) => setValue("prazo", date)}
								/>
							</PopoverContent>
						</Popover>

						{errors.prazo && (
							<p className="text-sm text-red-600">{errors.prazo.message}</p>
						)}
					</div>

					<div className="space-y-2">
						<Label htmlFor="type">Tipo:</Label>
						<Input
							id="edit-type"
							placeholder="Ex: Poupança ou Redução de Dívida"
							{...register("tipo")}
						/>
						{errors.tipo && (
							<p className="text-sm text-red-600">{errors.tipo.message}</p>
						)}
					</div>

					<div className="space-y-2">
						<Label htmlFor="status">Status:</Label>
						<Input
							id="status"
							placeholder="Ex: Viagem de férias"
							{...register("status")}
						/>
						{errors.status && (
							<p className="text-sm text-red-600">{errors.status.message}</p>
						)}
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
