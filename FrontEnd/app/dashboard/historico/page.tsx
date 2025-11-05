"use client"

import { useState } from "react"
import { TransactionCard } from "@/components/dedicated/history/transaction-card"
import { FiltersDialog } from "@/components/dedicated/history/filters-dialog"
import { Button } from "@/components/ui/button"
import { Filter } from "lucide-react"

export default function HistoricoPage() {
  const [filtersOpen, setFiltersOpen] = useState(false)

  // Dados de exemplo
  const transactions = [
    { id: 1, type: "Débito" as const, value: 150.0, date: "15/01/25", responsible: "Pessoa 1" },
    { id: 2, type: "Crédito" as const, value: 2500.0, date: "14/01/25", responsible: "Pessoa 2" },
    { id: 3, type: "Conta" as const, value: 1800.0, date: "13/01/25", responsible: "Pessoa 1" },
    { id: 4, type: "Débito" as const, value: 89.9, date: "13/01/25", responsible: "Pessoa 1" },
    { id: 5, type: "Crédito" as const, value: 1200.0, date: "12/01/25", responsible: "Pessoa 3" },
    { id: 6, type: "Débito" as const, value: 450.0, date: "11/01/25", responsible: "Pessoa 2" },
  ]

  const handleTransactionClick = (id: number) => {
    console.log("[v0] Transaction clicked:", id)
    // TODO: abrir modal de detalhes
  }

  return (
    <div className="min-h-screen bg-background text-foreground">
      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Cabeçalho */}
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold">Histórico de Transações</h1>
          <Button
            variant="outline"
            className="gap-2 border-border text-muted-foreground hover:text-primary hover:bg-accent"
            onClick={() => setFiltersOpen(true)}
          >
            <Filter className="h-4 w-4" />
            Filtros
          </Button>
        </div>

        {/* Lista de transações */}
        <div className="space-y-3">
          {transactions.map((transaction) => (
            <TransactionCard
              key={transaction.id}
              type={transaction.type}
              value={transaction.value}
              date={transaction.date}
              responsible={transaction.responsible}
              onClick={() => handleTransactionClick(transaction.id)}
            />
          ))}
        </div>
      </main>

      <FiltersDialog open={filtersOpen} onOpenChange={setFiltersOpen} />
    </div>
  )
}
