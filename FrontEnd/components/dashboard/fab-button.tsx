"use client"

import { Plus } from "lucide-react"
import { Button } from "@/components/ui/button"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"

interface FabButtonProps {
  onActionSelect: (action: string) => void
}

export function FabButton({ onActionSelect }: FabButtonProps) {
  const fabActions = ["Adicionar Despesa", "Adicionar Receita", "Criar Novo Orçamento", "Criar Nova Meta"]

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          size="icon"
          className="fixed bottom-8 right-8 h-14 w-14 rounded-full bg-blue-600 hover:bg-blue-700 shadow-lg"
        >
          <Plus className="h-6 w-6 text-white" />
          <span className="sr-only">Adicionar transação</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" side="top" className="w-56 mb-2">
        {fabActions.map((action) => (
          <DropdownMenuItem key={action} className="cursor-pointer py-3" onSelect={() => onActionSelect(action)}>
            {action}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
