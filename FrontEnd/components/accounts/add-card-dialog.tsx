"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"

interface CardForm {
  name: string
  bank: string
  brand: string
  lastDigits: string
  totalLimit: string
  closingDate: string
  dueDate: string
}

interface AddCardDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddCardDialog({ open, onOpenChange }: AddCardDialogProps) {
  const [cardForm, setCardForm] = useState<CardForm>({
    name: "",
    bank: "",
    brand: "",
    lastDigits: "",
    totalLimit: "",
    closingDate: "",
    dueDate: "",
  })

  const handleCardFormChange = (field: string, value: string) => {
    setCardForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSaveCard = () => {
    console.log("[v0] Saving card:", cardForm)
    setCardForm({
      name: "",
      bank: "",
      brand: "",
      lastDigits: "",
      totalLimit: "",
      closingDate: "",
      dueDate: "",
    })
    onOpenChange(false)
  }

  const handleCancelCard = () => {
    setCardForm({
      name: "",
      bank: "",
      brand: "",
      lastDigits: "",
      totalLimit: "",
      closingDate: "",
      dueDate: "",
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Adicionar Cartão de Crédito</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="cardName">Nome do Cartão</Label>
            <Input
              id="cardName"
              placeholder="Ex: Cartão Pessoal Visa"
              value={cardForm.name}
              onChange={(e) => handleCardFormChange("name", e.target.value)}
            />
          </div>

          <div className="space-y-2">
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
          </div>

          <div className="space-y-2">
            <Label htmlFor="lastDigits">Últimos 4 dígitos</Label>
            <Input
              id="lastDigits"
              type="number"
              placeholder="1234"
              maxLength={4}
              value={cardForm.lastDigits}
              onChange={(e) => handleCardFormChange("lastDigits", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="totalLimit">Limite Total</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-600">R$</span>
              <Input
                id="totalLimit"
                type="number"
                placeholder="0.00"
                className="pl-10"
                value={cardForm.totalLimit}
                onChange={(e) => handleCardFormChange("totalLimit", e.target.value)}
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="closingDate">Data de Fechamento</Label>
              <Input
                id="closingDate"
                type="number"
                placeholder="Ex: 20"
                min="1"
                max="31"
                value={cardForm.closingDate}
                onChange={(e) => handleCardFormChange("closingDate", e.target.value)}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="dueDate">Data de Vencimento</Label>
              <Input
                id="dueDate"
                type="number"
                placeholder="Ex: 28"
                min="1"
                max="31"
                value={cardForm.dueDate}
                onChange={(e) => handleCardFormChange("dueDate", e.target.value)}
              />
            </div>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelCard}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveCard}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
