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
import { createEmailFormData, createSecurityFormData, emailSchema, securitySchema } from '@/lib/service/usuario/usuario-schema'
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

  const formEmail = useForm<createEmailFormData>({
    resolver: zodResolver(emailSchema),
    defaultValues: {
      emailAntigo: '',
      emailNovo: '',
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

  async function onSubmitEmail(data: createEmailFormData) {
    await usuarioService.alterarEmail(data.emailNovo).then(() => {
      toast({
        title: 'Email alterado',
        description: 'Seu email foi atualizado com sucesso.',
      })
      formEmail.reset()
    }
    )
    console.log(data)
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Segurança</CardTitle>
        <CardDescription>Configure opções de segurança</CardDescription>
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
          <Form {...formEmail}>
            <form onSubmit={formEmail.handleSubmit(onSubmitEmail)} className="space-y-6">
              <FormField
                control={formEmail.control}
                name="emailAntigo"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email Antigo</FormLabel>
                    <FormControl>
                      <Input type="email" placeholder="seu-email@exemplo.com" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={formEmail.control}
                name="emailNovo"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Novo Email</FormLabel>
                    <FormControl>
                      <Input type="email" placeholder="seu-email@exemplo.com" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <Button type="submit" className="w-full sm:w-auto">
                Atualizar Email
              </Button>
            </form>
          </Form>
        </div>
      </CardContent>
    </Card>
  )
}
