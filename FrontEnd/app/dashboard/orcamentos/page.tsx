"use client"

import { useState } from "react"
import { BudgetsPageHeader } from "@/components/dedicated/budgets/budgets-page-header"
import { BudgetDetailCard } from "@/components/dedicated/budgets/budget-detail-card"
import { EditBudgetDialog } from "@/components/dedicated/budgets/edit-budget-dialog"

export default function OrcamentosPage() {
  const [balance] = useState(5420.5)
  const [editDialogOpen, setEditDialogOpen] = useState(false)
  const [selectedBudget, setSelectedBudget] = useState<{
    id: number
    title: string
    total: number
    category: string
    profile: string
  } | null>(null)

  // Sample budget data with transactions
  const budgets = [
    {
      id: 1,
      title: "Comida",
      spent: 90,
      total: 100,
      category: "Comida",
      profile: "Pessoal",
      transactions: [
        { description: "Mc Donalds", value: 50.0, date: "17/10/2025" },
        { description: "Supermercado", value: 40.0, date: "15/10/2025" },
      ],
    },
    {
      id: 2,
      title: "Transporte",
      spent: 150,
      total: 200,
      category: "Transporte",
      profile: "Pessoal",
      transactions: [
        { description: "Uber", value: 45.0, date: "18/10/2025" },
        { description: "Gasolina", value: 105.0, date: "16/10/2025" },
      ],
    },
    {
      id: 3,
      title: "Lazer",
      spent: 80,
      total: 150,
      category: "Lazer",
      profile: "Pessoal",
      transactions: [
        { description: "Cinema", value: 50.0, date: "19/10/2025" },
        { description: "Restaurante", value: 30.0, date: "14/10/2025" },
      ],
    },
  ]

  const handleEdit = (budgetId: number) => {
    const budget = budgets.find((b) => b.id === budgetId)
    if (budget) {
      setSelectedBudget({
        id: budget.id,
        title: budget.title,
        total: budget.total,
        category: budget.category,
        profile: budget.profile,
      })
      setEditDialogOpen(true)
    }
  }

  const handleDelete = (budgetId: number) => {
    console.log("[v0] Delete budget:", budgetId)
    // TODO: Implement delete functionality
  }

  return (
    <div className="min-h-screen bg-neutral-100">


      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="space-y-6">
          {budgets.map((budget) => (
            <BudgetDetailCard
              key={budget.id}
              title={budget.title}
              spent={budget.spent}
              total={budget.total}
              transactions={budget.transactions}
              onEdit={() => handleEdit(budget.id)}
              onDelete={() => handleDelete(budget.id)}
            />
          ))}
        </div>
      </main>

      <EditBudgetDialog open={editDialogOpen} onOpenChange={setEditDialogOpen} budget={selectedBudget} />
    </div>
  )
}
