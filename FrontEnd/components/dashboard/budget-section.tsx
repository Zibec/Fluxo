import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import Link from "next/link"

interface Budget {
  name: string
  spent: number
  total: number
}

interface BudgetSectionProps {
  budgets: Budget[]
}

export function BudgetSection({ budgets }: BudgetSectionProps) {
  return (
    <Card className="bg-white">
      <CardHeader>
        <Link href="/orcamentos">
          <CardTitle className="text-xl font-semibold text-neutral-900 hover:text-blue-600 transition-colors cursor-pointer">
            Orçamentos
          </CardTitle>
        </Link>
      </CardHeader>
      <CardContent className="space-y-4">
        {budgets.map((budget) => {
          const percentage = (budget.spent / budget.total) * 100
          return (
            <div key={budget.name} className="space-y-2">
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium text-neutral-700">{budget.name}</span>
                <span className="text-sm text-neutral-600">
                  R$ {budget.spent} / R$ {budget.total}
                </span>
              </div>
              <Progress value={percentage} className="h-2" />
            </div>
          )
        })}
      </CardContent>
    </Card>
  )
}
