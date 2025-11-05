"use client"

import { useState } from "react"
import { Plus } from "lucide-react"
import { PageHeader } from "@/components/dedicated/accounts/page-header"
import { ProfileItem } from "@/components/dedicated/profiles/profile-item"
import { AddProfileDialog } from "@/components/dedicated/profiles/add-profile-dialog"
import { EditProfileDialog } from "@/components/dedicated/profiles/edit-profile-dialog"
import { Button } from "@/components/ui/button"

export default function PerfisPage() {
  const [isAddProfileOpen, setIsAddProfileOpen] = useState(false)
  const [isEditProfileOpen, setIsEditProfileOpen] = useState(false)
  const [selectedProfile, setSelectedProfile] = useState("")

  const profiles = [
    { id: 1, name: "Perfil Pessoal" },
    { id: 2, name: "Perfil Trabalho" },
    { id: 3, name: "Perfil Família" },
  ]

  const handleAddProfile = () => {
    setIsAddProfileOpen(true)
  }

  const handleEditProfile = (profileName: string) => {
    setSelectedProfile(profileName)
    setIsEditProfileOpen(true)
  }

  return (
    <div className="min-h-screen bg-[var(--color-background)] text-[var(--color-foreground)] transition-colors">

      <main className="max-w-4xl mx-auto px-6 py-8">
        {/* Cabeçalho */}
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold text-[var(--color-foreground)]">Perfis</h1>
          <Button
            onClick={handleAddProfile}
            size="icon"
            className="
              rounded-full 
              bg-[var(--color-primary)] 
              hover:bg-[var(--color-primary-hover)] 
              text-[var(--color-on-primary)] 
              shadow-md 
              hover:shadow-lg 
              transition-all
            "
          >
            <Plus className="h-5 w-5" />
            <span className="sr-only">Criar novo perfil</span>
          </Button>
        </div>

        {/* Lista de Perfis */}
        <div className="space-y-3">
          {profiles.map((profile) => (
            <ProfileItem
              key={profile.id}
              name={profile.name}
              onEdit={() => handleEditProfile(profile.name)}
            />
          ))}
        </div>
      </main>

      {/* Dialogs */}
      <AddProfileDialog open={isAddProfileOpen} onOpenChange={setIsAddProfileOpen} />
      <EditProfileDialog open={isEditProfileOpen} onOpenChange={setIsEditProfileOpen} profileName={selectedProfile} />
    </div>
  )
}
