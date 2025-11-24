import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function getCurrencySymbol(): string {
  const fluxoUser = localStorage.getItem("fluxo_user")
  var currencyCode = "BRL"

  if (fluxoUser) {
    const user = JSON.parse(fluxoUser)
    currencyCode = user.moedaPreferida || "BRL"
  }

  const symbols: Record<string, string> = {
    USD: '$',
    EUR: '€',
    BRL: 'R$'
  }

  return symbols[currencyCode]
}

export function formatDateByUserPreference(date: Date | string): string {
  const fluxoUser = localStorage.getItem("fluxo_user")
  let dateFormat = "dd-MM-yyyy"

  if (fluxoUser) {
    const user = JSON.parse(fluxoUser)
    dateFormat = user.dataPreferida || "dd-MM-yyyy"
  }

  const data = dateFormat.replaceAll("-", "/")

  return format(date, data, { locale: ptBR } )
}

export function formatCardNumber(num) {

  if(typeof num !== "string") {
    return null
  }

  
  return num
    .replace(/\D/g, "")        // remove tudo que não é número
    .replace(/(.{4})/g, "$1 ") // adiciona espaço a cada 4 dígitos
    .trim();
}