'use client';
import { Search, Wallet } from 'lucide-react';
import { useState } from 'react';
import Link from 'next/link';



export default function Home(){

    const [pagos, setPagos] = useState([]);
    const [busqueda, setBusqueda] = useState('');
    
    const fetchPagos = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/gastos');
            
            if (!response.ok) {
                throw new Error('Error al obtener los pagos');
            }
            
            const data = await response.json();

            setPagos(data);
            console.log("Datos cargados:", data);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    }

    const buscarGastos = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/gastos/buscar?texto=${busqueda}`);
            
            if (!response.ok) {
                throw new Error('Error en la búsqueda');
            }
            
            const data = await response.json();

            setPagos(data);
            console.log("Datos cargados:", data);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    }

    return(
        <div className="flex flex-col min-h-screen">
            
            {/* Cabecera con icono de monedero y titulo */}
            <header className="bg-gray-800 text-white p-4">
                <Wallet className="w-6 h-6" />
                <Link href="/" className="text-2xl font-bold">SmartLedger AI</Link>
            </header>

            {/* Barra de busqueda */}
            <div className="p-4">
                <input 
                    type="text"
                    placeholder="Ej: Taxi, Cena, Mercadona..."
                    className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none text-gray-800"
                    value={busqueda}
                    onChange={(e) => setBusqueda(e.target.value)} // Guardamos lo que escribes
                    onKeyDown={(e) => e.key === 'Enter' && buscarGastos()} // Buscar al dar Enter
                />
                <button 
                    onClick={buscarGastos}
                    className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition-colors mb-6"
                >
                    <Search className="w-5 h-5" />
                    Buscar
                </button>
            </div>
            
            {/* Main con titulo, boton y listado de gastos */}
            <main className="p-8">
                <h1 className="text-2xl font-bold mb-4">Historial de Gastos</h1>
                
                <button 
                    onClick={fetchPagos}
                    className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition-colors mb-6"
                >
                    Cargar listado
                </button>

                <ul className="space-y-3">
                    {/* Renderizamos los datos de forma segura */}
                    {pagos.length === 0 ? (
                        <p className="text-gray-500">No hay datos cargados aún.</p>
                    ) : (
                        pagos.map((pago: any) => (
                            <li key={pago.id} className="p-4 bg-white border border-gray-200 rounded shadow-sm flex justify-between">
                                <div>
                                    <span className="font-bold text-blue-900">{pago.categoria}</span>
                                    <p className="text-gray-600">{pago.descripcion}</p>
                                </div>
                                <span className="font-bold text-black">{pago.importe} €</span>
                            </li>
                        ))
                    )}
                </ul>
            </main>
        </div>
    )
}


