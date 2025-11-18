"use client"

import { Trash2 } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { getCurrencySymbol } from "@/lib/utils"

interface InvestmentCardProps {
  name: string
  currentValue: number
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
            {getCurrencySymbol()} {currentValue.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </p>
        </div>
      </div>
    </Card>
  )
}
