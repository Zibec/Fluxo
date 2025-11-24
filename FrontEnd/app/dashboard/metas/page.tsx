"use client"

import { useEffect, useState } from "react"
import { GoalCard } from "@/components/dedicated/metas/goal-card"
import { EditGoalDialog } from "@/components/dedicated/metas/edit-goal-dialog"
import { metaService } from "@/lib/service/meta/meta-service"
import { Button } from "@/components/ui/button"
import { AddGoalDialog } from "@/components/dedicated/metas/add-goal-dialog"
import { createMetaFormData, MetaFormSchema } from "@/lib/service/meta/meta-schema"
import { useToast } from "@/hooks/use-toast"
import { AddValueGoalDialog } from "@/components/dedicated/metas/add-value-goal-dialog"

export default function MetasPage() {
  const [editDialogOpen, setEditDialogOpen] = useState(false)
  const [addDialogOpen, setAddDialogOpen] = useState(false)
  const [addValueDialogOpen, setAddValueDialogOpen] = useState(false)
  const [selectedGoal, setSelectedGoal] = useState<createMetaFormData | null>(null)
  const [goals, setGoals] = useState<createMetaFormData[]>()

  const { toast } = useToast()

  useEffect(() => {
    const fetchGoals = async () => {
      const fetchedGoals = await metaService.getAllMetas()
      setGoals(fetchedGoals)
    }
    fetchGoals()
  }, [])

  const handleEditGoal = (goalId: string) => {
    const goal = goals?.find((g) => g.id === goalId)
    if (goal) {
      setSelectedGoal(goal)
      setEditDialogOpen(true)
    }
  }

  const handleAddValueGoal = (goalId: string) => {
    const goal = goals?.find((g) => g.id === goalId)
    if (goal) {
      setSelectedGoal(goal)
      setAddValueDialogOpen(true)
    }
  }

  const handleDeleteGoal = async (goalId: string) => {
    await metaService.deleteMeta(goalId)

    setGoals((prev) => prev?.filter((g) => g.id !== goalId))

    toast({
      title: "Meta deletada",
      description: "A meta foi deletada com sucesso.",
    })
  }

  return (
    <div className="min-h-screen bg-background text-foreground">

      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="justify-between">
          <h1 className="text-3xl font-bold mb-6 text-foreground">Minhas Metas</h1>

          <Button
            className="mb-6 bg-primary text-primary-foreground hover:bg-primary/90"
            onClick={() => setAddDialogOpen(true)}
          >
            Adicionar Meta
          </Button>
        </div>

        <div className="space-y-4">
          {goals?.map((goal) => (
            <GoalCard
              key={goal.id}
              goal={goal}
              onEdit={() => handleEditGoal(goal.id)}
              onDelete={() => handleDeleteGoal(goal.id)}
              onAddValue={() => handleAddValueGoal(goal.id)}
            />
          ))}
        </div>
      </main>

      <EditGoalDialog
        open={editDialogOpen}
        onOpenChange={setEditDialogOpen}
        goal={selectedGoal}
        setMeta={setGoals}
      />

      <AddGoalDialog
        open={addDialogOpen}
        onOpenChange={setAddDialogOpen}
        setMeta={setGoals}
      />

      <AddValueGoalDialog
        open={addValueDialogOpen}
        onOpenChange={setAddValueDialogOpen}
        id={selectedGoal?.id || ""}
        setMeta={setGoals}
      />
    </div>
  )
}
