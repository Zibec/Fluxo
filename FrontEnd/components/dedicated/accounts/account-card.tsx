"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Plus } from "lucide-react"

interface AccountCardProps {
  title: string
  subtitle: string
}

export function AccountCard({ title, subtitle }: AccountCardProps) {
  return (
    <Card
      className="
        p-4
        bg-[var(--color-card)]
        text-[var(--color-card-foreground)]
        border border-[var(--color-border)]
        rounded-[var(--radius-md)]
        shadow-sm hover:shadow-md
        transition-shadow
      "
    >
      <div className="flex items-center justify-between">
        <div>
          <h3 className="font-semibold text-[var(--color-foreground)]">{title}</h3>
          <p className="text-sm text-[var(--color-muted-foreground)]">{subtitle}</p>
        </div>
      </div>
    </Card>
  )
}
