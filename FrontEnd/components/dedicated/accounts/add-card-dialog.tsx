"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { CartaoFormSchema, createCartaoFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { useToast } from "@/hooks/use-toast"
import { cartoesService } from "@/lib/service/contas-cartoes/contas-cartoes-service"

interface AddCardDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  setCards: React.Dispatch<React.SetStateAction<createCartaoFormData[]>>
}

export function AddCardDialog({ open, onOpenChange, setCards }: AddCardDialogProps) {
  const  {
          register,
          handleSubmit,
          watch,
          getValues,
          formState: { errors }
      } = useForm<createCartaoFormData>({
          resolver: zodResolver(CartaoFormSchema)
        })

  const { toast } = useToast()

  const onSubmit = async () => {
    try {
      await cartoesService.createCartao(getValues())

      toast({
          title: "Cartão criado com sucesso!"
      })

      console.log("Cartão criado com sucesso")
      onOpenChange(false)

      setCards(await cartoesService.getAllCartoes())
      
    } catch (error) {
      console.error("Erro ao criar cartão:", error)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Adicionar Cartão de Crédito</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit(onSubmit)}>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="titular">Titular do Cartão</Label>
            <Input
              {...register("titular")}
            />
            <p className="text-sm text-red-600">{errors.titular?.message}</p>
          </div>

          {/*<div className="space-y-2">
            <Label htmlFor="bank">Banco</Label>
            <Input
              id="bank"
              placeholder="Ex: Banco do Brasil"
              value={cardForm.bank}
              onChange={(e) => handleCardFormChange("bank", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="brand">Bandeira</Label>
            <Input
              id="brand"
              placeholder="Ex: Mastercard, Visa"
              value={cardForm.brand}
              onChange={(e) => handleCardFormChange("brand", e.target.value)}
            />
          </div>*/}

          <div className="space-y-2">
            <Label htmlFor="numero">Número do Cartão</Label>
            <Input
              {...register("numero")}
            />
            <p className="text-sm text-red-600">{errors.numero?.message}</p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="cvv">CVV</Label>
            <Input
              {...register("cvv")}
            />
            <p className="text-sm text-red-600">{errors.cvv?.message}</p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="limite">Limite Total</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-600">R$</span>
              <Input
                type="number"
                {...register("limite", { valueAsNumber: true })}
              />
            </div>
            <p className="text-sm text-red-600">{errors.limite?.message}</p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="validade">Validade</Label>
            <Input
              {...register("validade")}
            />
            <p className="text-sm text-red-600">{errors.validade?.message}</p>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="dataFechamento">Data de Fechamento</Label>
              <Input
                type="string"
                {...register("dataFechamentoFatura")}
              />
              <p className="text-sm text-red-600">{errors.dataFechamentoFatura?.message}</p>
            </div>

            <div className="space-y-2">
              <Label htmlFor="dataVencimento">Data de Vencimento</Label>
              <Input
                type="string"
                {...register("dataVencimentoFatura")}
              />
              <p className="text-sm text-red-600">{errors.dataVencimentoFatura?.message}</p>
            </div>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
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
