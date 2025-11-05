"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { CalendarIcon } from "lucide-react"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

interface Goal {
  id: number
  title: string
  value: number
  description: string
  deadline?: Date
}

interface EditGoalDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  goal: Goal | null
}

export function EditGoalDialog({ open, onOpenChange, goal }: EditGoalDialogProps) {
  const [description, setDescription] = useState("")
  const [targetValue, setTargetValue] = useState("")
  const [deadline, setDeadline] = useState<Date | undefined>(undefined)

  useEffect(() => {
    if (goal) {
      setDescription(goal.description)
      setTargetValue(goal.value.toString())
      setDeadline(goal.deadline)
    }
  }, [goal])

  const handleSave = () => {
    console.log("[v0] Saving edited goal:", { description, targetValue, deadline })
    onOpenChange(false)
  }

  const handleCancel = () => {
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Editar Meta</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="edit-description">Descrição:</Label>
            <Input
              id="edit-description"
              placeholder="Ex: Viagem de férias"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="edit-targetValue">Valor alvo:</Label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-500">R$</span>
              <Input
                id="edit-targetValue"
                type="number"
                placeholder="0.00"
                className="pl-10"
                value={targetValue}
                onChange={(e) => setTargetValue(e.target.value)}
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label>Data limite:</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button variant="outline" className="w-full justify-start text-left font-normal bg-transparent">
                  <CalendarIcon className="mr-2 h-4 w-4" />
                  {deadline ? (
                    format(deadline, "PPP", { locale: ptBR })
                  ) : (
                    <span className="text-neutral-500">Selecione uma data</span>
                  )}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <Calendar mode="single" selected={deadline} onSelect={setDeadline} initialFocus />
              </PopoverContent>
            </Popover>
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
