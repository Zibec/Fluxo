"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Pencil, Trash2 } from "lucide-react"

interface ScheduleCardProps {
  title: string
  value: number
  nextDate: string
  onEdit: () => void
  onDelete: () => void
}

export function ScheduleCard({ title, value, nextDate, onEdit, onDelete }: ScheduleCardProps) {
  return (
    <Card className="p-4 bg-white shadow-sm hover:shadow-md transition-shadow">
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-neutral-900 mb-1">{title}</h3>
          <p className="text-2xl font-bold text-blue-600 mb-2">
            R$ {value.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </p>
          <p className="text-sm text-neutral-600">Próxima: {nextDate}</p>
        </div>

        <div className="flex gap-2">
          <Button
            variant="outline"
            size="icon"
            onClick={onEdit}
            className="hover:bg-blue-50 hover:border-blue-600 bg-transparent"
          >
            <Pencil className="h-4 w-4 text-blue-600" />
            <span className="sr-only">Editar</span>
          </Button>
          <Button
            variant="outline"
            size="icon"
            onClick={onDelete}
            className="hover:bg-red-50 hover:border-red-600 bg-transparent"
          >
            <Trash2 className="h-4 w-4 text-red-600" />
            <span className="sr-only">Excluir</span>
          </Button>
        </div>
      </div>
    </Card>
  )
}
