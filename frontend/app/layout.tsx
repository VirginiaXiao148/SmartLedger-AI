import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link"; // Importamos Link para la navegación
import { Wallet, LayoutDashboard, CreditCard } from "lucide-react"; // Iconos
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "SmartLedger AI",
  description: "Tu contable personal con Inteligencia Artificial",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="es">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased bg-background text-foreground`}
      >
        {/* Estructura Principal del Layout (Sidebar Fija + Contenido Dinámico) */}
        <div className="flex min-h-screen w-full">
          
          {/* --- SIDEBAR GLOBAL --- */}
          <aside className="w-64 bg-card border-r border-border hidden md:flex flex-col p-6 fixed h-full z-10">
            {/* Logo */}
            <div className="flex items-center gap-3 mb-10 px-2">
              <div className="p-2 bg-primary/10 rounded-lg">
                <Wallet className="w-6 h-6 text-primary" />
              </div>
              <span className="text-xl font-bold text-foreground tracking-tight">
                SmartLedger
              </span>
            </div>

            {/* Navegación */}
            <nav className="flex flex-col gap-2 space-y-1">
              <Link 
                href="/" 
                className="flex items-center gap-3 px-4 py-3 rounded-lg text-muted-foreground hover:text-primary hover:bg-primary/5 transition-all font-medium"
              >
                <LayoutDashboard className="w-5 h-5" />
                Dashboard
              </Link>
              
              <Link 
                href="/gastos" 
                className="flex items-center gap-3 px-4 py-3 rounded-lg text-muted-foreground hover:text-primary hover:bg-primary/5 transition-all font-medium"
              >
                <CreditCard className="w-5 h-5" />
                Historial Gastos
              </Link>
            </nav>

            {/* Footer del Sidebar */}
            <div className="mt-auto pt-6 border-t border-border">
              <p className="text-xs text-muted-foreground">
                © 2025 Virginia Toledo
              </p>
            </div>
          </aside>

          {/* --- CONTENIDO DE LAS PÁGINAS --- */}
          {/* Añadimos ml-64 para dejar espacio al sidebar fijo */}
          <main className="flex-1 md:ml-64 p-8">
            {children}
          </main>
          
        </div>
      </body>
    </html>
  );
}