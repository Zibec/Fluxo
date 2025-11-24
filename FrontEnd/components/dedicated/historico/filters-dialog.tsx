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
import { createPerfilFormData } from "@/lib/service/perfil/perfil-schema"

export interface FiltersForm {
  name: string
  dateFrom: Date | undefined
  dateTo: Date | undefined
  profile: string
  tipo: string
  status: string
}

interface FiltersDialogProps {
  open: boolean
  profiles: createPerfilFormData[]
  onOpenChange: (open: boolean) => void
  onApplyFilters: (filters: FiltersForm) => void 
  onClearFilters: () => void
}

export function FiltersDialog({ open, profiles, onOpenChange, onApplyFilters, onClearFilters }: FiltersDialogProps) {
  const [filtersForm, setFiltersForm] = useState<FiltersForm>({
    name: "",
    dateFrom: undefined,
    dateTo: undefined,
    profile: "",
    tipo: "",
    status: ""
  })

  const tipo = ["Reembolso", "Receita", "Despesa"]
  const status = ["Pendente", "Efetivada"]


  const handleFormChange = (field: string, value: string | Date | undefined) => {
    setFiltersForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleApply = () => {
    onApplyFilters(filtersForm)
    onOpenChange(false)
  }

  const handleCancel = () => {
    setFiltersForm({
      name: "",
      dateFrom: undefined,
      dateTo: undefined,
      profile: "",
      tipo: "",
      status: ""
    })
    onClearFilters()
    onOpenChange(false)
  }

  const handleClearFilters = () => {
    setFiltersForm({
      name: "",
      dateFrom: undefined,
      dateTo: undefined,
      profile: "",
      tipo: "",
      status: ""
    })
    onClearFilters()
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
                  <SelectItem key={profile.id} value={profile.id}>
                    {profile.nome}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="tipo">Tipo de Transação</Label>
            <Select
              value={filtersForm.tipo}
              onValueChange={(value) => handleFormChange("tipo", value)}
            >
              <SelectTrigger id="tipo">
                <SelectValue placeholder="Selecione um tipo de transação" />
              </SelectTrigger>
              <SelectContent>
                {tipo.map((method) => (
                  <SelectItem key={method} value={method.toUpperCase()}>
                    {method}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="status">Status de Transação</Label>
            <Select
              value={filtersForm.status}
              onValueChange={(value) => handleFormChange("status", value)}
            >
              <SelectTrigger id="status">
                <SelectValue placeholder="Selecione um status de transação" />
              </SelectTrigger>
              <SelectContent>
                {status.map((method) => (
                  <SelectItem key={method} value={method.toUpperCase()}>
                    {method}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="destructive" onClick={handleClearFilters}>
            Limpar Filtros
          </Button>
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
