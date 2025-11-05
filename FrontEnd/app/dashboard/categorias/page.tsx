"use client"

import { useState } from "react"
import { PageHeader } from "@/components/dedicated/accounts/page-header"
import { CategoryItem } from "@/components/dedicated/categories/category-item"
import { EditCategoryDialog } from "@/components/dedicated/categories/edit-category-dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

export default function CategoriasPage() {
  const [newCategory, setNewCategory] = useState("")
  const [categories, setCategories] = useState([
    { id: 1, name: "Carro" },
    { id: 2, name: "Aluguel" },
    { id: 3, name: "Casa" },
    { id: 4, name: "Alimentação" },
    { id: 5, name: "Transporte" },
  ])
  const [editDialogOpen, setEditDialogOpen] = useState(false)
  const [editingCategory, setEditingCategory] = useState<{ id: number; name: string } | null>(null)

  const handleAddCategory = () => {
    if (newCategory.trim()) {
      const newId = Math.max(...categories.map((c) => c.id), 0) + 1
      setCategories([...categories, { id: newId, name: newCategory.trim() }])
      setNewCategory("")
    }
  }

  const handleEditCategory = (id: number) => {
    const category = categories.find((c) => c.id === id)
    if (category) {
      setEditingCategory(category)
      setEditDialogOpen(true)
    }
  }

  const handleSaveEditedCategory = (newName: string) => {
    if (editingCategory) {
      setCategories(categories.map((c) => (c.id === editingCategory.id ? { ...c, name: newName } : c)))
    }
  }

  const handleDeleteCategory = (id: number) => {
    setCategories(categories.filter((c) => c.id !== id))
  }

  return (
    <div className="min-h-screen bg-[var(--color-background)] text-[var(--color-foreground)]">
      <main className="max-w-4xl mx-auto px-6 py-8">
        <h1 className="text-3xl font-bold mb-6 text-[var(--color-foreground)]">Categorias</h1>

        {/* Lista de categorias */}
        <div className="space-y-3 mb-8">
          {categories.map((category) => (
            <CategoryItem
              key={category.id}
              name={category.name}
              onEdit={() => handleEditCategory(category.id)}
              onDelete={() => handleDeleteCategory(category.id)}
            />
          ))}
        </div>

        {/* Adicionar nova categoria */}
        <div
          className="
            bg-[var(--color-card)] 
            border 
            border-[var(--color-border)] 
            rounded-lg 
            shadow-sm 
            p-6
          "
        >
          <div className="flex items-end gap-4">
            <div className="flex-1">
              <Label
                htmlFor="new-category"
                className="text-sm font-medium text-[var(--color-muted-foreground)] mb-2 block"
              >
                Nova categoria:
              </Label>
              <Input
                id="new-category"
                type="text"
                value={newCategory}
                onChange={(e) => setNewCategory(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    handleAddCategory()
                  }
                }}
                placeholder="Digite o nome da categoria"
                className="
                  w-full 
                  bg-[var(--color-input)] 
                  border-[var(--color-border)] 
                  text-[var(--color-foreground)] 
                  placeholder:text-[var(--color-muted-foreground)]
                "
              />
            </div>
            <Button
              onClick={handleAddCategory}
              className="
                bg-[var(--color-primary)] 
                hover:bg-[var(--color-primary)]/90 
                text-[var(--color-primary-foreground)] 
                font-medium 
                transition-colors
              "
            >
              Salvar
            </Button>
          </div>
        </div>
      </main>

      <EditCategoryDialog
        open={editDialogOpen}
        onOpenChange={setEditDialogOpen}
        categoryName={editingCategory?.name || ""}
        onSave={handleSaveEditedCategory}
      />
    </div>
  )
}
