"use client"

import { use, useEffect, useState } from "react"
import { PageHeader } from "@/components/dedicated/accounts/page-header"
import { AccountCard } from "@/components/dedicated/accounts/account-card"
import { CardCard } from "@/components/dedicated/accounts/card-card"
import { AddAccountDialog } from "@/components/dedicated/accounts/add-account-dialog"
import { AddCardDialog } from "@/components/dedicated/accounts/add-card-dialog"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"
import { cartoesService, contasService } from "@/lib/service/contas-cartoes/contas-cartoes-service"
import { createCartaoFormData, createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { useToast } from "@/hooks/use-toast"

export default function ContasCartoesPage() {
  const [addAccountOpen, setAddAccountOpen] = useState(false)
  const [addCardOpen, setAddCardOpen] = useState(false)

  const [accounts, setAccounts] = useState<createContaFormData[]>([])
  const [cards, setCards] = useState<createCartaoFormData[]>([])

  useEffect(() => {
    async function fetchAccounts() {
      const contas = await contasService.getAllContas()
      setAccounts(contas)
    }

    fetchAccounts()
  }, [])

  useEffect(() => {
    async function fetchCards() {
      const cartoes = await cartoesService.getAllCartoes()
      setCards(cartoes)
    }

    fetchCards()
  }, [])

  return (
    <div
      className="
        min-h-screen
        bg-[var(--color-background)]
        text-[var(--color-foreground)]
        transition-colors
      "
    >
      <main className="max-w-4xl mx-auto px-6 py-8 space-y-10">
        {/* Contas */}
        <section>
          <div className="flex justify-between">
            <h2 className="text-lg font-bold text-[var(--color-foreground)] mb-4">CONTAS</h2>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setAddAccountOpen(true)}
              className="
                text-[var(--color-primary)]
                hover:text-[var(--color-primary-foreground)]
                hover:bg-[var(--color-primary)]/10
                transition-colors
              "
            >
              <Plus className="h-4 w-4 mr-1" />
              Adicionar
            </Button>
          </div>
          {accounts.length > 0 ? (
            <div className="space-y-3">
              {accounts.map((account) => (
                <AccountCard
                  id={account.id!}
                  key={account.id}
                  account={account}
                  setAccounts={setAccounts}
                />
              ))}
            </div>
          ) : (
            <p className="text-sm text-[var(--color-muted-foreground)]">
              Nenhuma conta adicionada ainda.
            </p>
          )}
        </section>

        <hr className="border-[var(--color-border)]" />

        {/* Cartões */}
        <section>
          <div className="flex justify-between">
            <h2 className="text-lg font-bold text-[var(--color-foreground)] mb-4">CARTÕES</h2>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setAddCardOpen(true)}
              className="
                text-[var(--color-primary)]
                hover:text-[var(--color-primary-foreground)]
                hover:bg-[var(--color-primary)]/10
                transition-colors
              "
            >
              <Plus className="h-4 w-4 mr-1" />
              Adicionar
            </Button>
          </div>
          {cards.length > 0 ? (
            <div className="space-y-3">
              {cards.map((card) => (
                <CardCard
                  id={card.id!}
                  key={card.id}
                  card={card}
                  setCards={setCards}
                />
              ))}
            </div>
          ) : (
            <p className="text-sm text-[var(--color-muted-foreground)]">
              Nenhum cartão adicionado ainda.
            </p>
          )}
        </section>
      </main>

      <AddAccountDialog open={addAccountOpen} onOpenChange={setAddAccountOpen} setAccounts={setAccounts} />
      <AddCardDialog open={addCardOpen} onOpenChange={setAddCardOpen} setCards={setCards} />
    </div>
  )
}
