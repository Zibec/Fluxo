"use client"

import { useEffect, useState } from "react"
import { ScheduleCard } from "@/components/dedicated/schedules/schedule-card"
import { AddScheduleDialog } from "@/components/dedicated/schedules/add-schedule-dialog"
import { EditScheduleDialog } from "@/components/dedicated/schedules/edit-schedule-dialog"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"
import { createAgendamentoFormData } from "@/lib/service/agendamentos/agendamento-schema"
import { agendamentoService } from "@/lib/service/agendamentos/agendamento-service"
import { useToast } from "@/hooks/use-toast"

export default function AgendamentosPage() {
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false)

  const [schedules, setSchedules] = useState<createAgendamentoFormData[]>([])
  const [selectedSchedule, setSelectedSchedule] = useState<createAgendamentoFormData>()

  const {toast} = useToast()

  useEffect(() => {
    const fetchSchedules = async () => {
      agendamentoService.getAllAgendamentos().then((data) => {
        setSchedules(data)
      })
    }
    fetchSchedules()
  }, []) 

  const handleEdit = (schedule: createAgendamentoFormData) => {
    setSelectedSchedule(schedule)
    setIsEditDialogOpen(true)
  }

  const handleDelete = (id: string) => {
    agendamentoService.deleteAgendamento(id).then(() => {
      setSchedules(schedules.filter((schedule) => schedule.id !== id))
      toast({
        title: "Agendamento deletado",
        description: "O agendamento foi deletado com sucesso.",
      })
    }).catch(() => {
      toast({
        title: "Erro ao deletar agendamento",
        description: "Ocorreu um erro ao deletar o agendamento. Tente novamente."
      })
    })
  }

  const handleAddSchedule = () => {

    setIsAddDialogOpen(true)
  }

  return (
    <div
      className="min-h-screen transition-colors"
      style={{
        backgroundColor: "var(--background)",
        color: "var(--foreground)",
      }}
    >
      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="flex items-center justify-between mb-6">
          <h1
            className="text-3xl font-bold"
            style={{
              color: "var(--foreground)",
            }}
          >
            Agendamentos
          </h1>

          <Button
            onClick={handleAddSchedule}
            size="icon"
            className="rounded-full transition-colors"
            style={{
              backgroundColor: "var(--primary)",
              color: "var(--primary-foreground)",
            }}
          >
            <Plus className="h-5 w-5" />
            <span className="sr-only">Agendar nova transação</span>
          </Button>
        </div>

        <div className="space-y-4">
          {schedules.map((schedule) => (
            <ScheduleCard
              key={schedule.id}
              title={schedule.descricao}
              value={schedule.valor}
              nextDate={schedule.proximaData.toString()}
              onEdit={() => handleEdit(schedule)}
              onDelete={() => handleDelete(schedule.id)}
            />
          ))}
        </div>

        {schedules.length === 0 && (
          <div className="text-center py-12">
            <p
              className="text-lg"
              style={{ color: "var(--muted-foreground)" }}
            >
              Nenhum agendamento encontrado
            </p>
            <p
              className="text-sm mt-2"
              style={{ color: "var(--muted-foreground)" }}
            >
              Clique no botão + para criar um novo agendamento
            </p>
          </div>
        )}
      </main>

      <AddScheduleDialog
        open={isAddDialogOpen}
        onOpenChange={setIsAddDialogOpen}
        setSchedules={setSchedules}
      />

      <EditScheduleDialog
        open={isEditDialogOpen}
        onOpenChange={setIsEditDialogOpen}
        setSchedules={setSchedules}
        schedule={selectedSchedule}
      />
    </div>
  )
}
