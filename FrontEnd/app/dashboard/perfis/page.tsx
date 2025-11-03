"use client"

import { useState } from "react"
import { Plus } from "lucide-react"
import { PageHeader } from "@/components/accounts/page-header"
import { ProfileItem } from "@/components/profiles/profile-item"
import { AddProfileDialog } from "@/components/profiles/add-profile-dialog"
import { EditProfileDialog } from "@/components/profiles/edit-profile-dialog"
import { Button } from "@/components/ui/button"

export default function PerfisPage() {
  const [balance] = useState(5420.5)
  const [isAddProfileOpen, setIsAddProfileOpen] = useState(false)
  const [isEditProfileOpen, setIsEditProfileOpen] = useState(false)
  const [selectedProfile, setSelectedProfile] = useState("")

  // Sample profiles data
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
    <div className="min-h-screen bg-neutral-100">
      <PageHeader balance={balance} />

      <main className="max-w-4xl mx-auto px-6 py-8">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-3xl font-bold text-neutral-900">Perfis</h1>
          <Button
            onClick={handleAddProfile}
            size="icon"
            className="rounded-full bg-blue-600 hover:bg-blue-700 text-white shadow-lg"
          >
            <Plus className="h-5 w-5" />
            <span className="sr-only">Criar novo perfil</span>
          </Button>
        </div>

        <div className="space-y-3">
          {profiles.map((profile) => (
            <ProfileItem key={profile.id} name={profile.name} onEdit={() => handleEditProfile(profile.name)} />
          ))}
        </div>
      </main>

      <AddProfileDialog open={isAddProfileOpen} onOpenChange={setIsAddProfileOpen} />
      <EditProfileDialog open={isEditProfileOpen} onOpenChange={setIsEditProfileOpen} profileName={selectedProfile} />
    </div>
  )
}
