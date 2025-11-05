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
    <Card
      className="p-4 transition-shadow hover:shadow-md"
      style={{
        backgroundColor: "var(--card)",
        color: "var(--card-foreground)",
        borderColor: "var(--border)",
        boxShadow: "var(--shadow-sm)",
      }}
    >
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <h3
            className="text-lg font-semibold mb-1"
            style={{ color: "var(--foreground)" }}
          >
            {title}
          </h3>

          <p
            className="text-2xl font-bold mb-2"
            style={{ color: "var(--primary)" }}
          >
            R$ {value.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </p>

          <p
            className="text-sm"
            style={{ color: "var(--muted-foreground)" }}
          >
            Próxima: {nextDate}
          </p>
        </div>

        <div className="flex gap-2">
          <Button
            variant="outline"
            size="icon"
            onClick={onEdit}
            className="transition-colors"
            style={{
              backgroundColor: "transparent",
              borderColor: "var(--border)",
              color: "var(--primary)",
            }}
          >
            <Pencil className="h-4 w-4" />
            <span className="sr-only">Editar</span>
          </Button>

          <Button
            variant="outline"
            size="icon"
            onClick={onDelete}
            className="transition-colors"
            style={{
              backgroundColor: "transparent",
              borderColor: "var(--border)",
              color: "var(--destructive)",
            }}
          >
            <Trash2 className="h-4 w-4" />
            <span className="sr-only">Excluir</span>
          </Button>
        </div>
      </div>
    </Card>
  )
}
