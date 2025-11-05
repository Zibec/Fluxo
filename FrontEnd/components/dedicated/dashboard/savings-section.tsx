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
    <Card
      className="transition-colors"
      style={{
        backgroundColor: "var(--card)",
        color: "var(--card-foreground)",
        borderColor: "var(--border)",
      }}
    >
      <CardHeader>
        <Link href="/dashboard/metas">
          <CardTitle
            className="text-xl font-semibold cursor-pointer transition-colors"
            style={{
              color: "var(--foreground)",
            }}
          >
            Poupança
          </CardTitle>
        </Link>
      </CardHeader>

      <CardContent className="space-y-4">
        {savings.map((saving) => (
          <div key={saving.name} className="space-y-1">
            <p
              className="text-sm font-medium"
              style={{ color: "var(--muted-foreground)" }}
            >
              {saving.name}
            </p>
            <p
              className="text-sm"
              style={{ color: "var(--muted-foreground)" }}
            >
              R$ {saving.saved.toLocaleString("pt-BR")} / R$ {saving.goal.toLocaleString("pt-BR")}
            </p>
          </div>
        ))}
      </CardContent>
    </Card>
  )
}
