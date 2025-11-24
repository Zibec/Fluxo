"use client"

import { useEffect, useState } from "react"
import { PageHeader } from "@/components/dedicated/accounts/page-header"
import { CategoryItem } from "@/components/dedicated/categories/category-item"
import { EditCategoryDialog } from "@/components/dedicated/categories/edit-category-dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { CategoriaFormSchema, createCategoriaFormData } from "@/lib/service/categoria/categoria-schemas"
import { categoriasService } from "@/lib/service/categoria/categoria-service"
import { useToast } from "@/hooks/use-toast"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"

export default function CategoriasPage() {
  const [categories, setCategories] = useState<createCategoriaFormData[]>([])
  const [editDialogOpen, setEditDialogOpen] = useState(false)
  const [editingCategory, setEditingCategory] = useState<{ id: string; name: string }>({ id: "", name: "" })

  const {toast} = useToast();

  const  {
        register,
        handleSubmit,
        watch,
        getValues,
        formState: { errors }
    } = useForm<createCategoriaFormData>({
        resolver: zodResolver(CategoriaFormSchema)
      })

  
  async function fetchCategories() {
    const categorias = await categoriasService.getAllCategorias();
    setCategories(categorias);
  }
  
  fetchCategories();

  const handleAddCategory = async () => {
    if (getValues("nome").trim() === "") return;

    try {
      await categoriasService.createCategoria({ nome: getValues("nome") });
      
      toast({
        title: "Categoria adicionada",
        description: "A nova categoria foi adicionada com sucesso.",
      });

      setCategories(await categoriasService.getAllCategorias());
    } catch (error) {
      console.error("Error adding category:", error);
    }
  }

  const handleDeleteCategory = async (id: string) => {
    try {
      await categoriasService.deleteCategoria(id);

      toast({
        title: "Categoria deletada",
        description: "A categoria foi deletada com sucesso.",
      });

      setCategories(await categoriasService.getAllCategorias());
    } catch (error) {
      console.error("Error deleting category:", error);
    }
  }

  const handleSaveEditedCategory = async (id: string, newName: string) => {
    try {
      await categoriasService.updateCategoria(id, { id: id, nome: newName });
      toast({
        title: "Categoria atualizada",
        description: "A categoria foi atualizada com sucesso.",
      });
      setCategories(await categoriasService.getAllCategorias());
    } catch (error) {
      console.error("Error updating category:", error);
    }
  }

  return (
    <div className="min-h-screen bg-[var(--color-background)] text-[var(--color-foreground)]">
      <main className="max-w-4xl mx-auto px-6 py-8">
        <h1 className="text-3xl font-bold mb-6 text-[var(--color-foreground)]">Categorias</h1>

        {/* Lista de categorias */}
        <div className="space-y-3 mb-8">
          {categories && categories.map((category) => (
            <CategoryItem
              key={category.id}
              name={category.nome}
              onEdit={() => {
                setEditingCategory({ id: category.id!, name: category.nome });
                setEditDialogOpen(true);
              }}
              onDelete={() => handleDeleteCategory(category.id!)}
            />
          ))}
        </div>

        
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
            <form onSubmit={handleSubmit(handleAddCategory)} className="flex items-end gap-4 w-full">
            <div className="flex-1">
              <Label
                htmlFor="new-category"
                className="text-sm font-medium text-[var(--color-muted-foreground)] mb-2 block"
              >
                Nova categoria:
              </Label>
              <Input
                {...register("nome")}
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
              type="submit"
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
            </form>
          </div>
        </div>
      </main>

      <EditCategoryDialog
        open={editDialogOpen}
        onOpenChange={setEditDialogOpen}
        category={editingCategory}
        onSave={handleSaveEditedCategory}
      />
    </div>
  )
}
