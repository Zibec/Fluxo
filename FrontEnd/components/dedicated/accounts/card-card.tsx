"use client"

import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast"
import { createCartaoFormData } from "@/lib/service/contas-cartoes/contas-cartoes-schemas"
import { cartoesService } from "@/lib/service/contas-cartoes/contas-cartoes-service"
import { PencilIcon, Plus, TrashIcon } from "lucide-react"

interface CardCardProps {
  id: string
  title: string
  cardNumber: string
  setCards: React.Dispatch<React.SetStateAction<createCartaoFormData[]>>
}



export function CardCard({id, title, cardNumber, setCards }: CardCardProps) {
  const { toast } = useToast();

  const handleDeleteCard = async (id: string) => {
    try {
      await cartoesService.deleteCartao(id);

      toast({
        title: "Cartão deletado",
        description: "O cartão foi deletado com sucesso.",
      });

      setCards(await cartoesService.getAllCartoes());

    } catch (error) {
      console.error('Error deleting card:', error);
    }
  }

  return (
    <Card
      className="
        bg-[var(--color-card)]
        text-[var(--color-card-foreground)]
        border border-[var(--color-border)]
        rounded-[var(--radius-md)]
        shadow-sm hover:shadow-md
        transition-shadow
        p-4
      "
    >
      <div className="flex items-center justify-between">
        <div>
          <h3 className="font-semibold text-[var(--color-foreground)]">{title}</h3>
          <p className="text-sm text-[var(--color-muted-foreground)] font-mono">{cardNumber}</p>
        </div>
        <div>
          <Button variant="ghost"><PencilIcon color="blue" /></Button>
          <Button variant="ghost" onClick={() => handleDeleteCard(id)}><TrashIcon color="red"/>
          </Button>
        </div>
      </div>
    </Card>
  )
}
