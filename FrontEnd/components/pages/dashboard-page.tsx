"use client";

import { useState } from "react";
import { BudgetSection } from "@/components/dedicated/dashboard/budget-section";
import { DebtSection } from "@/components/dedicated/dashboard/debt-section";
import { SavingsSection } from "@/components/dedicated/dashboard/savings-section";
import { FabButton } from "@/components/dedicated/dashboard/fab-button";
import { AddExpenseDialog } from "@/components/dedicated/dashboard/add-expense-dialog";
import { AddIncomeDialog } from "@/components/dedicated/dashboard/add-income-dialog";
import { AddBudgetDialog } from "@/components/dedicated/dashboard/add-budget-dialog";
import { AddGoalDialog } from "@/components/dedicated/dashboard/add-goal-dialog";

const DashboardPage = () => {
  const [isExpenseDialogOpen, setIsExpenseDialogOpen] = useState(false);
  const [isIncomeDialogOpen, setIsIncomeDialogOpen] = useState(false);
  const [isBudgetDialogOpen, setIsBudgetDialogOpen] = useState(false);
  const [isGoalDialogOpen, setIsGoalDialogOpen] = useState(false);

  const budgets = [
    { name: "Comida", spent: 90, total: 100 },
    { name: "Transporte", spent: 45, total: 80 },
    { name: "Lazer", spent: 120, total: 150 },
  ];

  const debts = [
    { name: "Casas Bahia", paid: 150, total: 1700 },
    { name: "Cartão de Crédito", paid: 800, total: 2500 },
    { name: "Empréstimo Pessoal", paid: 3000, total: 10000 },
  ];

  const savings = [
    { name: "Carro", saved: 90, goal: 50000 },
    { name: "Viagem", saved: 2500, goal: 8000 },
    { name: "Emergência", saved: 5000, goal: 15000 },
  ];

  const handleFabAction = (action: string) => {
    if (action === "Adicionar Despesa") setIsExpenseDialogOpen(true);
    if (action === "Adicionar Receita") setIsIncomeDialogOpen(true);
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
      <AddIncomeDialog
        open={isIncomeDialogOpen}
        onOpenChange={setIsIncomeDialogOpen}
      />
      <AddBudgetDialog
        open={isBudgetDialogOpen}
        onOpenChange={setIsBudgetDialogOpen}
      />
      <AddGoalDialog
        open={isGoalDialogOpen}
        onOpenChange={setIsGoalDialogOpen}
      />
    </div>
  );
};

export default DashboardPage;
