import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'

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

  return symbols[currencyCode] || currencyCode
}
