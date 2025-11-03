"use client"

import { Pencil, Trash2 } from "lucide-react"
import { Button } from "@/components/ui/button"

interface CategoryItemProps {
  name: string
  onEdit: () => void
  onDelete: () => void
}

export function CategoryItem({ name, onEdit, onDelete }: CategoryItemProps) {
  return (
    <div className="flex items-center justify-between p-4 bg-white rounded-lg border border-neutral-200 shadow-sm hover:shadow-md transition-shadow">
      <span className="text-lg text-neutral-900 font-medium">{name}</span>

      <div className="flex items-center gap-2">
        <Button
          variant="ghost"
          size="sm"
          onClick={onEdit}
          className="text-neutral-600 hover:text-blue-600 hover:bg-blue-50"
        >
          <Pencil className="h-4 w-4 mr-1" />
          Editar
        </Button>
        <Button
          variant="ghost"
          size="sm"
          onClick={onDelete}
          className="text-neutral-600 hover:text-red-600 hover:bg-red-50"
        >
          <Trash2 className="h-4 w-4 mr-1" />
          Excluir
        </Button>
      </div>
    </div>
  )
}
