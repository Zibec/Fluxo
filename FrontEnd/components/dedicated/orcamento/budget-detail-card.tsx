"use client"

import { Card, CardContent } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { Button } from "@/components/ui/button"
import { ChevronsDown, ChevronsUp, ChevronsUpDown, Pencil, Trash2 } from "lucide-react"
import { createOrcamentoFormData } from "@/lib/service/orcamento/orcamento-schema"
import { useEffect, useState } from "react"
import { orcamentoService } from "@/lib/service/orcamento/orcamento-service"
import { categoriasService } from "@/lib/service/categoria/categoria-service"
import { getCurrencySymbol } from "@/lib/utils"
import { createTransacaoFormData } from "@/lib/service/transacao/transacao-schema"
import { transacaoService } from "@/lib/service/transacao/transacao-service"
import { TransactionCard } from "../historico/transaction-card"
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible"

interface BudgetDetailCardProps {
  budget: createOrcamentoFormData
  onEdit: () => void
}

export function BudgetDetailCard({ budget, onEdit }: BudgetDetailCardProps) {
  const [spent, setSpent] = useState<number>(0)
  const [categoriaNome, setCategoriaNome] = useState<string>("")
  const [transacoes, setTransacoes] = useState<createTransacaoFormData[]>()
  const [isOpen, setIsOpen] = useState<boolean>(false)

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

  useEffect(() => {
    const fetchTransacoes = async() => {
      const transacoes: createTransacaoFormData[] = await transacaoService.getTransacoesByUser()
      setTransacoes(transacoes.filter(t => t.categoriaId === budget.categoriaId))
    }
    fetchTransacoes()
  }, [])


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
            <div className="flex items-center text-sm gap-2">
              <p className="text-muted-foreground">Data Limite:</p>
              <p className="font-medium">
                {`${budget.ano?.toString().substring(2)}/${budget.mes < 10 ? "0" + budget.mes?.toString() : budget.mes?.toString()}`}
              </p>
            </div>
          </div>
        </div>

        <Collapsible className="space-y-2">
          <CollapsibleTrigger className="flex flex-row" onClick={() => setIsOpen(!isOpen)}>
              {isOpen ? <><ChevronsUp />Transações</> : <><ChevronsDown />Transações</>}
          </CollapsibleTrigger>
          <CollapsibleContent className="space-y-2">
            {transacoes && transacoes.map((transaction) => (
              <TransactionCard
                key={transaction.id}
                transaction={transaction}
                onClick={() => (true)}
              />
            ))}
          </CollapsibleContent>
        </Collapsible>

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
