"use client"

import { Button } from "@/components/ui/button"
import { useToast } from "@/hooks/use-toast"
import { createPerfilFormData } from "@/lib/service/perfil/perfil-schema"
import { perfilService } from "@/lib/service/perfil/perfil-service"
import { createTransacaoFormData } from "@/lib/service/transacao/transacao-schema"
import { transacaoService } from "@/lib/service/transacao/transacao-service"
import { formatDateByUserPreference, getCurrencySymbol } from "@/lib/utils"
import { useState, useEffect } from "react"

interface TransactionCardProps {
  transaction: createTransacaoFormData
  profiles?: createPerfilFormData[]
  onClick?: () => void
}

export function TransactionCard({ transaction, profiles, onClick }: TransactionCardProps) {
  // Cores adaptadas ao tema global, sem fixar tons neutros
  const getTypeStyles = () => {
    switch (transaction.tipo) {
      case "DESPESA":
        return "bg-destructive/10 text-destructive-foreground"
      case "RECEITA":
        return "bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-300"
      case "REEMBOLSO":
        return "bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300"
      default:
        return "bg-muted text-muted-foreground"
    }
  }

  return (
    <button
      onClick={onClick}
      className="w-full bg-card text-card-foreground rounded-lg border border-border p-4 shadow-sm hover:shadow-md hover:bg-accent/40 transition-all text-left"
    >
      <div className="flex items-center justify-between">
        <div className="flex-1">
          {/* Tipo + Valor */}
          <div className="flex items-center gap-3 mb-2">
            <span
              className={`text-xs font-medium px-3 py-1 rounded-full ${getTypeStyles()}`}
            >
              {transaction.tipo}
            </span>
            <span className="text-lg font-semibold text-foreground">
              {getCurrencySymbol()} {transaction.valor.toLocaleString("pt-BR", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })}
            </span>
          </div>

          {/* Data + Responsável */}
          <div className="flex items-center gap-4 text-sm text-muted-foreground">
            <span>{formatDateByUserPreference(transaction.data)}</span>
            <span>•</span>
            <span>{transaction.descricao}</span>
            <span>•</span>
            <span>{profiles?.find(p => p.id === transaction.perfilId)?.nome}</span>
            <span>•</span>
            <span>{transaction.status}</span>
          </div>
        </div>
      </div>
    </button>
  )
}
