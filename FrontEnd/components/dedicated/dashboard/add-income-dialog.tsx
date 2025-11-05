"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"

interface AddIncomeDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddIncomeDialog({ open, onOpenChange }: AddIncomeDialogProps) {
  const [value, setValue] = useState("")

  const handleSaveIncome = () => {
    console.log("[v0] Saving income:", value)
    setValue("")
    onOpenChange(false)
  }

  const handleCancelIncome = () => {
    setValue("")
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[400px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Receita</DialogTitle>
        </DialogHeader>
        <div className="py-4">
          <div className="space-y-2">
            <Label htmlFor="income-value">Valor</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-500">R$</span>
              <Input
                id="income-value"
                type="number"
                placeholder="0,00"
                value={value}
                onChange={(e) => setValue(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelIncome}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveIncome}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
