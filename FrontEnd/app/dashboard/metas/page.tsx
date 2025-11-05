"use client"

import { useState } from "react"
import { BudgetsPageHeader } from "@/components/dedicated/budgets/budgets-page-header"
import { GoalCard } from "@/components/dedicated/goals/goal-card"
import { EditGoalDialog } from "@/components/dedicated/goals/edit-goal-dialog"

interface Goal {
  id: number
  title: string
  value: number
  description: string
  deadline?: Date
}

export default function MetasPage() {
  const [balance] = useState(5420.5)
  const [editDialogOpen, setEditDialogOpen] = useState(false)
  const [selectedGoal, setSelectedGoal] = useState<Goal | null>(null)

  const goals: Goal[] = [
    {
      id: 1,
      title: "Meta de dívida",
      value: 200,
      description: "A dívida que eu tenho com Tio Jonas de 200 reais que preciso pagar até o final do mês",
      deadline: new Date(2025, 10, 30),
    },
    {
      id: 2,
      title: "Meta de poupança",
      value: 5000,
      description: "Economizar para a viagem de férias no final do ano. Preciso juntar esse valor nos próximos 6 meses",
      deadline: new Date(2025, 11, 31),
    },
    {
      id: 3,
      title: "Fundo de emergência",
      value: 10000,
      description: "Criar um fundo de emergência para imprevistos e segurança financeira da família",
      deadline: new Date(2026, 5, 30),
    },
  ]

  const handleEditGoal = (goalId: number) => {
    const goal = goals.find((g) => g.id === goalId)
    if (goal) {
      setSelectedGoal(goal)
      setEditDialogOpen(true)
    }
  }

  const handleDeleteGoal = (goalId: number) => {
    console.log("[v0] Delete goal:", goalId)
    // TODO: Implement delete functionality
  }

  return (
    <div className="min-h-screen bg-neutral-100">

      <main className="max-w-7xl mx-auto px-6 py-8">
        <h1 className="text-3xl font-bold text-neutral-900 mb-6">Minhas Metas</h1>

        <div className="space-y-4">
          {goals.map((goal) => (
            <GoalCard
              key={goal.id}
              title={goal.title}
              value={goal.value}
              description={goal.description}
              onEdit={() => handleEditGoal(goal.id)}
              onDelete={() => handleDeleteGoal(goal.id)}
            />
          ))}
        </div>
      </main>

      <EditGoalDialog open={editDialogOpen} onOpenChange={setEditDialogOpen} goal={selectedGoal} />
    </div>
  )
}
