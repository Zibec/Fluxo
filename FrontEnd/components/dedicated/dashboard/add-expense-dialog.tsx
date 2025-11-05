"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"

interface ExpenseForm {
  description: string
  value: string
  category: string
  profile: string
  paymentMethod: string
}

interface AddExpenseDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddExpenseDialog({ open, onOpenChange }: AddExpenseDialogProps) {
  const [expenseForm, setExpenseForm] = useState<ExpenseForm>({
    description: "",
    value: "",
    category: "",
    profile: "",
    paymentMethod: "",
  })

  const categories = ["Comida", "Transporte", "Lazer", "Saúde", "Educação", "Moradia", "Outros"]
  const profiles = ["Pessoal", "Trabalho", "Família", "Investimentos"]
  const paymentMethods = ["Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX", "Transferência Bancária"]

  const handleExpenseFormChange = (field: string, value: string) => {
    setExpenseForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSaveExpense = () => {
    console.log("[v0] Saving expense:", expenseForm)
    setExpenseForm({
      description: "",
      value: "",
      category: "",
      profile: "",
      paymentMethod: "",
    })
    onOpenChange(false)
  }

  const handleCancelExpense = () => {
    setExpenseForm({
      description: "",
      value: "",
      category: "",
      profile: "",
      paymentMethod: "",
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Adicionar Despesa</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="description">Descrição</Label>
            <Input
              id="description"
              placeholder="Ex: Almoço no restaurante"
              value={expenseForm.description}
              onChange={(e) => handleExpenseFormChange("description", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="value">Valor</Label>
            <Input
              id="value"
              type="number"
              placeholder="0.00"
              value={expenseForm.value}
              onChange={(e) => handleExpenseFormChange("value", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="category">Categoria</Label>
            <Select value={expenseForm.category} onValueChange={(value) => handleExpenseFormChange("category", value)}>
              <SelectTrigger id="category">
                <SelectValue placeholder="Selecione uma categoria" />
              </SelectTrigger>
              <SelectContent>
                {categories.map((category) => (
                  <SelectItem key={category} value={category}>
                    {category}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="profile">Perfil</Label>
            <Select value={expenseForm.profile} onValueChange={(value) => handleExpenseFormChange("profile", value)}>
              <SelectTrigger id="profile">
                <SelectValue placeholder="Selecione um perfil" />
              </SelectTrigger>
              <SelectContent>
                {profiles.map((profile) => (
                  <SelectItem key={profile} value={profile}>
                    {profile}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="paymentMethod">Forma de Pagamento</Label>
            <Select
              value={expenseForm.paymentMethod}
              onValueChange={(value) => handleExpenseFormChange("paymentMethod", value)}
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
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelExpense}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveExpense}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
