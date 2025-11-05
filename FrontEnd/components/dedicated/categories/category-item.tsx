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
    <div
      className="
        flex items-center justify-between p-4
        rounded-lg border bg-card text-card-foreground
        shadow-sm hover:shadow-md transition-shadow
      "
    >
      <span className="text-lg font-medium">{name}</span>

      <div className="flex items-center gap-2">
        <Button
          variant="ghost"
          size="sm"
          onClick={onEdit}
          className="text-muted-foreground hover:text-primary hover:bg-accent"
        >
          <Pencil className="h-4 w-4 mr-1" />
          Editar
        </Button>
        <Button
          variant="ghost"
          size="sm"
          onClick={onDelete}
          className="text-muted-foreground hover:text-destructive hover:bg-accent"
        >
          <Trash2 className="h-4 w-4 mr-1" />
          Excluir
        </Button>
      </div>
    </div>
  )
}
