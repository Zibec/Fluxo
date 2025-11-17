'use client'

import { useState } from 'react'
import { ChevronLeft } from 'lucide-react'
import { InformationsSection } from './informations-section'
import { PreferencesSection } from './preferences-section'

export function SettingsPage() {
  const [activeSection, setActiveSection] = useState<'informations' | 'preferences' | null>(null)

  if (activeSection === 'informations') {
    return (
      <div className="min-h-screen bg-secondary">
        <header className="border-b-4 border-primary bg-secondary px-6 py-4">
          <div className="flex items-center justify-between">
            <button
              onClick={() => setActiveSection(null)}
              className="flex items-center gap-2 text-foreground hover:opacity-80 transition-opacity"
            >
              <ChevronLeft className="w-6 h-6" />
              <span className="text-2xl font-semibold">Fluxo</span>
            </button>
            <button
              onClick={() => setActiveSection(null)}
              className="text-sm font-medium text-foreground hover:opacity-80 transition-opacity"
            >
              voltar
            </button>
          </div>
        </header>
        <main className="p-6">
          <InformationsSection onBack={() => setActiveSection(null)} />
        </main>
      </div>
    )
  }

  if (activeSection === 'preferences') {
    return (
      <div className="min-h-screen bg-secondary">
        <header className="border-b-4 border-primary bg-secondary px-6 py-4">
          <div className="flex items-center justify-between">
            <button
              onClick={() => setActiveSection(null)}
              className="flex items-center gap-2 text-foreground hover:opacity-80 transition-opacity"
            >
              <ChevronLeft className="w-6 h-6" />
              <span className="text-2xl font-semibold">Fluxo</span>
            </button>
            <button
              onClick={() => setActiveSection(null)}
              className="text-sm font-medium text-foreground hover:opacity-80 transition-opacity"
            >
              voltar
            </button>
          </div>
        </header>
        <main className="p-6">
          <PreferencesSection onBack={() => setActiveSection(null)} />
        </main>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-secondary">
      <header className="border-b-4 border-primary bg-secondary px-6 py-4">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-semibold text-foreground">Fluxo</h1>
          <button className="text-sm font-medium text-foreground hover:opacity-80 transition-opacity">
            voltar
          </button>
        </div>
      </header>

      <main className="flex items-center justify-center min-h-[calc(100vh-100px)] p-6">
        <div className="w-full max-w-2xl space-y-4">
          <button
            onClick={() => setActiveSection('informations')}
            className="w-full rounded-3xl bg-muted p-8 hover:opacity-80 transition-opacity text-left"
          >
            <h2 className="mb-6 text-center text-lg font-semibold text-foreground">Informações</h2>
            <div className="space-y-4 text-foreground">
              <div className="flex justify-between items-center">
                <span>Username: usuario123</span>
                <span className="text-sm">Alterar</span>
              </div>
              <div className="flex justify-between items-center">
                <span>Email: usuario@gmail.com</span>
                <span className="text-sm">Alterar</span>
              </div>
              <div className="flex justify-between items-center">
                <span>Nome Completo: Nome Completo</span>
                <span className="text-sm">Alterar</span>
              </div>
              <div className="flex justify-between items-center">
                <span>CPF: XXX.XXX.XXX-XX</span>
                <span className="text-sm">Alterar</span>
              </div>
              <div className="flex justify-between items-center">
                <span>Telefone: +55 (XX) XXXXX-XXXX</span>
                <span className="text-sm">Alterar</span>
              </div>
              <div className="flex justify-between items-center">
                <span>Data de Nascimento: XX/XX/XXXX</span>
                <span className="text-sm">Alterar</span>
              </div>
            </div>
          </button>

          <button
            onClick={() => setActiveSection('preferences')}
            className="w-full rounded-3xl bg-muted p-8 hover:opacity-80 transition-opacity text-left"
          >
            <h2 className="mb-6 text-center text-lg font-semibold text-foreground">Preferências</h2>
            <div className="space-y-4 text-foreground">
              <div className="flex justify-between items-center">
                <span>Moeda: BRL</span>
                <span className="text-sm">Alterar</span>
              </div>
              <div className="flex justify-between items-center">
                <span>Formato de Data: DD/MM/YYYY</span>
                <span className="text-sm">Alterar</span>
              </div>
            </div>
          </button>
        </div>
      </main>
    </div>
  )
}
