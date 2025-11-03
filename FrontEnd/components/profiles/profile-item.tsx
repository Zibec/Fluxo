"use client"

import { Edit2 } from "lucide-react"
import { Button } from "@/components/ui/button"

interface ProfileItemProps {
  name: string
  onEdit: () => void
}

export function ProfileItem({ name, onEdit }: ProfileItemProps) {
  return (
    <div className="bg-white rounded-lg shadow-sm border border-neutral-200 p-4 flex items-center justify-between hover:shadow-md transition-shadow">
      <span className="text-lg font-medium text-neutral-900">{name}</span>
      <Button
        variant="ghost"
        size="icon"
        onClick={onEdit}
        className="text-blue-600 hover:text-blue-700 hover:bg-blue-50"
      >
        <Edit2 className="h-5 w-5" />
        <span className="sr-only">Editar {name}</span>
      </Button>
    </div>
  )
}
