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
    <Card
      className="transition-colors"
      style={{
        backgroundColor: "var(--card)",
        color: "var(--card-foreground)",
        borderColor: "var(--border)",
      }}
    >
      <CardHeader>
        <Link href="/dashboard/orcamentos">
          <CardTitle
            className="text-xl font-semibold cursor-pointer transition-colors"
            style={{
              color: "var(--foreground)",
            }}
          >
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
                <span
                  className="text-sm font-medium"
                  style={{ color: "var(--muted-foreground)" }}
                >
                  {budget.name}
                </span>
                <span
                  className="text-sm"
                  style={{ color: "var(--muted-foreground)" }}
                >
                  R$ {budget.spent} / R$ {budget.total}
                </span>
              </div>

              <Progress
                value={percentage}
                className="h-2"
                style={{
                  backgroundColor: "var(--muted)",
                }}
              />
            </div>
          )
        })}
      </CardContent>
    </Card>
  )
}
