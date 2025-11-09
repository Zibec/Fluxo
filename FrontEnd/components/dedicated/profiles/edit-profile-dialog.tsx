"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { perfilService } from "@/lib/service/perfil/perfil-service"
import { createPerfilFormData } from "@/lib/service/perfil/perfil-schema"
import { useToast } from "@/components/ui/use-toast"

interface EditProfileDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  profile: createPerfilFormData
  setProfiles: React.Dispatch<React.SetStateAction<createPerfilFormData[] | undefined>>
}

export function EditProfileDialog({ open, onOpenChange, profile, setProfiles }: EditProfileDialogProps) {
  const [name, setName] = useState(profile?.nome || "")

  const { toast } = useToast()

  useEffect(() => {
    setName(profile?.nome)
  }, [profile?.nome])

  const handleSaveProfile = () => {
    perfilService.updatePerfil(profile.id, { id: profile.id, nome: name })

    toast({
      title: "Perfil atualizado",
      description: `O perfil "${name}" foi atualizado com sucesso.`
    })

    setProfiles((prevProfiles) =>
      prevProfiles?.map((p) =>
        p.id === profile.id ? { ...p, nome: name } : p
      )
    )
    onOpenChange(false)
  }

  const handleCancelProfile = () => {
    setName(profile.nome)
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[400px]">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold">Editar Perfil</DialogTitle>
        </DialogHeader>
        <div className="py-4">
          <div className="space-y-2">
            <Label htmlFor="edit-profile-name">Nome:</Label>
            <Input
              id="edit-profile-name"
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
