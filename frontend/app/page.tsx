'use client'; // Necesario para usar interactividad (useState)

import { useState } from 'react';
import { Send, Wallet, Loader2, CheckCircle } from 'lucide-react';





export default function Home() {
  const [inputText, setInputText] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<any>(null); // Guardaremos el JSON aquí
  const [error, setError] = useState('');

  const analizarGasto = async () => {
    if (!inputText.trim()) return;
    
    setLoading(true);
    setError('');
    setResult(null);

    const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

    try {
      // Llamada a tu Backend Spring Boot
      const response = await fetch(`${apiUrl}/api/gastos/analizar`, {
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain', // Enviamos texto plano, igual que en Postman
        },
        body: inputText,
      });

      if (!response.ok) throw new Error('Error al conectar con el servidor');

      const data = await response.json();
      setResult(data);
    } catch (err) {
      setError('No se pudo analizar el gasto. ¿Está el backend encendido?');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto w-full pt-6">

      {/* Título Principal */}
      <div className="mb-10 text-center">
        <h1 className="text-4xl font-bold text-foreground flex items-center justify-center gap-3 tracking-tight">
          <Wallet className="w-10 h-10 text-primary" />
          SmartLedger AI
        </h1>
        <p className="text-muted-foreground mt-3 text-lg">
          Tu contable personal con Inteligencia Artificial
        </p>
      </div>

      {/* Tarjeta Principal */}
      <div className="w-full bg-card rounded-xl shadow-sm border border-border overflow-hidden">
        <div className="p-8">
          <label className="block text-sm font-medium text-foreground mb-3">
            Describe tu gasto
          </label>
          
          <textarea
            className="w-full p-4 border border-input rounded-lg bg-background focus:ring-2 focus:ring-primary focus:border-primary outline-none transition h-32 resize-none text-foreground placeholder:text-muted-foreground"
            placeholder="Ej: Cena con amigos en el centro 45.50 euros..."
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
          />

          <button
            onClick={analizarGasto}
            disabled={loading || !inputText}
            className={`w-full mt-6 flex items-center justify-center gap-2 py-3 px-4 rounded-lg font-medium transition-all ${
              loading || !inputText
                ? 'bg-muted text-muted-foreground cursor-not-allowed'
                : 'bg-primary text-primary-foreground hover:bg-primary/90 shadow-md hover:shadow-lg'
            }`}
          >
            {loading ? (
              <>
                <Loader2 className="animate-spin w-5 h-5" /> Analizando...
              </>
            ) : (
              <>
                <Send className="w-5 h-5" /> Procesar Gasto
              </>
            )}
          </button>

          {/* Mensaje de Error */}
          {error && (
            <div className="mt-4 p-3 bg-red-50 text-red-600 rounded-lg text-sm border border-red-100">
              {error}
            </div>
          )}
        </div>

        {/* Sección de Resultados */}
        {result && (
          <div className="bg-muted/30 p-8 border-t border-border animate-in fade-in slide-in-from-bottom-2 duration-500">
            <div className="flex items-center gap-2 text-primary font-semibold mb-6">
              <CheckCircle className="w-5 h-5" /> Gasto Identificado
            </div>
            
            <div className="space-y-4">
              <div className="flex justify-between items-center bg-card p-4 rounded-lg border border-border/50 shadow-sm">
                <span className="text-muted-foreground text-sm">Categoría</span>
                <span className="font-bold text-primary px-3 py-1 bg-primary/10 rounded-full text-sm border border-primary/20">
                  {result.categoria}
                </span>
              </div>
              
              <div className="flex justify-between items-center bg-card p-4 rounded-lg border border-border/50 shadow-sm">
                <span className="text-muted-foreground text-sm">Importe</span>
                <span className="font-bold text-3xl text-foreground">
                  {result.importe} €
                </span>
              </div>

              <div className="flex justify-between items-center bg-card p-4 rounded-lg border border-border/50 shadow-sm">
                <span className="text-muted-foreground text-sm">Descripción</span>
                <span className="text-foreground font-medium text-right">{result.descripcion}</span>
              </div>
            </div>
          </div>
        )}
      </div>
      
      <p className="mt-8 text-center text-xs text-muted-foreground/60">
        Powered by Spring Boot 4 & Next.js 16
      </p>
    </div>
  );
}