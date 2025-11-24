"use client";

import { useEffect, useState } from "react"
import { BudgetSection } from "@/components/dedicated/dashboard/budget-section"
import { DebtSection } from "@/components/dedicated/dashboard/debt-section"
import { SavingsSection } from "@/components/dedicated/dashboard/savings-section"
import { FabButton } from "@/components/dedicated/dashboard/fab-button"
import { AddExpenseDialog } from "@/components/dedicated/dashboard/add-expense-dialog"
import { AddIncomeDialog } from "@/components/dedicated/dashboard/add-income-dialog"
import { metaService } from "@/lib/service/meta/meta-service"
import { orcamentoService } from "@/lib/service/orcamento/orcamento-service"
import { createOrcamentoFormData } from "@/lib/service/orcamento/orcamento-schema"
import { AddGoalDialog } from "../dedicated/metas/add-goal-dialog";
import { AddBudgetDialog } from "../dedicated/orcamento/add-budget-dialog";
import { createMetaFormData } from "@/lib/service/meta/meta-schema";
import { createDividaFormData } from "@/lib/service/dividas/divida-schema";
import { dividaService } from "@/lib/service/dividas/divida-service";

const DashboardPage = () => {
  const [isExpenseDialogOpen, setIsExpenseDialogOpen] = useState(false)
  const [isBudgetDialogOpen, setIsBudgetDialogOpen] = useState(false)
  const [isGoalDialogOpen, setIsGoalDialogOpen] = useState(false)

  const [budgets, setBudgets] = useState<createOrcamentoFormData[]>([])

  const [debts, setDebts] = useState<createDividaFormData[]>([])

  const [savings, setSavings] = useState<createMetaFormData[]>([])


  useEffect(() => {
    const fetchSavings = async () => {
      setSavings(await metaService.getAllMetas())
          console.log(budgets)

    }
    fetchSavings()        
  }, [])

  useEffect(() => {
    const fetchDebts = async () => {
      setDebts(await dividaService.getAllDividas())
    }
    fetchDebts()
  }, [])

  useEffect(() => {
    const fetchBudgets = async () => {
      setBudgets(await orcamentoService.getOrcamentos())
    }
    fetchBudgets()
  }, [])

  const handleFabAction = (action: string) => {
    if (action === "Adicionar Despesa") setIsExpenseDialogOpen(true);
    if (action === "Criar Novo Orçamento") setIsBudgetDialogOpen(true);
    if (action === "Criar Nova Meta") setIsGoalDialogOpen(true);
  };

  return (
    <div
      className="min-h-screen transition-colors"
      style={{
        backgroundColor: "var(--background)",
        color: "var(--foreground)",
      }}
    >
      <main
        className="max-w-7xl mx-auto p-6"
        style={{
          backgroundColor: "var(--card)",
          borderRadius: "var(--radius-lg)",
        }}
      >
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <BudgetSection budgets={budgets} />
          <DebtSection debts={debts} />
          <SavingsSection savings={savings} />
        </div>
      </main>

      {/* Botão Flutuante */}
      <FabButton onActionSelect={handleFabAction} />

      {/* Dialogs */}
      <AddExpenseDialog
        open={isExpenseDialogOpen}
        onOpenChange={setIsExpenseDialogOpen}
      />
      <AddBudgetDialog
        open={isBudgetDialogOpen}
        onOpenChange={setIsBudgetDialogOpen}
        setBudgets={setBudgets}
      />
      <AddGoalDialog
        open={isGoalDialogOpen}
        onOpenChange={setIsGoalDialogOpen}
        setMeta={setSavings}
      />
    </div>
  );
};

export default DashboardPage;
