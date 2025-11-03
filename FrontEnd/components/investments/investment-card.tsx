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
    <Card className="p-4 bg-white shadow-sm hover:shadow-md transition-shadow">
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-neutral-900 mb-2">{name}</h3>
          <p className="text-sm text-neutral-500 mb-1">Valor Atual:</p>
          <p className="text-2xl font-bold text-blue-600">
            R$ {currentValue.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </p>
        </div>
        <Button
          variant="ghost"
          size="icon"
          className="text-red-500 hover:text-red-700 hover:bg-red-50"
          onClick={onDelete}
        >
          <Trash2 className="h-5 w-5" />
          <span className="sr-only">Excluir</span>
        </Button>
      </div>
    </Card>
  )
}
