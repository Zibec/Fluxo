"use client"

import { useState } from "react"
import { PageHeader } from "@/components/accounts/page-header"
import { AccountCard } from "@/components/accounts/account-card"
import { CardCard } from "@/components/accounts/card-card"
import { AddAccountDialog } from "@/components/accounts/add-account-dialog"
import { AddCardDialog } from "@/components/accounts/add-card-dialog"

export default function ContasCartoesPage() {
  const balance = 5420.5
  const [addAccountOpen, setAddAccountOpen] = useState(false)
  const [addCardOpen, setAddCardOpen] = useState(false)

  const handleAddAccount = () => {
    setAddAccountOpen(true)
  }

  const handleAddCard = () => {
    setAddCardOpen(true)
  }

  return (
    <div className="min-h-screen bg-neutral-100">
      <PageHeader balance={balance} />

      <main className="max-w-4xl mx-auto px-6 py-8">
        {/* Contas Section */}
        <section className="mb-8">
          <h2 className="text-lg font-bold text-neutral-900 mb-4">CONTAS</h2>
          <div className="space-y-3">
            <AccountCard title="Conta Poupança" subtitle="Nome Completo" onAdd={handleAddAccount} />
          </div>
        </section>

        {/* Cartões Section */}
        <section>
          <h2 className="text-lg font-bold text-neutral-900 mb-4">CARTÕES</h2>
          <div className="space-y-3">
            <CardCard title="Cartão Mastercard Credito" cardNumber="XXXX XXXX XXXX XXXX" onAdd={handleAddCard} />
          </div>
        </section>
      </main>

      <AddAccountDialog open={addAccountOpen} onOpenChange={setAddAccountOpen} />
      <AddCardDialog open={addCardOpen} onOpenChange={setAddCardOpen} />
    </div>
  )
}
