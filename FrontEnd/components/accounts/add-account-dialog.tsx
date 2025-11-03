"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"

interface AccountForm {
  name: string
  type: string
  initialBalance: string
}

interface AddAccountDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddAccountDialog({ open, onOpenChange }: AddAccountDialogProps) {
  const [accountForm, setAccountForm] = useState<AccountForm>({
    name: "",
    type: "",
    initialBalance: "",
  })

  const accountTypes = ["Corrente", "Poupança", "Investimento", "Carteira"]

  const handleAccountFormChange = (field: string, value: string) => {
    setAccountForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSaveAccount = () => {
    console.log("[v0] Saving account:", accountForm)
    setAccountForm({
      name: "",
      type: "",
      initialBalance: "",
    })
    onOpenChange(false)
  }

  const handleCancelAccount = () => {
    setAccountForm({
      name: "",
      type: "",
      initialBalance: "",
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Adicionar Conta</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="name">Nome/Descrição</Label>
            <Input
              id="name"
              placeholder="Ex: Conta Corrente Bradesco"
              value={accountForm.name}
              onChange={(e) => handleAccountFormChange("name", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="type">Tipo</Label>
            <Select value={accountForm.type} onValueChange={(value) => handleAccountFormChange("type", value)}>
              <SelectTrigger id="type">
                <SelectValue placeholder="Selecione um tipo" />
              </SelectTrigger>
              <SelectContent>
                {accountTypes.map((type) => (
                  <SelectItem key={type} value={type}>
                    {type}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="initialBalance">Saldo Inicial</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-600">R$</span>
              <Input
                id="initialBalance"
                type="number"
                placeholder="0.00"
                className="pl-10"
                value={accountForm.initialBalance}
                onChange={(e) => handleAccountFormChange("initialBalance", e.target.value)}
              />
            </div>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelAccount}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveAccount}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
