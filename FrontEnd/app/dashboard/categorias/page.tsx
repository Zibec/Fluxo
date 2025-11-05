"use client"

import { useEffect, useState } from "react"
import { PageHeader } from "@/components/dedicated/accounts/page-header"
import { CategoryItem } from "@/components/dedicated/categories/category-item"
import { EditCategoryDialog } from "@/components/dedicated/categories/edit-category-dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { createCategoriaFormData } from "@/lib/service/categoria/categoria-schemas"

export default function CategoriasPage() {
  const [newCategory, setNewCategory] = useState("")
  const [categories, setCategories] = useState<createCategoriaFormData>([])
  const [editDialogOpen, setEditDialogOpen] = useState(false)
  const [editingCategory, setEditingCategory] = useState<{ id: number; name: string } | null>(null)

  useEffect(() => {
    async function fetchCategories() {
      const categorias = 
    }
  })

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
