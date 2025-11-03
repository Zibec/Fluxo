"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Plus } from "lucide-react"

interface CardCardProps {
  title: string
  cardNumber: string
  onAdd: () => void
}

export function CardCard({ title, cardNumber, onAdd }: CardCardProps) {
  return (
    <Card className="p-4 bg-white shadow-sm hover:shadow-md transition-shadow">
      <div className="flex items-center justify-between">
        <div>
          <h3 className="font-semibold text-neutral-900">{title}</h3>
          <p className="text-sm text-neutral-500 font-mono">{cardNumber}</p>
        </div>
        <Button
          variant="ghost"
          size="sm"
          onClick={onAdd}
          className="text-blue-600 hover:text-blue-700 hover:bg-blue-50"
        >
          <Plus className="h-4 w-4 mr-1" />
          Adicionar
        </Button>
      </div>
    </Card>
  )
}
