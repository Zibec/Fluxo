'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog'

interface ResgateDialogProps {
  investment: any
  onResgate: (type: 'total' | 'parcial', amount?: number) => void
  type: 'total' | 'parcial'
}

export function ResgateDialog({ investment, onResgate, type }: ResgateDialogProps) {
  const [open, setOpen] = useState(false)
  const [amount, setAmount] = useState('')

  const handleResgate = () => {
    if (type === 'total') {
      onResgate('total')
    } else {
      const valor = parseFloat(amount)
      if (valor > 0 && valor <= investment.valorAtual) {
        onResgate('parcial', valor)
        setAmount('')
      }
    }
    setOpen(false)
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="w-full bg-primary text-primary-foreground hover:bg-primary/90">
          {type === 'total' ? 'Resgate Total' : 'Resgate Parcial'}
        </Button>
      </DialogTrigger>
      <DialogContent className="bg-background border-muted-foreground/20">
        <DialogHeader>
          <DialogTitle className="text-foreground">
            {type === 'total' ? 'Resgate Total' : 'Resgate Parcial'}
          </DialogTitle>
        </DialogHeader>

        {type === 'parcial' && (
          <div className="space-y-4">
            <label className="block text-sm font-medium text-foreground">
              Valor a Resgatar (máximo: R${investment.valorAtual.toFixed(2)})
            </label>
            <Input
              type="number"
              placeholder="Digite o valor"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              max={investment.valorAtual}
              className="bg-secondary text-foreground border-muted-foreground/30"
            />
          </div>
        )}

        <Button
          onClick={handleResgate}
          className="w-full bg-primary text-primary-foreground hover:bg-primary/90 mt-4"
        >
          Confirmar Resgate
        </Button>
      </DialogContent>
    </Dialog>
  )
}
