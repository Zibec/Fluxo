"use client"

import { ArrowLeft } from "lucide-react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { useRouter } from "next/navigation"

interface PageHeaderProps {
  balance: number
}

export function PageHeader({ balance }: PageHeaderProps) {
  const router = useRouter()

  return (
    <header className="sticky top-0 z-10 bg-white border-b border-neutral-200 px-6 py-4">
      <div className="flex items-center justify-between max-w-7xl mx-auto">
        <Link href="/" className="cursor-pointer">
          <img
            src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/503032198-f0f23b0e-d540-498b-b4a3-4b33f89caf6e-eg4z1CkWeSwdB3BmyoNF2VoCEwHIde.png"
            alt="Fluxo Logo"
            className="h-12 w-12 hover:opacity-80 transition-opacity"
          />
        </Link>

        <div className="text-center">
          <p className="text-sm text-neutral-500 mb-1">Dinheiro na conta</p>
          <p className="text-4xl font-bold text-neutral-900">
            R$ {balance.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </p>
        </div>

        <Button variant="ghost" size="icon" className="rounded-full" onClick={() => router.push("/")}>
          <ArrowLeft className="h-5 w-5" />
          <span className="sr-only">Voltar</span>
        </Button>
      </div>
    </header>
  )
}
