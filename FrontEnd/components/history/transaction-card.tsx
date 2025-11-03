"use client"

interface TransactionCardProps {
  type: "Débito" | "Crédito" | "Conta"
  value: number
  date: string
  responsible: string
  onClick?: () => void
}

export function TransactionCard({ type, value, date, responsible, onClick }: TransactionCardProps) {
  const getTypeStyles = () => {
    switch (type) {
      case "Débito":
        return "bg-red-100 text-red-700"
      case "Crédito":
        return "bg-green-100 text-green-700"
      case "Conta":
        return "bg-blue-100 text-blue-700"
      default:
        return "bg-neutral-100 text-neutral-700"
    }
  }

  return (
    <button
      onClick={onClick}
      className="w-full bg-white rounded-lg shadow-sm border border-neutral-200 p-4 hover:shadow-md transition-shadow text-left"
    >
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <div className="flex items-center gap-3 mb-2">
            <span className={`text-sm font-medium px-3 py-1 rounded-full ${getTypeStyles()}`}>{type}</span>
            <span className="text-lg font-semibold text-neutral-900">
              R$ {value.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </span>
          </div>
          <div className="flex items-center gap-4 text-sm text-neutral-600">
            <span>{date}</span>
            <span>•</span>
            <span>{responsible}</span>
          </div>
        </div>
      </div>
    </button>
  )
}
