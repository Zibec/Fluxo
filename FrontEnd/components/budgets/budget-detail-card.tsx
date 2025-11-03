"use client"

import { Card, CardContent } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { Button } from "@/components/ui/button"
import { Pencil, Trash2 } from "lucide-react"

interface Transaction {
  description: string
  value: number
  date: string
}

interface BudgetDetailCardProps {
  title: string
  spent: number
  total: number
  transactions: Transaction[]
  onEdit: () => void
  onDelete: () => void
}

export function BudgetDetailCard({ title, spent, total, transactions, onEdit, onDelete }: BudgetDetailCardProps) {
  const percentage = (spent / total) * 100

  return (
    <Card className="bg-white rounded-lg shadow-sm">
      <CardContent className="p-6 space-y-4">
        {/* Title and Progress */}
        <div className="space-y-3">
          <h3 className="text-lg font-semibold text-neutral-900">{title}</h3>
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span className="text-neutral-600">Progresso</span>
              <span className="font-medium text-neutral-900">
                R$ {spent.toFixed(2)} / R$ {total.toFixed(2)}
              </span>
            </div>
            <Progress value={percentage} className="h-2" />
          </div>
        </div>

        {/* Transactions List */}
        <div className="space-y-2">
          <h4 className="text-sm font-medium text-neutral-700">Transações</h4>
          <div className="space-y-2">
            {transactions.map((transaction, index) => (
              <div
                key={index}
                className="flex items-center justify-between text-sm py-2 border-b border-neutral-100 last:border-0"
              >
                <span className="text-neutral-700">{transaction.description}</span>
                <div className="flex items-center gap-3">
                  <span className="font-medium text-neutral-900">R$ {transaction.value.toFixed(2)}</span>
                  <span className="text-neutral-500 text-xs">dia {transaction.date}</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center gap-3 pt-2">
          <Button
            variant="outline"
            className="flex-1 text-red-600 border-red-200 hover:bg-red-50 bg-transparent"
            onClick={onDelete}
          >
            <Trash2 className="h-4 w-4 mr-2" />
            Excluir
          </Button>
          <Button
            variant="outline"
            className="flex-1 text-blue-600 border-blue-200 hover:bg-blue-50 bg-transparent"
            onClick={onEdit}
          >
            <Pencil className="h-4 w-4 mr-2" />
            Editar
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
