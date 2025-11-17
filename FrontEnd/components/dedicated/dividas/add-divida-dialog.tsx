"use client"

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { useToast } from "@/components/ui/use-toast"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"
import { createDividaFormData, DividaFormSchema } from "@/lib/service/dividas/divida-schema"
import { dividaService } from "@/lib/service/dividas/divida-service"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { useEffect, useState } from "react"
import { createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { contasService } from "@/lib/service/contas-cartoes/contas-cartoes-service"
import { Popover, PopoverContent } from "@radix-ui/react-popover"
import { PopoverTrigger } from "@/components/ui/popover"
import { Calendar } from "@/components/ui/calendar"
import { CalendarIcon } from "lucide-react"

interface AddDividaDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  setDivida: (dividas: createDividaFormData[]) => void
}

export function AddDividaDialog({ open, onOpenChange, setDivida }: AddDividaDialogProps) {
      const  {
          register,
          handleSubmit,
          watch,
          getValues,
          setValue,
          formState: { errors }
      } = useForm<createDividaFormData>({
          resolver: zodResolver(DividaFormSchema)
        })

  const { toast } = useToast()
  
  const [contas, setContas] = useState<createContaFormData[]>([])

  useEffect(() => {
    const fetchContas = async () => {
      const contasData = await contasService.getAllContas()
      setContas(contasData)
    }

    fetchContas()
  }, [])

  const handleSave = async () => {
    await dividaService.createDivida(getValues())

    toast({
      title: "Meta criada",
      description: "A meta foi criada com sucesso.",
    })

    setDivida && setDivida(await dividaService.getAllDividas())
    onOpenChange(false)
  }

  const handleCancel = () => {
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <form className="space-y-4 py-4" onSubmit={handleSubmit(handleSave)}>
          <DialogHeader>
            <DialogTitle className="text-xl font-semibold">Adicionar Divida</DialogTitle>
          </DialogHeader>
          <div className="space-y-2">
            <Label htmlFor="nome">Nome:</Label>
            <Input
              id="nome"
              placeholder="Ex: Carro"
              {...register("nome")}
            />
            {errors.nome && <p className="text-sm text-red-600">{errors.nome.message}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="edit-budget-category">Conta Associada:</Label>
            <input type="hidden" {...register("contaAssociadaId")} />

            <Select
              onValueChange={(value) => setValue("contaAssociadaId", value)}
            >
              <SelectTrigger id="edit-budget-category" className="w-full">
                <SelectValue placeholder="Selecione uma categoria" />
              </SelectTrigger>
              <SelectContent>
                {contas && contas.map((conta) => (
                  <SelectItem key={conta.id} value={conta.id}>
                    {conta.nome}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <p className="text-sm text-red-600">{errors.contaAssociadaId?.message}</p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="valorDivida">Valor:</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-500">R$</span>
              <Input
                id="valorDivida"
                type="number"
                placeholder="0.00"
                step="0.01"
                className="pl-10"
                {...register("valorDivida", { valueAsNumber: true })}
              />
              {errors.valorDivida && <p className="text-sm text-red-600">{errors.valorDivida.message}</p>}
            </div>
          </div>

          <div className="space-y-2">
            <Label>Data Recorrente</Label>
            <Popover>
              <PopoverTrigger  asChild>
                <Button variant="outline" className="w-full justify-start text-left font-normal bg-transparent">
                  <CalendarIcon className="mr-2 h-4 w-4" />
                  {watch("dataLimite") ? (
                    format(watch("dataLimite"), "yyyy-MM-dd", { locale: ptBR } )
                  ) : (
                    <span className="text-neutral-500">Selecione uma data</span>
                  )}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <Calendar
                  mode="single"
                  selected={watch("dataLimite")}
                  onSelect={(date) => setValue("dataLimite", date)}
                />
              </PopoverContent>
            </Popover>
            <p className="text-sm text-red-600">{errors.dataLimite?.message}</p>
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
  )
}
