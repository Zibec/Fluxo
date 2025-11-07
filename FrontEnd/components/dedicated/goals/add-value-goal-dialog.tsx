"use client"

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { CalendarIcon } from "lucide-react"
import { format } from "date-fns"
import { createMetaFormData, MetaFormSchema } from "@/lib/service/meta/meta-schema"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { metaService } from "@/lib/service/meta/meta-service"
import { useToast } from "@/components/ui/use-toast"
import { useState } from "react"
import { createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

interface AddGoalDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  id: string
}

export function AddValueGoalDialog({ open, onOpenChange, id }: AddGoalDialogProps) {
  const [value, setValue] = useState(0)
  const [selectedAccount, setSelectedAccount] = useState<string | null>(null)
  const [accounts, setAccounts] = useState<createContaFormData[]>([])

  const { toast } = useToast()

  const handleSave = async () => {
    await metaService.addValueToMeta(id, value)

    toast({
      title: "Aporte realizado",
      description: "O aporte foi realizado com sucesso.",
    })

    onOpenChange(false)
  }

  const handleCancel = () => {
    onOpenChange(false)
  }

  

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle className="text-xl font-semibold">Realizar Aporte</DialogTitle>
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

          <div className="space-y-2">
            <Label htmlFor="conta">Conta:</Label>
            <Select
                id="conta"
                value={selectedAccount}
                onValueChange={setSelectedAccount}
            >
                <SelectTrigger>
                    <SelectValue placeholder="Selecione uma conta" />
                </SelectTrigger>
                <SelectContent>
                    {accounts.map((account) => (
                        <SelectItem key={account.id} value={account.id}>
                            {account.nome}
                        </SelectItem>
                    ))}
                </SelectContent>
            </Select>
          </div>


        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancel}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSave}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

          
