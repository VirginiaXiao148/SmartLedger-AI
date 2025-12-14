'use client'; // Necesario para usar interactividad (useState)

import { useState } from 'react';
import { Send, Wallet, Loader2, CheckCircle } from 'lucide-react';
import Link from 'next/link';





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
    // 1. CONTENEDOR PRINCIPAL
    <div className="flex min-h-screen bg-gray-50">
      
      {/* 2. SIDEBAR (Izquierda) */}
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col p-4 fixed h-full md:relative z-10">
        
        {/* Logo o Título pequeño en la barra */}
        <div className="mb-8 px-4 font-bold text-blue-900 text-xl flex items-center gap-2">
           <Wallet className="w-6 h-6" />
           SmartLedger
        </div>

        <nav className="flex flex-col gap-2">
          <Link href="/gastos" className="px-4 py-2 rounded-md bg-blue-50 text-blue-700 font-medium transition-colors flex items-center gap-2">
            <CheckCircle className="w-4 h-4" />
            Gastos
          </Link>
        </nav>
      </aside>

      {/* 3. ÁREA DE CONTENIDO (Derecha) */}
      <main className="flex-1 flex flex-col items-center justify-start p-8 md:p-12 overflow-y-auto">
        
        {/* Título Principal */}
        <div className="mb-8 text-center mt-10">
          <h1 className="text-4xl font-bold text-blue-900 flex items-center justify-center gap-3">
            SmartLedger AI
          </h1>
          <p className="text-gray-600 mt-2">Tu contable personal con Inteligencia Artificial</p>
        </div>

        {/* Tarjeta Principal */}
        <div className="w-full max-w-lg bg-white rounded-xl shadow-xl overflow-hidden border border-gray-100">
          <div className="p-8">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Describe tu gasto
            </label>
            <textarea
              className="w-full p-4 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition h-32 resize-none text-gray-900"
              placeholder="Ej: Cena con amigos en el centro 45.50 euros..."
              value={inputText}
              onChange={(e) => setInputText(e.target.value)}
            />

            <button
              onClick={analizarGasto}
              disabled={loading || !inputText}
              className={`w-full mt-4 flex items-center justify-center gap-2 py-3 px-4 rounded-lg text-white font-medium transition-all ${
                loading || !inputText
                  ? 'bg-blue-300 cursor-not-allowed'
                  : 'bg-blue-600 hover:bg-blue-700 shadow-lg hover:shadow-blue-500/30'
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
              <div className="mt-4 p-3 bg-red-50 text-red-700 rounded-lg text-sm border border-red-200">
                {error}
              </div>
            )}
          </div>

          {/* Sección de Resultados */}
          {result && (
            <div className="bg-blue-50 p-6 border-t border-blue-100 animate-in fade-in slide-in-from-bottom-4 duration-500">
              <div className="flex items-center gap-2 text-green-700 font-semibold mb-4">
                <CheckCircle className="w-5 h-5" /> Gasto Identificado
              </div>
              
              <div className="space-y-3">
                <div className="flex justify-between items-center bg-white p-3 rounded-md shadow-sm">
                  <span className="text-gray-500 text-sm">Categoría</span>
                  <span className="font-bold text-blue-800 px-3 py-1 bg-blue-100 rounded-full text-sm">
                    {result.categoria}
                  </span>
                </div>
                
                <div className="flex justify-between items-center bg-white p-3 rounded-md shadow-sm">
                  <span className="text-gray-500 text-sm">Importe</span>
                  <span className="font-bold text-2xl text-gray-900">
                    {result.importe} €
                  </span>
                </div>

                <div className="flex justify-between items-center bg-white p-3 rounded-md shadow-sm">
                  <span className="text-gray-500 text-sm">Descripción</span>
                  <span className="text-gray-700 font-medium text-right">{result.descripcion}</span>
                </div>
              </div>
            </div>
          )}
        </div>
        
        <p className="mt-8 text-xs text-gray-400">Powered by Spring Boot 4 & Next.js 16</p>
      </main>
    </div>
  );
}