"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"

interface EditBudgetDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  budget: {
    id: number
    title: string
    total: number
    category: string
    profile: string
  } | null
}

export function EditBudgetDialog({ open, onOpenChange, budget }: EditBudgetDialogProps) {
  const [value, setValue] = useState("")
  const [category, setCategory] = useState("")
  const [profile, setProfile] = useState("")

  const categories = ["Comida", "Transporte", "Lazer", "Saúde", "Educação", "Moradia", "Outros"]
  const profiles = ["Pessoal", "Trabalho", "Família", "Investimentos"]

  useEffect(() => {
    if (budget) {
      setValue(budget.total.toString())
      setCategory(budget.category)
      setProfile(budget.profile)
    }
  }, [budget])

  const handleSave = () => {
    console.log("[v0] Saving budget:", { id: budget?.id, value, category, profile })
    onOpenChange(false)
  }

  const handleCancel = () => {
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Editar Orçamento</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="edit-budget-value">Valor:</Label>
            <Input
              id="edit-budget-value"
              type="number"
              placeholder="0.00"
              value={value}
              onChange={(e) => setValue(e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="edit-budget-category">Categoria:</Label>
            <Select value={category} onValueChange={setCategory}>
              <SelectTrigger id="edit-budget-category">
                <SelectValue placeholder="Selecione uma categoria" />
              </SelectTrigger>
              <SelectContent>
                {categories.map((cat) => (
                  <SelectItem key={cat} value={cat}>
                    {cat}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="edit-budget-profile">Perfil:</Label>
            <Select value={profile} onValueChange={setProfile}>
              <SelectTrigger id="edit-budget-profile">
                <SelectValue placeholder="Selecione um perfil" />
              </SelectTrigger>
              <SelectContent>
                {profiles.map((prof) => (
                  <SelectItem key={prof} value={prof}>
                    {prof}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
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
