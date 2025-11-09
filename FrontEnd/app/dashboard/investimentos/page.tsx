"use client"

import { useEffect, useState } from "react"
import { Plus } from "lucide-react"
import { InvestmentCard } from "@/components/dedicated/investments/investment-card"
import { AddInvestmentDialog } from "@/components/dedicated/investments/add-investment-dialog"
import { Button } from "@/components/ui/button"
import { createInvestimentoFormData } from "@/lib/service/investimentos/investimento-schema"
import { api } from "@/lib/axios"
import { investimentoService } from "@/lib/service/investimentos/investimento-service"

export default function InvestimentosPage() {
  const [balance] = useState(12500.0)
  const [selicRate, setSelicRate] = useState(0.0)
  const [isAddInvestmentOpen, setIsAddInvestmentOpen] = useState(false)
  const [investments, setInvestments] = useState<createInvestimentoFormData[]>([])

  const handleAddInvestment = () => {
    setIsAddInvestmentOpen(true)
  }

  useEffect(() => {
    async function fetchInvestments() {
      await investimentoService.getInvestimentosByUserId().then((response) => {
        setInvestments(response.data)
      }).catch((error) => {
        console.error("Error fetching investments:", error)
      })
    }

    async function fetchSelicRate() {
      await investimentoService.getTaxaSelic().then((response) => {
        setSelicRate(response.data.valor)
      }).catch((error) => {
        console.error("Error fetching Selic rate:", error)
      })
    }

    fetchInvestments()
    fetchSelicRate()
  }, [])

  return (
    <div
      className="
        min-h-screen 
        bg-[var(--color-background)] 
        text-[var(--color-foreground)] 
        transition-colors
      "
    >
      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Cabeçalho e botão adicionar */}
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold text-[var(--color-foreground)]">
            Meus Investimentos
          </h1>
          <Button
            size="icon"
            className="
              rounded-full 
              bg-[var(--color-primary)] 
              text-[var(--color-primary-foreground)] 
              hover:opacity-90 
              shadow-md 
              transition
            "
            onClick={handleAddInvestment}
          >
            <Plus className="h-5 w-5" />
            <span className="sr-only">Adicionar Investimento</span>
          </Button>
        </div>

        {/* Taxa Selic */}
        <div
          className="
            border 
            rounded-lg 
            px-4 py-3 mb-6 inline-block
            bg-[var(--color-secondary)]
            border-[var(--color-border)]
            text-[var(--color-secondary-foreground)]
            transition-colors
          "
        >
          <p className="text-sm font-medium">
            Taxa Selic:{" "}
            <span className="font-bold">{selicRate}%</span>
          </p>
        </div>

        {/* Lista de investimentos */}
        <div className="space-y-4">
          {investments.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-[var(--color-muted-foreground)]">
                Nenhum investimento cadastrado ainda.
              </p>
            </div>
          ) : (
            investments.map((investment) => (
              <InvestmentCard
                key={investment.id}
                name={investment.nome}
                currentValue={investment.valorAtual}
              />
            ))
          )}
        </div>
      </main>

      <AddInvestmentDialog open={isAddInvestmentOpen} onOpenChange={setIsAddInvestmentOpen} />
    </div>
  )
}
