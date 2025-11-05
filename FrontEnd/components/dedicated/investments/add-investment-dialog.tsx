"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"

interface InvestmentForm {
  name: string
  description: string
  initialValue: string
}

interface AddInvestmentDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddInvestmentDialog({ open, onOpenChange }: AddInvestmentDialogProps) {
  const [investmentForm, setInvestmentForm] = useState<InvestmentForm>({
    name: "",
    description: "",
    initialValue: "",
  })

  const handleInvestmentFormChange = (field: string, value: string) => {
    setInvestmentForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSaveInvestment = () => {
    console.log("[v0] Saving investment:", investmentForm)
    setInvestmentForm({
      name: "",
      description: "",
      initialValue: "",
    })
    onOpenChange(false)
  }

  const handleCancelInvestment = () => {
    setInvestmentForm({
      name: "",
      description: "",
      initialValue: "",
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Adicionar Investimento</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="name">Nome</Label>
            <Input
              id="name"
              placeholder="Ex: Tesouro Selic 2029"
              value={investmentForm.name}
              onChange={(e) => handleInvestmentFormChange("name", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Descrição</Label>
            <Textarea
              id="description"
              placeholder="Descreva o investimento..."
              rows={4}
              value={investmentForm.description}
              onChange={(e) => handleInvestmentFormChange("description", e.target.value)}
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
                className="pl-10"
                value={investmentForm.initialValue}
                onChange={(e) => handleInvestmentFormChange("initialValue", e.target.value)}
              />
            </div>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelInvestment}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveInvestment}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
