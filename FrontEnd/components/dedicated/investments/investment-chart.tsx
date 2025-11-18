'use client'

import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts'

interface InvestmentChartProps {
  data: { mes: string; valor: number }[]
}

export function InvestmentChart({ data }: InvestmentChartProps) {
  return (
    <ResponsiveContainer width="100%" height={200}>
      <LineChart data={data}>
        <CartesianGrid strokeDasharray="3 3" stroke="oklch(0.922 0 0)" />
        <XAxis dataKey="mes" stroke="oklch(0.556 0 0)" />
        <YAxis stroke="oklch(0.556 0 0)" />
        <Tooltip
          contentStyle={{
            backgroundColor: 'oklch(1 0 0)',
            border: '1px solid oklch(0.922 0 0)',
            borderRadius: '8px',
          }}
        />
        <Line
          type="monotone"
          dataKey="valor"
          stroke="oklch(0.205 0 0)"
          strokeWidth={2}
          dot={{ fill: 'oklch(0.205 0 0)', r: 4 }}
        />
      </LineChart>
    </ResponsiveContainer>
  )
}
