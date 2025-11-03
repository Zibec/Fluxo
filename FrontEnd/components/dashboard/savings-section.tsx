import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import Link from "next/link"

interface Saving {
  name: string
  saved: number
  goal: number
}

interface SavingsSectionProps {
  savings: Saving[]
}

export function SavingsSection({ savings }: SavingsSectionProps) {
  return (
    <Card className="bg-white">
      <CardHeader>
        <Link href="/metas">
          <CardTitle className="text-xl font-semibold text-neutral-900 cursor-pointer hover:text-blue-600 transition-colors">
            Poupança
          </CardTitle>
        </Link>
      </CardHeader>
      <CardContent className="space-y-4">
        {savings.map((saving) => (
          <div key={saving.name} className="space-y-1">
            <p className="text-sm font-medium text-neutral-700">{saving.name}</p>
            <p className="text-sm text-neutral-600">
              R$ {saving.saved.toLocaleString("pt-BR")} / R$ {saving.goal.toLocaleString("pt-BR")}
            </p>
          </div>
        ))}
      </CardContent>
    </Card>
  )
}
