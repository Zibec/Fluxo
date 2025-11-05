"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"

interface BudgetForm {
  description: string
  value: string
  category: string
  profile: string
}

interface AddBudgetDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddBudgetDialog({ open, onOpenChange }: AddBudgetDialogProps) {
  const [budgetForm, setBudgetForm] = useState<BudgetForm>({
    description: "",
    value: "",
    category: "",
    profile: "",
  })

  const categories = ["Comida", "Transporte", "Lazer", "Saúde", "Educação", "Moradia", "Outros"]
  const profiles = ["Pessoal", "Trabalho", "Família", "Investimentos"]

  const handleBudgetFormChange = (field: string, value: string) => {
    setBudgetForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSaveBudget = () => {
    console.log("[v0] Saving budget:", budgetForm)
    setBudgetForm({
      description: "",
      value: "",
      category: "",
      profile: "",
    })
    onOpenChange(false)
  }

  const handleCancelBudget = () => {
    setBudgetForm({
      description: "",
      value: "",
      category: "",
      profile: "",
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Orçamento</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="budget-description">Descrição</Label>
            <Input
              id="budget-description"
              placeholder="Ex: Orçamento mensal de alimentação"
              value={budgetForm.description}
              onChange={(e) => handleBudgetFormChange("description", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="budget-value">Valor</Label>
            <Input
              id="budget-value"
              type="number"
              placeholder="0.00"
              value={budgetForm.value}
              onChange={(e) => handleBudgetFormChange("value", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="budget-category">Categoria</Label>
            <Select value={budgetForm.category} onValueChange={(value) => handleBudgetFormChange("category", value)}>
              <SelectTrigger id="budget-category">
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
            <Label htmlFor="budget-profile">Perfil</Label>
            <Select value={budgetForm.profile} onValueChange={(value) => handleBudgetFormChange("profile", value)}>
              <SelectTrigger id="budget-profile">
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
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelBudget}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveBudget}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
