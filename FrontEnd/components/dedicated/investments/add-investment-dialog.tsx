"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { createInvestimentoFormData, InvestimentoFormSchema } from "@/lib/service/investimentos/investimento-schema"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { investimentoService } from "@/lib/service/investimentos/investimento-service"
import { useToast } from "@/hooks/use-toast"

interface AddInvestmentDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddInvestmentDialog({ open, onOpenChange }: AddInvestmentDialogProps) {
  const [investmentForm, setInvestmentForm] = useState<createInvestimentoFormData>()

    const  {
          register,
          handleSubmit,
          watch,
          getValues,
          reset,
          formState: { errors }
      } = useForm<createInvestimentoFormData>({
          resolver: zodResolver(InvestimentoFormSchema)
        })

  const { toast } = useToast()

  const handleSaveInvestment = () => {
    console.log("Saving investment:", investmentForm)

    investimentoService.createInvestimento(getValues()).then((response) => {
        console.log("Investment created successfully:", response.data)

        toast({
          title: "Investimento criado com sucesso!",
          description: `Investimento foi adicionado.`
        })

        reset()
      })
      .catch((error) => {
        console.error("Error creating investment:", error)
      })
    
    onOpenChange(false)
  }

  const handleCancelInvestment = () => {
    reset()
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <form onSubmit={handleSubmit(handleSaveInvestment)}>
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Adicionar Investimento</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="name">Nome</Label>
            <Input
              id="name"
              placeholder="Ex: Tesouro Selic 2029"
              {...register("nome")}
            />
          {errors.nome && <p className="text-sm text-red-600">{errors.nome.message}</p>}
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Descrição</Label>
            <Textarea
              id="description"
              placeholder="Descreva o investimento..."
              rows={4}
              {...register("descricao")}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="initialValue">Valor Inicial</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-600">R$</span>
              <Input
                id="initialValue"
                type="number"
                placeholder="0.00"
                step="0.01"
                className="pl-10"
                {...register("valorAtual", { valueAsNumber: true })}
              />
              
            </div>
            {errors.valorAtual && <p className="text-sm text-red-600">{errors.valorAtual.message}</p>}
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelInvestment}>
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
