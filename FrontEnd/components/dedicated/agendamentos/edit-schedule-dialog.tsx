"use client";

import { z } from "zod";

import { useContext, useEffect, useState } from "react";
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
import { Checkbox } from "@/components/ui/checkbox";
import {
	Popover,
	PopoverContent,
	PopoverTrigger,
} from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { CalendarIcon } from "lucide-react";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import {
	AgendamentoFormSchema,
	createAgendamentoFormData,
} from "@/lib/service/agendamentos/agendamento-schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { createPerfilFormData } from "@/lib/service/perfil/perfil-schema";
import { perfilService } from "@/lib/service/perfil/perfil-service";
import { agendamentoService } from "@/lib/service/agendamentos/agendamento-service";
import { useToast } from "@/hooks/use-toast";
import { createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas";
import { contasService } from "@/lib/service/contas-cartoes/contas-cartoes-service";
import { DataContext } from "@/hooks/data-context";

interface EditScheduleDialogProps {
	open: boolean;
	onOpenChange: (open: boolean) => void;
	setSchedules: React.Dispatch<
		React.SetStateAction<createAgendamentoFormData[]>
	>;
	schedule?: createAgendamentoFormData;
}

export function EditScheduleDialog({
	open,
	onOpenChange,
	setSchedules,
	schedule,
}: EditScheduleDialogProps) {
	const {
		register,
		handleSubmit,
		watch,
		getValues,
		setValue,
		reset,
		formState: { errors },
	} = useForm<createAgendamentoFormData>({
		resolver: zodResolver(AgendamentoFormSchema),
	});

	const frequencia = z.enum(["DIARIA", "SEMANAL", "MENSAL", "ANUAL"]).options;

	const { perfis, contas } = useContext(DataContext);
	const { toast } = useToast();

	useEffect(() => {
		if (schedule) {
			reset(schedule);
		}
	}, [schedule, reset]);

	const handleSave = () => {
		agendamentoService
			.updateAgendamento(schedule.id, getValues())
			.then(() => {
				console.log(getValues());
				reset();

				toast({
					title: "Agendamento atualizado",
					description: "O agendamento foi atualizado com sucesso.",
				});

				agendamentoService.getAllAgendamentos().then((data) => {
					setSchedules(data);
				});
			})
			.catch(() => {
				console.log(getValues());
				toast({
					title: "Erro ao atualizar agendamento",
					description:
						"Ocorreu um erro ao atualizar o agendamento. Tente novamente.",
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
				<DialogHeader>
					<DialogTitle className="text-xl font-semibold">
						Transação Recorrente
					</DialogTitle>
				</DialogHeader>
				<form onSubmit={handleSubmit(handleSave)}>
					<div className="space-y-4 py-4">
						<div className="space-y-2">
							<Label htmlFor="title">Título da Transação</Label>
							<Input
								id="title"
								placeholder="Ex: Aluguel"
								{...register("descricao")}
							/>
							<p className="text-sm text-red-600">
								{errors.descricao?.message}
							</p>
						</div>

						<div className="space-y-2">
							<Label htmlFor="value">Valor</Label>
							<div className="relative">
								<span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-500">
									R$
								</span>
								<Input
									id="value"
									type="number"
									step="0.01"
									placeholder="0.00"
									className="pl-10"
									{...register("valor", { valueAsNumber: true })}
								/>
							</div>
							<p className="text-sm text-red-600">{errors.valor?.message}</p>
						</div>

						<div className="space-y-2">
							<Label>Data Recorrente</Label>
							<Popover>
								<PopoverTrigger asChild>
									<Button
										variant="outline"
										className="w-full justify-start text-left font-normal bg-transparent"
									>
										<CalendarIcon className="mr-2 h-4 w-4" />
										{watch("proximaData") ? (
											format(watch("proximaData"), "yyyy-MM-dd", {
												locale: ptBR,
											})
										) : (
											<span className="text-neutral-500">
												Selecione uma data
											</span>
										)}
									</Button>
								</PopoverTrigger>
								<PopoverContent className="w-auto p-0" align="start">
									<Calendar
										mode="single"
										selected={watch("proximaData")}
										onSelect={(date) => setValue("proximaData", date)}
									/>
								</PopoverContent>
							</Popover>
							<p className="text-sm text-red-600">
								{errors.proximaData?.message}
							</p>
						</div>

						<div className="space-y-2">
							<Label htmlFor="edit-budget-category">Conta Associada:</Label>
							<input type="hidden" {...register("contaId")} />

							<Select onValueChange={(value) => setValue("contaId", value)}>
								<SelectTrigger id="edit-budget-category" className="w-full">
									<SelectValue placeholder="Selecione uma categoria" />
								</SelectTrigger>
								<SelectContent>
									{contas &&
										contas.map((conta) => (
											<SelectItem key={conta.id} value={conta.id}>
												{conta.nome}
											</SelectItem>
										))}
								</SelectContent>
							</Select>
							<p className="text-sm text-red-600">{errors.contaId?.message}</p>
						</div>

						<div className="space-y-2">
							<Label htmlFor="category">Frequencia</Label>
							<input id="frequencia " hidden {...register("frequencia")} />

							<Select onValueChange={(value) => setValue("frequencia", value)}>
								<SelectTrigger id="category">
									<SelectValue placeholder="Selecione uma categoria" />
								</SelectTrigger>
								<SelectContent>
									{frequencia.map((freq) => (
										<SelectItem key={freq} value={freq}>
											{freq}
										</SelectItem>
									))}
								</SelectContent>
							</Select>
							<p className="text-sm text-red-600">
								{errors.frequencia?.message}
							</p>
						</div>

						<div className="space-y-2">
							<Label htmlFor="profile">Perfil</Label>
							<input id="perfilId" hidden {...register("perfilId")} />

							<Select onValueChange={(value) => setValue("perfilId", value)}>
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
							<p className="text-sm text-red-600">{errors.perfilId?.message}</p>
						</div>

						{/*<div className="space-y-2">
            <Label htmlFor="paymentMethod">Forma de pagamento</Label>
            <input id="formaPagamentoId" hidden {...register("formaPagamentoId")} />

            <Select
              
            >
              <SelectTrigger id="paymentMethod">
                <SelectValue placeholder="Selecione uma forma de pagamento" />
              </SelectTrigger>
              <SelectContent>
                {paymentMethods.map((method) => (
                  <SelectItem key={method} value={method}>
                    {method}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>*/}

						{/*<div className="flex items-center space-x-2">
            <Checkbox
              id="recurring"
            />
            <Label htmlFor="recurring" className="text-sm font-normal cursor-pointer">
              Recorrente
            </Label>
          </div>*/}
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
