"use client"

import { useState } from "react"
import { PageHeader } from "@/components/accounts/page-header"
import { ScheduleCard } from "@/components/schedules/schedule-card"
import { AddScheduleDialog } from "@/components/schedules/add-schedule-dialog"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"

export default function AgendamentosPage() {
  const [balance] = useState(12500.0)
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)

  // Sample scheduled transactions data
  const [schedules, setSchedules] = useState([
    {
      id: 1,
      title: "Aluguel",
      value: 1500.0,
      nextDate: "05/11/2025",
    },
    {
      id: 2,
      title: "Netflix",
      value: 55.9,
      nextDate: "10/11/2025",
    },
    {
      id: 3,
      title: "Conta de Luz",
      value: 180.0,
      nextDate: "15/11/2025",
    },
  ])

  const handleEdit = (id: number) => {
    console.log("[v0] Edit schedule:", id)
    // TODO: Implement edit functionality
  }

  const handleDelete = (id: number) => {
    console.log("[v0] Delete schedule:", id)
    setSchedules(schedules.filter((schedule) => schedule.id !== id))
  }

  const handleAddSchedule = () => {
    console.log("[v0] Add new schedule")
    setIsAddDialogOpen(true)
  }

  return (
    <div className="min-h-screen bg-neutral-100">
      <PageHeader balance={balance} />

      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold text-neutral-900">Agendamentos</h1>
          <Button onClick={handleAddSchedule} size="icon" className="rounded-full bg-blue-600 hover:bg-blue-700">
            <Plus className="h-5 w-5" />
            <span className="sr-only">Agendar nova transação</span>
          </Button>
        </div>

        <div className="space-y-4">
          {schedules.map((schedule) => (
            <ScheduleCard
              key={schedule.id}
              title={schedule.title}
              value={schedule.value}
              nextDate={schedule.nextDate}
              onEdit={() => handleEdit(schedule.id)}
              onDelete={() => handleDelete(schedule.id)}
            />
          ))}
        </div>

        {schedules.length === 0 && (
          <div className="text-center py-12">
            <p className="text-neutral-500 text-lg">Nenhum agendamento encontrado</p>
            <p className="text-neutral-400 text-sm mt-2">Clique no botão + para criar um novo agendamento</p>
          </div>
        )}
      </main>

      <AddScheduleDialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen} />
    </div>
  )
}
