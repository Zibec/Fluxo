"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { DialogContent, DialogFooter, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/hooks/use-toast"
import { CartaoFormSchema, createCartaoFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { cartoesService } from "@/lib/service/contas-cartoes/contas-cartoes-service"
import { getCurrencySymbol } from "@/lib/utils"
import { zodResolver } from "@hookform/resolvers/zod"
import { Dialog, DialogTitle } from "@radix-ui/react-dialog"
import { PencilIcon, Plus, TrashIcon } from "lucide-react"
import { use, useEffect, useState } from "react"
import { useForm } from "react-hook-form"

interface CardCardProps {
  id: string
  card: createCartaoFormData
  setCards: React.Dispatch<React.SetStateAction<createCartaoFormData[]>>
}



export function CardCard({id, card, setCards }: CardCardProps) {
  const { toast } = useToast();

  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const  {
        register,
        handleSubmit,
        reset,
        getValues,
        formState: { errors }
    } = useForm<createCartaoFormData>({
        resolver: zodResolver(CartaoFormSchema)
      })

  useEffect(() => {
    reset(card);
  }, [card, reset]);

  const handleSaveEditedCard = async (data: createCartaoFormData) => {
    try {
      await cartoesService.updateCartao(id, data);

      toast({
        title: "Cartão atualizado",
        description: "As informações do cartão foram atualizadas com sucesso.",
      });

      setCards(await cartoesService.getAllCartoes());
      setIsDialogOpen(false);
    } catch (error) {
      console.error("Error updating card:", error);
    } 

  }

  const handleDeleteCard = async (id: string) => {
    try {
      await cartoesService.deleteCartao(id);

      toast({
        title: "Cartão deletado",
        description: "O cartão foi deletado com sucesso.",
      });

      setCards(await cartoesService.getAllCartoes());

    } catch (error) {
      console.error('Error deleting card:', error);
    }
  }

  return (
    <Card
      className="
        bg-[var(--color-card)]
        text-[var(--color-card-foreground)]
        border border-[var(--color-border)]
        rounded-[var(--radius-md)]
        shadow-sm hover:shadow-md
        transition-shadow
        p-4
      "
    >
      <div className="flex items-center justify-between">
        <div>
          <h3 className="font-semibold text-[var(--color-foreground)]">{card.titular}</h3>
          <p className="text-sm text-[var(--color-muted-foreground)] font-mono">{card.numero}</p>
        </div>
        <div>
          
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <div>
              <DialogTrigger asChild>
                <Button variant="ghost" onClick={() => setIsDialogOpen(true)}><PencilIcon color="blue" /></Button>
              </DialogTrigger>
              <Button variant="ghost" onClick={() => handleDeleteCard(id)}><TrashIcon color="red"/></Button>
            </div>
            <DialogContent>
              <form onSubmit={handleSubmit(handleSaveEditedCard)} className="space-y-4">
                <DialogTitle>Editar Cartão</DialogTitle>
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
                      <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-600">{getCurrencySymbol()}</span>
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

                  <DialogFooter className="gap-2 justify-items-center">
                    <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                      Cancelar
                    </Button>
                    <Button className="bg-blue-600 hover:bg-blue-700" type="submit">
                      Salvar
                    </Button>
                  </DialogFooter>
                </div>
              </form>
            </DialogContent>
        </Dialog>
        </div>
      </div>
    </Card>
  )
}
