"use client"

import { useState } from "react"
import { Plus } from "lucide-react"
import { PageHeader } from "@/components/accounts/page-header"
import { InvestmentCard } from "@/components/investments/investment-card"
import { AddInvestmentDialog } from "@/components/investments/add-investment-dialog"
import { Button } from "@/components/ui/button"

export default function InvestimentosPage() {
  const [balance] = useState(12500.0)
  const [selicRate] = useState(13.75)
  const [isAddInvestmentOpen, setIsAddInvestmentOpen] = useState(false)
  const [investments, setInvestments] = useState([
    { id: 1, name: "Investimento A", currentValue: 5000.0 },
    { id: 2, name: "Tesouro Direto", currentValue: 8500.0 },
    { id: 3, name: "CDB Banco XYZ", currentValue: 3200.0 },
  ])

  const handleDeleteInvestment = (id: number) => {
    setInvestments(investments.filter((inv) => inv.id !== id))
  }

  const handleAddInvestment = () => {
    setIsAddInvestmentOpen(true)
  }

  return (
    <div className="min-h-screen bg-neutral-100">
      <PageHeader balance={balance} />

      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Title and Add Button */}
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold text-neutral-900">Meus Investimentos</h1>
          <Button
            size="icon"
            className="rounded-full bg-blue-600 hover:bg-blue-700 text-white shadow-lg"
            onClick={handleAddInvestment}
          >
            <Plus className="h-5 w-5" />
            <span className="sr-only">Adicionar Investimento</span>
          </Button>
        </div>

        {/* Selic Rate Card */}
        <div className="bg-blue-50 border border-blue-200 rounded-lg px-4 py-3 mb-6 inline-block">
          <p className="text-sm font-medium text-blue-900">
            Taxa Selic: <span className="font-bold">{selicRate.toFixed(2)}%</span>
          </p>
        </div>

        {/* Investments List */}
        <div className="space-y-4">
          {investments.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-neutral-500">Nenhum investimento cadastrado ainda.</p>
            </div>
          ) : (
            investments.map((investment) => (
              <InvestmentCard
                key={investment.id}
                name={investment.name}
                currentValue={investment.currentValue}
                onDelete={() => handleDeleteInvestment(investment.id)}
              />
            ))
          )}
        </div>
      </main>

      <AddInvestmentDialog open={isAddInvestmentOpen} onOpenChange={setIsAddInvestmentOpen} />
    </div>
  )
}
