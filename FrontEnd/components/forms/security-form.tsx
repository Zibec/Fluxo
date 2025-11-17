'use client'

import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { useToast } from '@/hooks/use-toast'
import { AlertCircle } from 'lucide-react'
import { createSecurityFormData, securitySchema } from '@/lib/service/usuario/usuario-schema'
import { usuarioService } from '@/lib/service/usuario/usuario-service'

export function SecurityForm() {
  const { toast } = useToast()
  const form = useForm<createSecurityFormData>({
    resolver: zodResolver(securitySchema),
    defaultValues: {
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
    },
  })

  async function onSubmit(data: createSecurityFormData) {
    await usuarioService.alterarSenha(data).then(() => {
      toast({
        title: 'Senha alterada',
        description: 'Sua senha foi atualizada com sucesso.',
      })
      form.reset()
    })
    console.log(data)
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Segurança</CardTitle>
        <CardDescription>Altere sua senha e configure opções de segurança</CardDescription>
      </CardHeader>
      <CardContent className="space-y-6">
        <Alert>
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>
            Use uma senha forte com números e letras maiúsculas para melhor segurança
          </AlertDescription>
        </Alert>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            <FormField
              control={form.control}
              name="currentPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Senha Atual</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="••••••••" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="newPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nova Senha</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="••••••••" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="confirmPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Confirmar Senha</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="••••••••" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <Button type="submit" className="w-full sm:w-auto">
              Atualizar Senha
            </Button>
          </form>
        </Form>

        <div className="mt-8 pt-8 border-t">
          <h3 className="text-lg font-semibold mb-4">Sessões Ativas</h3>
          <p className="text-sm text-muted-foreground mb-4">
            Você está conectado em 3 dispositivos
          </p>
          <Button variant="outline">Encerrar Todas as Sessões</Button>
        </div>
      </CardContent>
    </Card>
  )
}
