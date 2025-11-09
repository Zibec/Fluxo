"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { perfilService } from "@/lib/service/perfil/perfil-service"
import { useToast } from "@/hooks/use-toast"
import { createPerfilFormData } from "@/lib/service/perfil/perfil-schema"

interface AddProfileDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  setProfiles: React.Dispatch<React.SetStateAction<createPerfilFormData[] | undefined>>
}

export function AddProfileDialog({ open, onOpenChange, setProfiles }: AddProfileDialogProps) {
  const [name, setName] = useState("")

  const { toast } = useToast()

  const handleSaveProfile = () => {
    perfilService.createPerfil({ nome: name })

    toast({
      title: "Perfil criado",
      description: `O perfil "${name}" foi criado com sucesso.`
    })

    setName("")
    setProfiles(prev => prev ? [...prev, {nome: name, id: ""}] : [{nome: name, id: ""}])
    onOpenChange(false)
  }

  const handleCancelProfile = () => {
    setName("")
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[400px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Criar Novo Perfil</DialogTitle>
        </DialogHeader>
        <div className="py-4">
          <div className="space-y-2">
            <Label htmlFor="profile-name">Nome:</Label>
            <Input
              id="profile-name"
              type="text"
              placeholder="Digite o nome do perfil"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
        </div>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancelProfile}>
            Cancelar
          </Button>
          <Button className="bg-blue-600 hover:bg-blue-700" onClick={handleSaveProfile}>
            Salvar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
