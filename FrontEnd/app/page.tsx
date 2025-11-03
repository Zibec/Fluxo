"use client"

import { useState } from "react"
import { ArrowRight, TrendingUp, PieChart, Zap } from "lucide-react"
import { Button } from "@/components/ui/button"
import { AuthDialog } from "@/components/auth/auth-dialog"
import Image from "next/image"

export default function Home() {
  const [isAuthDialogOpen, setIsAuthDialogOpen] = useState(false)

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-50 via-white to-slate-50">
      {/* Header */}
      <header className="sticky top-0 z-50 backdrop-blur-sm bg-white/80 border-b border-slate-200">
        <nav className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Image
              src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Imagem%20do%20WhatsApp%20de%202025-10-19%20%C3%A0%28s%29%2018.19.15_be016fe0-15btNodOVv3HJqmwsiVLandRznsM67.jpg"
              alt="Fluxo Logo"
              width={40}
              height={40}
              className="w-10 h-10"
            />
            <span className="text-xl font-bold text-slate-900">Fluxo</span>
          </div>
          <div className="hidden md:flex items-center gap-8">
            <Button
              variant="outline"
              className="border-slate-300 text-slate-900 bg-transparent"
              onClick={() => setIsAuthDialogOpen(true)}
            >
              Entrar
            </Button>
            <Button className="bg-blue-600 hover:bg-blue-700 text-white" onClick={() => setIsAuthDialogOpen(true)}>
              Começar
            </Button>
          </div>
          <Button className="md:hidden bg-blue-600 hover:bg-blue-700 text-white">Menu</Button>
        </nav>
      </header>
      <div>
        <AuthDialog isOpen={isAuthDialogOpen} onClose={() => setIsAuthDialogOpen(false)} />
      {/* Hero Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24">
        <div className="grid md:grid-cols-2 gap-12 items-center">
          <div className="space-y-8">
            <div className="space-y-4">
              <div className="inline-block bg-blue-50 px-4 py-2 rounded-full border border-blue-200">
                <span className="text-sm font-medium text-blue-700">💡 Novo padrão de gestão financeira</span>
              </div>
              <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-slate-900 text-balance leading-tight">
                Sua vida financeira em <span className="text-blue-600">fluxo</span>
              </h1>
              <p className="text-xl text-slate-600 text-balance leading-relaxed">
                Controle seu dinheiro com inteligência. Fluxo é a plataforma que torna a gestão financeira simples,
                segura e intuitiva.
              </p>
            </div>
            <div className="flex flex-col sm:flex-row gap-4">
              <Button
                size="lg"
                className="bg-blue-600 hover:bg-blue-700 text-white text-base"
                onClick={() => setIsAuthDialogOpen(true)}
              >
                Começar Grátis <ArrowRight className="w-5 h-5 ml-2" />
              </Button>
 
            </div>
            <p className="text-sm text-slate-500">
              ✓ Sem cartão de crédito necessário • ✓ Acesso imediato • ✓ 30 dias grátis
            </p>
          </div>
          <div className="relative h-96 md:h-full min-h-96 flex items-center justify-center">
            <div className="absolute inset-0 bg-gradient-to-br from-blue-400/20 to-blue-600/20 rounded-2xl blur-3xl"></div>
            <Image
              src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Imagem%20do%20WhatsApp%20de%202025-10-19%20%C3%A0%28s%29%2018.19.15_be016fe0-15btNodOVv3HJqmwsiVLandRznsM67.jpg"
              alt="Fluxo - Gestão Financeira"
              width={300}
              height={300}
              className="w-64 h-64 md:w-80 md:h-80 relative z-10 drop-shadow-2xl"
            />
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="recursos" className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24">
        <div className="text-center space-y-4 mb-12 md:mb-16">
          <h2 className="text-3xl md:text-4xl font-bold text-slate-900">Tudo que você precisa para prosperar</h2>
          <p className="text-lg text-slate-600 max-w-2xl mx-auto">
            Ferramentas poderosas e simples para controlar cada aspecto de suas finanças
          </p>
        </div>
        <div className="grid md:grid-cols-3 gap-8">
          {[
            {
              icon: <TrendingUp className="w-8 h-8" />,
              title: "Análise Inteligente",
              description:
                "Entenda seus gastos com relatórios detalhados e insights automáticos que ajudam você a economizar mais.",
            },
            {
              icon: <PieChart className="w-8 h-8" />,
              title: "Orçamento Automático",
              description:
                "Defina metas de gastos e receba alertas em tempo real quando você estiver próximo do limite.",
            },
            {
              icon: <Zap className="w-8 h-8" />,
              title: "Sincronização Rápida",
              description: "Conecte suas contas bancárias e veja tudo em um único lugar, atualizado a cada segundo.",
            },
          ].map((feature, i) => (
            <div
              key={i}
              className="bg-white border border-slate-200 rounded-xl p-8 hover:border-blue-300 hover:shadow-lg transition-all group"
            >
              <div className="w-12 h-12 bg-blue-50 rounded-lg flex items-center justify-center text-blue-600 mb-6 group-hover:bg-blue-100 transition">
                {feature.icon}
              </div>
              <h3 className="text-xl font-semibold text-slate-900 mb-3">{feature.title}</h3>
              <p className="text-slate-600 leading-relaxed">{feature.description}</p>
            </div>
          ))}
        </div>
      </section>

      {/* CTA Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24">
        <div className="bg-gradient-to-r from-blue-50 to-blue-100 rounded-2xl border border-blue-200 p-12 md:p-16 text-center space-y-8">
          <div className="space-y-4">
            <h2 className="text-3xl md:text-4xl font-bold text-slate-900">
              Pronto para tomar controle das suas finanças?
            </h2>
            <p className="text-lg text-slate-600 max-w-2xl mx-auto">
              Junte-se a milhares de brasileiros que já estão economizando mais com Fluxo.
            </p>
          </div>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button
              size="lg"
              className="bg-blue-600 hover:bg-blue-700 text-white text-base"
              onClick={() => setIsAuthDialogOpen(true)}
            >
              Criar Conta Grátis <ArrowRight className="w-5 h-5 ml-2" />
            </Button>
          </div>
          <p className="text-sm text-slate-600">Todos os planos incluem 30 dias grátis de acesso ilimitado</p>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-slate-200 bg-slate-50 py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-4 gap-8 mb-8">
            <div>
              <div className="flex items-center gap-2 mb-4">
                <Image
                  src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Imagem%20do%20WhatsApp%20de%202025-10-19%20%C3%A0%28s%29%2018.19.15_be016fe0-15btNodOVv3HJqmwsiVLandRznsM67.jpg"
                  alt="Fluxo Logo"
                  width={32}
                  height={32}
                  className="w-8 h-8"
                />
                <span className="font-bold text-slate-900">Fluxo</span>
              </div>
              <p className="text-sm text-slate-600">Sua vida financeira em fluxo</p>
            </div>
            <div>
              <h4 className="font-semibold text-slate-900 mb-4">Produto</h4>
              <ul className="space-y-2 text-sm text-slate-600">
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Recursos
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Preços
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Segurança
                  </a>
                </li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold text-slate-900 mb-4">Empresa</h4>
              <ul className="space-y-2 text-sm text-slate-600">
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Sobre
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Blog
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Contato
                  </a>
                </li>
              </ul>
            </div>
            <div>
              <h4 className="font-semibold text-slate-900 mb-4">Legal</h4>
              <ul className="space-y-2 text-sm text-slate-600">
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Privacidade
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Termos
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-slate-900 transition">
                    Cookies
                  </a>
                </li>
              </ul>
            </div>
          </div>
          <div className="border-t border-slate-200 pt-8 flex flex-col md:flex-row justify-between items-center text-sm text-slate-600">
            <p>&copy; 2025 Fluxo. Todos os direitos reservados.</p>
            <div className="flex gap-6 mt-4 md:mt-0">
              <a href="#" className="hover:text-slate-900 transition">
                LinkedIn
              </a>
              <a href="#" className="hover:text-slate-900 transition">
                Twitter
              </a>
              <a href="#" className="hover:text-slate-900 transition">
                Instagram
              </a>
            </div>
          </div>
        </div>
      </footer>
      </div>
    </div>
  )
}
