"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"

interface EditProfileDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  profileName: string
}

export function EditProfileDialog({ open, onOpenChange, profileName }: EditProfileDialogProps) {
  const [name, setName] = useState(profileName)

  useEffect(() => {
    setName(profileName)
  }, [profileName])

  const handleSaveProfile = () => {
    console.log("[v0] Updating profile:", name)
    onOpenChange(false)
  }

  const handleCancelProfile = () => {
    setName(profileName)
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
