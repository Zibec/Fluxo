'use client'

import { useEffect, useState } from 'react'
import { useToast } from '@/hooks/use-toast'
import { InvestmentChart } from './investment-chart'
import { ResgateDialog } from './resgate-dialog'
import { createHistoricoInvestimentoData, createInvestimentoFormData } from '@/lib/service/investimentos/investimento-schema'
import { investimentoService } from '@/lib/service/investimentos/investimento-service'
import { redirect, useParams } from 'next/navigation'

export function InvestmentPage() {
  const { toast } = useToast()
  const { id } = useParams()

  const [selicRate, setSelicRate] = useState(0.0)
  const [investment, setInvestment] = useState<createInvestimentoFormData>()
  const [historicos, setHistoricos] = useState<createHistoricoInvestimentoData[]>([])
  
  useEffect(() => {
    async function fetchInvestment() {
      await investimentoService.getInvestimento(id as string).then((response) => {
        console.log("Investment data:", response.data)
        setInvestment(response.data)
      }).catch((error) => {
        console.error("Error fetching investment:", error)
      }
      )
    }
    fetchInvestment()
  }, [])

  useEffect(() => {
    async function fetchHistoricos() {
      await investimentoService.getHistoricoInvestimento(id as string).then((response) => {
        setHistoricos(response.data)
      }).catch((error) => {
        console.error("Error fetching historicos:", error)
      })
    }
    fetchHistoricos()
  }, [])

  useEffect(() => {
    async function fetchSelicRate() {
          await investimentoService.getTaxaSelic().then((response) => {
            setSelicRate(response.data.valor)
          }).catch((error) => {
            console.error("Error fetching Selic rate:", error)
          })
        }
    fetchSelicRate()
  }, [])


  const handleResgate = (type: 'total' | 'parcial', amount?: number) => {
    toast({
      title: 'Resgate Solicitado',
      description: `Resgate ${type === 'total' ? 'total' : `parcial de R$ ${amount?.toFixed(2)}`} foi solicitado com sucesso`,
    })

    if(type === 'parcial' && amount !== undefined) {
      investimentoService.resgateParcial(investment?.id as string, amount).then(() => {
        // Atualizar o estado do investimento após o resgate parcial
        setInvestment((prev) => prev ? { ...prev, valorAtual: prev.valorAtual - amount } : prev)
      }).catch((error) => {
        console.error("Error during partial resgate:", error)
        toast({
          title: 'Erro no Resgate',
          description: `Ocorreu um erro ao processar o resgate parcial.`,
          variant: 'destructive',
        })
      })
    } else if(type === 'total') {
      investimentoService.resgateTotal(investment?.id as string).then(() => {
        // Atualizar o estado do investimento após o resgate total
        redirect('/dedicated/investments')
      }).catch((error) => {
        console.error("Error during total resgate:", error)
        toast({
          title: 'Erro no Resgate',
          description: `Ocorreu um erro ao processar o resgate total.`,
          variant: 'destructive',
        })
      })
    }

    
  }


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
          {investment && <div
            key={investment.id}
            className="bg-background rounded-3xl p-8 border border-muted-foreground/10"
          >
            <div className="grid grid-cols-3 gap-8">
              {/* Left Column */}
              <div className="space-y-6">
                <div>
                  <h2 className="text-2xl font-bold text-foreground mb-4">
                    {investment.nome}
                  </h2>

                  {/* descricao */}
                  <div className="space-y-2">
                    <label className="block text-sm font-medium text-foreground">
                      Descrição
                    </label>
                      <div className="flex items-center justify-between">
                        <div className="flex-1 p-3 bg-background text-foreground rounded-lg min-h-[40px] flex items-center">
                          {investment.descricao}
                        </div>
                      </div>
                  </div>
                </div>

                {/* Investment Chart */}
                <div>
                  <label className="block text-sm font-medium text-foreground mb-4">
                    Dashboard de crescimento do investimento:
                  </label>
                  <div className="bg-background rounded-lg p-4">
                    <InvestmentChart data={historicos} />
                  </div>
                </div>
              </div>

              {/* Middle Column - Values */}
              <div className="space-y-6 flex flex-col justify-start">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Valor Atual:
                  </label>
                  <div className="text-3xl font-bold text-foreground">
                    R${investment.valorAtual.toLocaleString('pt-BR', {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Rendimento Atual:
                  </label>
                  <div className="text-2xl font-semibold text-green-600">
                    +{selicRate.toFixed(2)}%
                  </div>
                </div>
              </div>

              {/* Right Column - Resgate */}
              <div className="flex flex-col justify-between">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-4">
                    Resgatar valor investido:
                  </label>
                  <div className="space-y-2">
                    <ResgateDialog
                      investment={investment}
                      onResgate={handleResgate}
                      type="total"
                    />
                    <ResgateDialog
                      investment={investment}
                      onResgate={handleResgate}
                      type="parcial"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>}
      </main>
    </div>
  )
}
