import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { createMetaFormData } from "@/lib/service/meta/meta-schema"
import Link from "next/link"

interface SavingsSectionProps {
  savings: createMetaFormData[]
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
          <div key={saving.id} className="space-y-1">
            <p
              className="text-sm font-medium"
              style={{ color: "var(--muted-foreground)" }}
            >
              {saving.descricao}
            </p>
            <p
              className="text-sm"
              style={{ color: "var(--muted-foreground)" }}
            >
              R$ {saving.saldoAcumulado || 0} / R$ {saving.valorAlvo}
            </p>
          </div>
        ))}
      </CardContent>
    </Card>
  )
}
