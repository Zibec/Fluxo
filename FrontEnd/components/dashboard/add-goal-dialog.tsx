"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { CalendarIcon } from "lucide-react"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

interface GoalForm {
  type: "Poupança" | "Dívida"
  description: string
  targetValue: string
  deadline: Date | undefined
}

interface AddGoalDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function AddGoalDialog({ open, onOpenChange }: AddGoalDialogProps) {
  const [goalForm, setGoalForm] = useState<GoalForm>({
    type: "Poupança",
    description: "",
    targetValue: "",
    deadline: undefined,
  })

  const handleGoalFormChange = (field: string, value: string | Date | undefined) => {
    setGoalForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSaveGoal = () => {
    console.log("[v0] Saving goal:", goalForm)
    setGoalForm({
      type: "Poupança",
      description: "",
      targetValue: "",
      deadline: undefined,
    })
    onOpenChange(false)
  }

  const handleCancelGoal = () => {
    setGoalForm({
      type: "Poupança",
      description: "",
      targetValue: "",
      deadline: undefined,
    })
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Nova Meta</DialogTitle>
        </DialogHeader>
        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label>Tipo de Meta</Label>
            <div className="flex gap-2">
              <Button
                type="button"
                variant={goalForm.type === "Poupança" ? "default" : "outline"}
                className={goalForm.type === "Poupança" ? "flex-1 bg-blue-600 hover:bg-blue-700" : "flex-1"}
                onClick={() => handleGoalFormChange("type", "Poupança")}
              >
                Poupança
              </Button>
              <Button
                type="button"
                variant={goalForm.type === "Dívida" ? "default" : "outline"}
                className={goalForm.type === "Dívida" ? "flex-1 bg-blue-600 hover:bg-blue-700" : "flex-1"}
                onClick={() => handleGoalFormChange("type", "Dívida")}
              >
                Dívida
              </Button>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="description">Descrição</Label>
            <Input
              id="description"
              placeholder="Ex: Viagem de férias"
              value={goalForm.description}
              onChange={(e) => handleGoalFormChange("description", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="targetValue">Valor alvo</Label>
            <Input
              id="targetValue"
              type="number"
              placeholder="0.00"
              value={goalForm.targetValue}
              onChange={(e) => handleGoalFormChange("targetValue", e.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label>Data limite</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button variant="outline" className="w-full justify-start text-left font-normal bg-transparent">
                  <CalendarIcon className="mr-2 h-4 w-4" />
                  {goalForm.deadline ? (
                    format(goalForm.deadline, "PPP", { locale: ptBR })
                  ) : (
                    <span className="text-neutral-500">Selecione uma data</span>
                  )}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <Calendar
                  mode="single"
                  selected={goalForm.deadline}
                  onSelect={(date) => handleGoalFormChange("deadline", date)}
                  initialFocus
                />
              </PopoverContent>
            </Popover>
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelGoal}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveGoal}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
