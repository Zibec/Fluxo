import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import Link from "next/link"

interface Debt {
  name: string
  paid: number
  total: number
}

interface DebtSectionProps {
  debts: Debt[]
}

export function DebtSection({ debts }: DebtSectionProps) {
  return (
    <Card className="bg-white">
      <CardHeader>
        <Link href="/metas">
          <CardTitle className="text-xl font-semibold text-neutral-900 cursor-pointer hover:text-blue-600 transition-colors">
            Dívidas
          </CardTitle>
        </Link>
      </CardHeader>
      <CardContent className="space-y-4">
        {debts.map((debt) => (
          <div key={debt.name} className="space-y-1">
            <p className="text-sm font-medium text-neutral-700">{debt.name}</p>
            <p className="text-sm text-neutral-600">
              Pago R$ {debt.paid} de R$ {debt.total}
            </p>
          </div>
        ))}
      </CardContent>
    </Card>
  )
}
