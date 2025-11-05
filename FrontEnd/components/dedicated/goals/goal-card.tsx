"use client"

import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Pencil, Trash2 } from "lucide-react"

interface GoalCardProps {
  title: string
  value: number
  description: string
  onEdit: () => void
  onDelete: () => void
}

export function GoalCard({ title, value, description, onEdit, onDelete }: GoalCardProps) {
  return (
    <Card className="bg-white rounded-lg shadow-sm">
      <CardContent className="p-6 space-y-4">
        {/* Title and Value */}
        <div className="space-y-2">
          <h3 className="text-lg font-semibold text-neutral-900">{title}</h3>
          <p className="text-2xl font-bold text-blue-600">
            R$ {value.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </p>
        </div>

        {/* Description */}
        <div className="space-y-1">
          <h4 className="text-sm font-medium text-neutral-700">Descrição</h4>
          <p className="text-sm text-neutral-600 leading-relaxed">{description}</p>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center gap-3 pt-2">
          <Button
            variant="outline"
            className="flex-1 text-red-600 border-red-200 hover:bg-red-50 bg-transparent"
            onClick={onDelete}
          >
            <Trash2 className="h-4 w-4 mr-2" />
            Excluir
          </Button>
          <Button
            variant="outline"
            className="flex-1 text-blue-600 border-blue-200 hover:bg-blue-50 bg-transparent"
            onClick={onEdit}
          >
            <Pencil className="h-4 w-4 mr-2" />
            Editar
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
