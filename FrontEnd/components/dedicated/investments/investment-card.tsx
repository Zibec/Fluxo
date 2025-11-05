"use client"

import { Trash2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"

interface InvestmentCardProps {
  name: string
  currentValue: number
  onDelete: () => void
}

export function InvestmentCard({ name, currentValue, onDelete }: InvestmentCardProps) {
  return (
    <Card
      className="
        p-4 
        bg-[var(--color-surface)] 
        text-[var(--color-foreground)] 
        border 
        border-[var(--color-border)]
        shadow-sm 
        hover:shadow-md 
        transition-all 
        duration-200
      "
    >
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-[var(--color-foreground)] mb-2">
            {name}
          </h3>
          <p className="text-sm text-[var(--color-muted-foreground)] mb-1">
            Valor Atual:
          </p>
          <p className="text-2xl font-bold text-[var(--color-primary)]">
            R$ {currentValue.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </p>
        </div>

        <Button
          variant="ghost"
          size="icon"
          onClick={onDelete}
          className="
            text-[var(--color-danger)] 
            hover:bg-[var(--color-danger-bg-hover)] 
            hover:text-[var(--color-danger-hover)]
            transition-colors
          "
        >
          <Trash2 className="h-5 w-5" />
          <span className="sr-only">Excluir</span>
        </Button>
      </div>
    </Card>
  )
}
