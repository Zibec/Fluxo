'use client'

import { useEffect, useState } from 'react'
import { useToast } from '@/hooks/use-toast'
import { createHistoricoInvestimentoData, createInvestimentoFormData } from '@/lib/service/investimentos/investimento-schema'
import { investimentoService } from '@/lib/service/investimentos/investimento-service'
import { redirect, useParams } from 'next/navigation'
import { createCartaoFormData } from '@/lib/service/contas-cartoes/contas-cartoes-schemas'
import { cartoesService, Fatura } from '@/lib/service/contas-cartoes/contas-cartoes-service'
import { formatCardNumber, formatDateByUserPreference, getCurrencySymbol } from '@/lib/utils'
import { createTransacaoFormData } from '@/lib/service/transacao/transacao-schema'
import { transacaoService } from '@/lib/service/transacao/transacao-service'
import { TransactionCard } from '@/components/dedicated/history/transaction-card'
import { Button } from '@/components/ui/button'

export default function Page() {
  const { toast } = useToast()
  const { id } = useParams()

  const [cartao, setCartao] = useState<createCartaoFormData>()
  const [fatura, setFatura] = useState<Fatura>()
  const [transacoes, setTransacoes] = useState<createTransacaoFormData[]>()

  const fetchInfo = async () => {
    const cartao = await cartoesService.getCartaoById(id)
    setCartao(cartao)

    const fatura: Fatura = await cartoesService.getFaturaByCartaoId(id)
    setFatura(fatura)

    const transacoes: createTransacaoFormData[] = await transacaoService.getTransacoesByUser()
    console.log(transacoes, fatura.transacoes)
    setTransacoes(transacoes.filter(t => fatura.transacoes.find((f) => f == t.id)))
  }
  fetchInfo()
 

  return (
    <div
      className="
        min-h-screen 
        bg-[var(--color-background)] 
        text-[var(--color-foreground)] 
        transition-colors
      "
    >
      <main className="max-w-7xl mx-auto px-6 py-8">
          {cartao && <div
            key={cartao.id}
            className="bg-background rounded-3xl p-8 border border-muted-foreground/10"
          >
            <div className="grid grid-cols-2 gap-8">
              {/* Left Column */}
              <div className="space-y-6">
                <div>
                  <h2 className="text-2xl font-bold text-foreground mb-4">
                    {formatCardNumber(cartao.numero)}
                  </h2>

                  {/* descricao */}
                  <div className="space-y-2 flex flex-row gap-2">
                    <label className="block text-sm font-medium text-foreground">
                      TITULAR:
                    </label>
                    <div className="bg-background text-sm rounded-lg">
                      {cartao.titular}
                    </div>
                  </div>
                  <div className='space-y-2 flex flex-row gap-2'>
                    <label className="block text-sm font-medium text-foreground">
                      CVV:
                    </label>
                    <div className="bg-background text-sm rounded-lg">
                      {cartao.cvv}
                    </div>
                  </div>
                  <div className='space-y-2 flex flex-row gap-2'>
                    <label className="block text-sm font-medium text-foreground">
                      VALIDADE:
                    </label>
                    <div className="bg-background text-sm rounded-lg">
                      {cartao.validade.substring(2).replaceAll("-", "/")}
                    </div>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Saldo:
                  </label>
                  <div className="text-3xl font-bold text-foreground">
                    {getCurrencySymbol()} {cartao.saldo.toLocaleString('pt-BR', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Limite:
                  </label>
                  <div className="text-3xl font-bold text-foreground">
                    {getCurrencySymbol()} {cartao.limite.toLocaleString('pt-BR', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </div>
                </div>
              </div>

              {fatura ? <div className="flex flex-col justify-between">
                <div>
                  <h2 className="block font-medium text-foreground mb-4">
                    Fatura
                  </h2>
                  <div className='space-y-2 flex flex-row gap-2'>
                    <label className="block text-sm font-medium text-foreground">
                      STATUS:
                    </label>
                    <div className="bg-background text-sm rounded-lg">
                      {fatura?.status}
                    </div>
                  </div>
                  <div className='space-y-2 flex flex-row gap-2'>
                    <label className="block text-sm font-medium text-foreground">
                      DATA DE FECHAMENTO:
                    </label>
                    <div className="bg-background text-sm rounded-lg">
                      {formatDateByUserPreference(cartao.dataFechamentoFatura)}
                    </div>
                  </div>
                  <div className='space-y-2 flex flex-row gap-2'>
                    <label className="block text-sm font-medium text-foreground">
                      DATA DE VENCIMENTO:
                    </label>
                    <div className="bg-background text-sm rounded-lg">
                      {formatDateByUserPreference(fatura.dataVencimento)}
                    </div>
                  </div>
                  <div className='space-y-2 mb-2 flex flex-row gap-2'>
                    <Button onClick={() => {
                      cartoesService.payFaturaByCartaoId(id)
                      window.location.reload()
                    }}>
                      PAGAR
                    </Button>
                  </div>
                  <div className="space-y-3">
                    {transacoes && transacoes.map((transaction) => (
                      <TransactionCard
                        key={transaction.id}
                        transaction={transaction}
                        onClick={() => (true)}
                      />
                    ))}
                  </div>
                </div>
              </div> :
              <div>
                <p>Fatura vazia</p>
              </div>}
            </div>
          </div>}
      </main>
    </div>
  )
}
