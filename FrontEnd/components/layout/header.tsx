"use client";

import type React from "react";
import { useEffect, useState } from "react";
import { ArrowBigLeft, ArrowLeft, Cog, Settings, Sun } from "lucide-react";
import Link from "next/link";
import { redirect, usePathname, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { authService } from "@/lib/service/auth/auth-service";
import { useTheme } from "next-themes";
import { getCurrencySymbol } from "@/lib/utils";
import { relatorioService } from "@/lib/service/relatorio/relatorio-service";


// Atualize a assinatura da função para incluir patrimony
export function Header() {
  const router = useRouter()
  const [balance, setBalance] = useState(0)
  const { theme, setTheme } = useTheme()
  const path = usePathname()

  const [patrimony, setPatrimony] = useState(0);

  const menuItems = [
    { label: "Contas e Cartões", route: "dashboard/contas-cartoes" },
    { label: "Meus Investimentos", route: "dashboard/investimentos" },
    { label: "Agendamentos", route: "dashboard/agendamentos" },
    { label: "Perfis", route: "dashboard/perfis" },
    { label: "Categorias", route: "dashboard/categorias" },
    { label: "Histórico", route: "dashboard/historico" },
  ];

  // Função para formatar moeda
  const formatCurrency = (value: number) => {
    return value.toLocaleString("pt-BR", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
  };

  useEffect(() => {
    const fetchPatrimonio = async () => {
      const data = await relatorioService.getPatrimonio()
      setPatrimony(data)
    }
    fetchPatrimonio()
  }, [])

  useEffect(() => {
    const fetchBalance = async () => {
      const data = await relatorioService.getDinheiroTotal()
      setBalance(data)
    }
    fetchBalance()
  }, [])

  return (
    <header
      style={{
        backgroundColor: "var(--background)",
        color: "var(--foreground)",
        borderBottom: "1px solid var(--border)",
      }}
      className="sticky top-0 z-10 px-6 py-4"
    >
      <div className="flex items-center justify-between max-w-7xl mx-auto">
        {/* Logo */}
        <Link href="/dashboard" className="cursor-pointer">
          <img
            src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/503032198-f0f23b0e-d540-498b-b4a3-4b33f89caf6e-eg4z1CkWeSwdB3BmyoNF2VoCEwHIde.png"
            alt="Fluxo Logo"
            className="h-12 w-12 hover:opacity-80 transition-opacity"
          />
        </Link>

        {/* Centro: saldo e patrimônio */}
        <div className="text-center flex flex-col items-center">
          <div>
            <p className="text-sm opacity-70 mb-1">Dinheiro na conta</p>

            {/* Saldo somente leitura */}
            <p
              className="text-4xl font-bold"
              style={{ color: "var(--foreground)" }}
            >
              {getCurrencySymbol()} {formatCurrency(balance)}
            </p>
          </div>

          {/* Patrimônio total */}
          <div className="mt-2">
            <p className="text-xs opacity-70">Patrimônio Total</p>
            <p className="text-lg font-semibold">
              {getCurrencySymbol()} {formatCurrency(patrimony)}
            </p>
          </div>
        </div>

        {/* Botões */}
        <div className="flex items-center gap-2">
          <Button
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            variant="ghost"
            className="rounded-full"
          >
            <Sun className="h-5 w-5" />
          </Button>

          <Sheet>
            <SheetTrigger asChild>
              <Button variant="ghost" size="icon" className="rounded-full ">
                <ArrowLeft className="h-7 w-7" />
              </Button>
            </SheetTrigger>
            <SheetContent
              style={{
                backgroundColor: "var(--sidebar)",
                borderColor: "var(--sidebar-border)",
                color: "var(--sidebar-foreground)",
              }}
            >
              <SheetHeader>
                <SheetTitle style={{ color: "var(--sidebar-foreground)" }}>
                  Menu
                </SheetTitle>
              </SheetHeader>
              <nav className="mt-8">
                <ul className="space-y-1">
                  {menuItems.map((item) => (
                    <li key={item.label}>
                      <button
                        onClick={() => {
                          const closeButton = document.querySelector(
                            '[data-slot="sheet-close"]'
                          ) as HTMLElement | null
                          closeButton?.click()

                          if (path.includes("dashboard/")) {
                            redirect(item.route.replace("dashboard/", ""))
                          } else {
                            router.push(item.route)
                          }
                        }}
                        style={{
                          color: "var(--sidebar-foreground)",
                        }}
                        className="w-full text-left px-4 py-3 rounded-lg transition-colors hover:opacity-80"
                      >
                        {item.label}
                      </button>
                    </li>
                  ))}
                  <li>
                    <button
                      className="w-full text-left px-4 py-3 rounded-lg transition-colors hover:opacity-80"
                      onClick={() => authService.logout()}
                      style={{ color: "var(--sidebar-foreground)" }}
                    >
                      Sair
                    </button>
                  </li>
                </ul>
              </nav>
            </SheetContent>
          </Sheet>
          
          <Button variant="ghost" size="icon" className="rounded-full" onClick={() => router.push('/dashboard/settings')}>
            <Cog className="h-7 w-7" />
          </Button>

        </div>
      </div>
    </header>
  );
}