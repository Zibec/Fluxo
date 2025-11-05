"use client"

interface TransactionCardProps {
  type: "Débito" | "Crédito" | "Conta"
  value: number
  date: string
  responsible: string
  onClick?: () => void
}

export function TransactionCard({ type, value, date, responsible, onClick }: TransactionCardProps) {
  // Cores adaptadas ao tema global, sem fixar tons neutros
  const getTypeStyles = () => {
    switch (type) {
      case "Débito":
        return "bg-destructive/10 text-destructive-foreground"
      case "Crédito":
        return "bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-300"
      case "Conta":
        return "bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300"
      default:
        return "bg-muted text-muted-foreground"
    }
  }

  return (
    <button
      onClick={onClick}
      className="w-full bg-card text-card-foreground rounded-lg border border-border p-4 shadow-sm hover:shadow-md hover:bg-accent/40 transition-all text-left"
    >
      <div className="flex items-center justify-between">
        <div className="flex-1">
          {/* Tipo + Valor */}
          <div className="flex items-center gap-3 mb-2">
            <span
              className={`text-xs font-medium px-3 py-1 rounded-full ${getTypeStyles()}`}
            >
              {type}
            </span>
            <span className="text-lg font-semibold text-foreground">
              R$ {value.toLocaleString("pt-BR", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
              })}
            </span>
          </div>

          {/* Data + Responsável */}
          <div className="flex items-center gap-4 text-sm text-muted-foreground">
            <span>{date}</span>
            <span>•</span>
            <span>{responsible}</span>
          </div>
        </div>
      </div>
    </button>
  )
}
