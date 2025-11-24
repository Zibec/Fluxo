"use client"

import { Edit2 } from "lucide-react"
import { Button } from "@/components/ui/button"

interface ProfileItemProps {
  name: string
  onEdit: () => void
}

export function ProfileItem({ name, onEdit }: ProfileItemProps) {
  return (
    <div
      className="
        bg-[var(--color-card)] 
        text-[var(--color-card-foreground)] 
        border 
        border-[var(--color-border)] 
        rounded-lg 
        p-4 
        flex 
        items-center 
        justify-between 
        shadow-sm 
        hover:shadow-md 
        transition-all 
        duration-200
      "
    >
      <span className="text-lg font-medium">{name}</span>
      <Button
        variant="ghost"
        size="icon"
        onClick={onEdit}
        className="
          text-[var(--color-primary)] 
          hover:text-[var(--color-primary-foreground)] 
          hover:bg-[var(--color-primary)]/10 
          transition-colors 
          duration-150
        "
      >
        <Edit2 className="h-5 w-5" />
        <span className="sr-only">Editar {name}</span>
      </Button>
    </div>
  )
}
