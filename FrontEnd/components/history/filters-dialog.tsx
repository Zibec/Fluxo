"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Button } from "@/components/ui/button"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Calendar } from "@/components/ui/calendar"
import { CalendarIcon } from "lucide-react"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

interface FiltersForm {
  name: string
  dateFrom: Date | undefined
  dateTo: Date | undefined
  profile: string
  paymentMethod: string
}

interface FiltersDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function FiltersDialog({ open, onOpenChange }: FiltersDialogProps) {
  const [filtersForm, setFiltersForm] = useState<FiltersForm>({
    name: "",
    dateFrom: undefined,
    dateTo: undefined,
    profile: "",
    paymentMethod: "",
  })

  const profiles = ["Pessoa 1", "Pessoa 2", "Pessoa 3"]
  const paymentMethods = ["Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX", "Transferência Bancária"]

  const handleFormChange = (field: string, value: string | Date | undefined) => {
    setFiltersForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleApply = () => {
    console.log("[v0] Applying filters:", filtersForm)
    onOpenChange(false)
  }

  const handleCancel = () => {
    setFiltersForm({
      name: "",
      dateFrom: undefined,
      dateTo: undefined,
      profile: "",
      paymentMethod: "",
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Filtros</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="name">Nome</Label>
            <Input
              id="name"
              placeholder="Filtrar por descrição"
              value={filtersForm.name}
              onChange={(e) => handleFormChange("name", e.target.value)}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label>De:</Label>
              <Popover>
                <PopoverTrigger asChild>
                  <Button variant="outline" className="w-full justify-start text-left font-normal bg-transparent">
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {filtersForm.dateFrom ? (
                      format(filtersForm.dateFrom, "dd/MM/yyyy", { locale: ptBR })
                    ) : (
                      <span className="text-neutral-500">Data</span>
                    )}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={filtersForm.dateFrom}
                    onSelect={(date) => handleFormChange("dateFrom", date)}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
            </div>

            <div className="space-y-2">
              <Label>até:</Label>
              <Popover>
                <PopoverTrigger asChild>
                  <Button variant="outline" className="w-full justify-start text-left font-normal bg-transparent">
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {filtersForm.dateTo ? (
                      format(filtersForm.dateTo, "dd/MM/yyyy", { locale: ptBR })
                    ) : (
                      <span className="text-neutral-500">Data</span>
                    )}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={filtersForm.dateTo}
                    onSelect={(date) => handleFormChange("dateTo", date)}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="profile">Perfil</Label>
            <Select value={filtersForm.profile} onValueChange={(value) => handleFormChange("profile", value)}>
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
              value={filtersForm.paymentMethod}
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
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancel}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleApply}>
            Aplicar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
