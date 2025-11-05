"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Plus } from "lucide-react"

interface CardCardProps {
  title: string
  cardNumber: string
}

export function CardCard({ title, cardNumber }: CardCardProps) {
  return (
    <Card
      className="
        bg-[var(--color-card)]
        text-[var(--color-card-foreground)]
        border border-[var(--color-border)]
        rounded-[var(--radius-md)]
        shadow-sm hover:shadow-md
        transition-shadow
        p-4
      "
    >
      <div className="flex items-center justify-between">
        <div>
          <h3 className="font-semibold text-[var(--color-foreground)]">{title}</h3>
          <p className="text-sm text-[var(--color-muted-foreground)] font-mono">{cardNumber}</p>
        </div>
      </div>
    </Card>
  )
}
