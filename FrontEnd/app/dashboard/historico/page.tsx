"use client"

import { useEffect, useState } from "react"
import { TransactionCard } from "@/components/dedicated/history/transaction-card"
import { FiltersDialog } from "@/components/dedicated/history/filters-dialog"
import { Button } from "@/components/ui/button"
import { Filter } from "lucide-react"
import { createTransacaoFormData } from "@/lib/service/transacao/transacao-schema"
import { transacaoService } from "@/lib/service/transacao/transacao-service"

export default function HistoricoPage() {
  const [filtersOpen, setFiltersOpen] = useState(false)

  const [transactions, setTransactions] = useState<createTransacaoFormData[]>()

  useEffect(() => {
    const fetchTransacoes = async () => {
      const fetchedDividas = await transacaoService.getTransacoesByUser()
      setTransactions(fetchedDividas)
    }
    fetchTransacoes()
  }, [])

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
          {transactions && transactions.map((transaction) => (
            <TransactionCard
              key={transaction.id}
              transaction={transaction}
              onClick={() => handleTransactionClick(transaction.id)}
            />
          ))}
        </div>
      </main>

      <FiltersDialog open={filtersOpen} onOpenChange={setFiltersOpen} />
    </div>
  )
}
