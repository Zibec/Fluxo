"use client"

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"
import { ContaFormSchema, createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { contasService } from "@/lib/service/contas-cartoes/contas-cartoes-service"
import { useToast } from "@/hooks/use-toast"

interface AddAccountDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  setAccounts: React.Dispatch<React.SetStateAction<createContaFormData[]>>
}

export function AddAccountDialog({ open, onOpenChange, setAccounts }: AddAccountDialogProps) {
  const  {
          register,
          handleSubmit,
          watch,
          getValues,
          formState: { errors }
      } = useForm<createContaFormData>({
          resolver: zodResolver(ContaFormSchema)
        })

  const { toast } = useToast()

  const onSubmit = async () => {
    try {
      await contasService.createConta(getValues())
      console.log("Conta criada com sucesso")

      toast({
          title: "Conta criada com sucesso!"
      })

      setAccounts(await contasService.getAllContas())

      onOpenChange(false)
      
    } catch (error) {
      console.error("Erro ao criar conta:", error)
    }
  }

  const accountTypes = ["Corrente", "Poupança", "Investimento", "Carteira"]

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <form onSubmit={handleSubmit(onSubmit)}>
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Adicionar Conta</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="nome">Nome/Descrição</Label>
            <Input
              {...register("nome")}
            />
            <p>{errors.nome?.message}</p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="tipo">Tipo</Label>
            <Input
              {...register("tipo")}
            />
            <p>{errors.tipo?.message}</p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="saldo">Saldo Inicial</Label>
            <div className="relative">
              <Input type="number"
                {...register("saldo", { valueAsNumber: true })}
              />
            </div>
            <p>{errors.saldo?.message}</p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="banco">Banco</Label>
            <div className="relative">
              <Input
                {...register("banco")}
              />
            </div>
            <p>{errors.banco?.message}</p>
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
