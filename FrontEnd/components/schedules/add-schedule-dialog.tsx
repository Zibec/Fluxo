"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Calendar } from "@/components/ui/calendar"
import { CalendarIcon } from "lucide-react"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

interface ScheduleForm {
  title: string
  value: string
  startDate: Date | undefined
  category: string
  profile: string
  paymentMethod: string
  recurring: boolean
}

interface AddScheduleDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddScheduleDialog({ open, onOpenChange }: AddScheduleDialogProps) {
  const [scheduleForm, setScheduleForm] = useState<ScheduleForm>({
    title: "",
    value: "",
    startDate: undefined,
    category: "",
    profile: "",
    paymentMethod: "",
    recurring: false,
  })

  const categories = ["Comida", "Transporte", "Lazer", "Saúde", "Educação", "Moradia", "Outros"]
  const profiles = ["Pessoal", "Trabalho", "Família", "Investimentos"]
  const paymentMethods = ["Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX", "Transferência Bancária"]

  const handleFormChange = (field: string, value: string | Date | boolean | undefined) => {
    setScheduleForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSave = () => {
    console.log("[v0] Saving schedule:", scheduleForm)
    setScheduleForm({
      title: "",
      value: "",
      startDate: undefined,
      category: "",
      profile: "",
      paymentMethod: "",
      recurring: false,
    })
    onOpenChange(false)
  }

  const handleCancel = () => {
    setScheduleForm({
      title: "",
      value: "",
      startDate: undefined,
      category: "",
      profile: "",
      paymentMethod: "",
      recurring: false,
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Transação Recorrente</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="title">Título da Transação</Label>
            <Input
              id="title"
              placeholder="Ex: Aluguel"
              value={scheduleForm.title}
              onChange={(e) => handleFormChange("title", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="value">Valor</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-500">R$</span>
              <Input
                id="value"
                type="number"
                placeholder="0.00"
                className="pl-10"
                value={scheduleForm.value}
                onChange={(e) => handleFormChange("value", e.target.value)}
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label>Data de início</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button variant="outline" className="w-full justify-start text-left font-normal bg-transparent">
                  <CalendarIcon className="mr-2 h-4 w-4" />
                  {scheduleForm.startDate ? (
                    format(scheduleForm.startDate, "dd/MM/yyyy", { locale: ptBR })
                  ) : (
                    <span className="text-neutral-500">Selecione uma data</span>
                  )}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <Calendar
                  mode="single"
                  selected={scheduleForm.startDate}
                  onSelect={(date) => handleFormChange("startDate", date)}
                  initialFocus
                />
              </PopoverContent>
            </Popover>
          </div>

          <div className="space-y-2">
            <Label htmlFor="category">Categoria</Label>
            <Select value={scheduleForm.category} onValueChange={(value) => handleFormChange("category", value)}>
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
            <Select value={scheduleForm.profile} onValueChange={(value) => handleFormChange("profile", value)}>
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
            <Label htmlFor="paymentMethod">Forma de pagamento</Label>
            <Select
              value={scheduleForm.paymentMethod}
              onValueChange={(value) => handleFormChange("paymentMethod", value)}
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

          <div className="flex items-center space-x-2">
            <Checkbox
              id="recurring"
              checked={scheduleForm.recurring}
              onCheckedChange={(checked) => handleFormChange("recurring", checked as boolean)}
            />
            <Label htmlFor="recurring" className="text-sm font-normal cursor-pointer">
              Recorrente
            </Label>
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
