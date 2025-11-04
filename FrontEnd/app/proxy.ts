import { NextResponse, NextRequest } from 'next/server'
 
export function proxy(request: NextRequest) {
  const isAuthenticated: boolean = false

  console.log('Proxy middleware triggered for:', request.url, isAuthenticated)
 
  if (isAuthenticated) {
    return NextResponse.next()
  }
 
  return NextResponse.redirect(new URL('/', request.url))
}
 
export const config = {
    matcher: [
    {
      source: '/dashboard/:path*',
      locale: false,
      has: [
        { type: 'header', key: 'Authorization', value: 'Bearer Token' },
      ]
    },
  ],
}