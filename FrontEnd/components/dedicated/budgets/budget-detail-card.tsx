"use client"

import { Card, CardContent } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { Button } from "@/components/ui/button"
import { Pencil, Trash2 } from "lucide-react"
import { createOrcamentoFormData } from "@/lib/service/orcamento/orcamento-schema"
import { useEffect, useState } from "react"
import { orcamentoService } from "@/lib/service/orcamento/orcamento-service"
import { categoriasService } from "@/lib/service/categoria/categoria-service"
import { getCurrencySymbol } from "@/lib/utils"

interface BudgetDetailCardProps {
  budget: createOrcamentoFormData
  onEdit: () => void
}

export function BudgetDetailCard({ budget, onEdit }: BudgetDetailCardProps) {
  const [spent, setSpent] = useState<number>(0)
  const [categoriaNome, setCategoriaNome] = useState<string>("")

  useEffect(() => {
    const fetchSpent = async () => {
      await orcamentoService.getOrcamentoTotalSpent(budget.categoriaId, `${budget.ano}-${budget.mes.toString().padStart(2, '0')}`).then((data) => {
        setSpent(data)
      })
    }
    fetchSpent()
  }, [budget.categoriaId, budget.anoMes])

  useEffect(() => {
    const fetchCategoriaNome = async () => {
      await categoriasService.getCategoriaById(budget.categoriaId).then((data) => {
        setCategoriaNome(data.nome)
      })
    }
    fetchCategoriaNome()
  }, [budget.categoriaId])


  const percentage = (spent / budget.limite) * 100

  return (
    <Card className="bg-card text-card-foreground rounded-lg shadow-sm">
      <CardContent className="p-6 space-y-4">
        <div className="space-y-3">
          <h3 className="text-lg font-semibold">{categoriaNome}</h3>
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span className="text-muted-foreground">Progresso</span>
              <span className="font-medium">
                {getCurrencySymbol()} {spent.toFixed(2)} / {getCurrencySymbol()} {budget.limite.toFixed(2)}
              </span>
            </div>
            <Progress value={percentage} className="h-2" />
          </div>
        </div>

        <div className="space-y-2">
          <h4 className="text-sm font-medium text-muted-foreground">Transações</h4>
          <div className="space-y-2">
             {/* transactions.map((transaction, index) => (
              <div
                key={index}
                className="flex items-center justify-between text-sm py-2 border-b border-border last:border-0"
              >
                <span>{transaction.description}</span>
                <div className="flex items-center gap-3">
                  <span className="font-medium">R$ {transaction.value.toFixed(2)}</span>
                  <span className="text-xs text-muted-foreground">dia {transaction.date}</span>
                </div>
              </div>
            ))*/} 
          </div>
        </div>

        <div className="flex items-center gap-3 pt-2">
          <Button
            variant="outline"
            className="flex-1 text-primary border-primary hover:bg-primary/10 bg-transparent"
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
