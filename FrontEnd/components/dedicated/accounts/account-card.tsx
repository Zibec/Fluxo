"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/hooks/use-toast"
import { ContaFormSchema, createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { contasService } from "@/lib/service/contas-cartoes/contas-cartoes-service"
import { zodResolver } from "@hookform/resolvers/zod"
import { id } from "date-fns/locale"
import { PencilIcon, Plus, TrashIcon } from "lucide-react"
import { useEffect, useState } from "react"
import { useForm } from "react-hook-form"

interface AccountCardProps {
  id: string
  account: createContaFormData
  setAccounts: React.Dispatch<React.SetStateAction<createContaFormData[]>>
}



export function AccountCard({id, account, setAccounts }: AccountCardProps) {
  const { toast } = useToast();

  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const  {
          register,
          handleSubmit,
          reset,
          getValues,
          formState: { errors }
      } = useForm<createContaFormData>({
          resolver: zodResolver(ContaFormSchema)
        })

  useEffect(() => {
      reset(account);
    }, [account, reset]);

  const handleSaveEditedAccount = async (data: createContaFormData) => {
    try {
      await contasService.updateConta(id, data);
      toast({
        title: "Conta atualizada",
        description: "As informações da conta foram atualizadas com sucesso.",
      });

      setAccounts(await contasService.getAllContas());
    } catch (error) {
      console.error("Error updating account:", error);
    }
  }

  const handleDeleteAccount = async (id: string) => {
    try {
      await contasService.deleteConta(id);

      toast({
        title: "Conta deletada",
        description: "A conta foi deletada com sucesso.",
      });

      setAccounts(await contasService.getAllContas());
    } catch (error) {
      console.error('Error deleting account:', error);
    }
  }

  return (
    <Card
      className="
        p-4
        bg-[var(--color-card)]
        text-[var(--color-card-foreground)]
        border border-[var(--color-border)]
        rounded-[var(--radius-md)]
        shadow-sm hover:shadow-md
        transition-shadow
      "
    >
      <div className="flex items-center justify-between">
        <div>
          <h3 className="font-semibold text-[var(--color-foreground)]">{account.nome}</h3>
          <p className="text-sm text-[var(--color-muted-foreground)] font-mono">{account.tipo}</p>
        </div>
        <div>
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <div>
              <DialogTrigger asChild>
                <Button variant="ghost" onClick={() => setIsDialogOpen(true)}><PencilIcon color="blue" /></Button>
              </DialogTrigger>
              <Button variant="ghost" onClick={() => handleDeleteAccount(id)}><TrashIcon color="red"/></Button>
            </div>
            <DialogContent className="sm:max-w-[500px]">
              <form onSubmit={handleSubmit(handleSaveEditedAccount)} className="space-y-4">
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
                <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                  Cancelar
                </Button>
                <Button className="bg-blue-600 hover:bg-blue-700" type="submit">
                  Salvar
                </Button>
              </DialogFooter>
              </form>
            </DialogContent>
        </Dialog>
        </div>
      </div>
    </Card>
  )
}
