"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

interface EditCategoryDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  categoryName: string
  onSave: (newName: string) => void
}

export function EditCategoryDialog({ open, onOpenChange, categoryName, onSave }: EditCategoryDialogProps) {
  const [name, setName] = useState(categoryName)

  // Update the name when categoryName prop changes
  useEffect(() => {
    setName(categoryName)
  }, [categoryName])

  const handleSave = () => {
    if (name.trim()) {
      onSave(name.trim())
      onOpenChange(false)
    }
  }

  const handleCancel = () => {
    setName(categoryName) // Reset to original name
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold text-neutral-900">Editar Categoria</DialogTitle>
        </DialogHeader>

        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="category-name" className="text-sm font-medium text-neutral-700">
              Nome:
            </Label>
            <Input
              id="category-name"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  handleSave()
                }
              }}
              placeholder="Digite o nome da categoria"
              className="w-full"
            />
          </div>
        </div>

        <div className="flex justify-end gap-3">
          <Button
            variant="outline"
            onClick={handleCancel}
            className="border-neutral-300 text-neutral-700 hover:bg-neutral-50 bg-transparent"
          >
            Cancelar
          </Button>
          <Button onClick={handleSave} className="bg-blue-600 hover:bg-blue-700 text-white">
            Salvar
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}
