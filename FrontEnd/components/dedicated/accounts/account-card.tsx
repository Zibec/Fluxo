"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { createContaFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { contasService } from "@/lib/service/contas-cartoes/contas-cartoes-service"
import { id } from "date-fns/locale"
import { PencilIcon, Plus, TrashIcon } from "lucide-react"

interface AccountCardProps {
  id: string
  title: string
  subtitle: string
  setAccounts: React.Dispatch<React.SetStateAction<createContaFormData[]>>
}



export function AccountCard({id, title, subtitle, setAccounts }: AccountCardProps) {
  const { toast } = useToast();

  const handleDeleteAccount = async (id: string) => {
    try {
      await contasService.deleteConta(id);

      toast({
        title: "Conta deletada",
        description: "A conta foi deletada com sucesso.",
      });

      setAccounts(await contasService.getAllContas());
    } catch (error) {
      console.error('Error deleting account:', error);
    }
  }

  return (
    <Card
      className="
        p-4
        bg-[var(--color-card)]
        text-[var(--color-card-foreground)]
        border border-[var(--color-border)]
        rounded-[var(--radius-md)]
        shadow-sm hover:shadow-md
        transition-shadow
      "
    >
      <div className="flex items-center justify-between">
        <div>
          <h3 className="font-semibold text-[var(--color-foreground)]">{title}</h3>
          <p className="text-sm text-[var(--color-muted-foreground)]">{subtitle}</p>
        </div>
        <div>
          <Button variant="ghost"><PencilIcon color="blue" /></Button>
          <Button variant="ghost" onClick={() => handleDeleteAccount(id)}><TrashIcon color="red"/>
          </Button>
        </div>
      </div>
    </Card>
  )
}
