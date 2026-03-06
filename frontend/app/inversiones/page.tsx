'use client';
import { Search, Loader2, RefreshCw } from 'lucide-react';
import { useState, useEffect } from 'react';



export default function InversionesPage() {

    const [inversiones, setInversiones] = useState([]);
    const [busqueda, setBusqueda] = useState('');
    const [loading, setLoading] = useState(false);

    const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

    const fetchInversiones = async () => {
        setLoading(true);
        try {
            const response = await fetch(`${apiUrl}/api/inversiones`);

            if (!response.ok) {
                throw new Error('Error al obtener las inversiones');
            }

            const data = await response.json();

            setInversiones(data);
            console.log("Datos cargados:", data);
        } catch (error) {
            console.error("Error fetching data:", error);
        } finally {
            setLoading(false);
        }
    }

    const buscarInversiones = async () => {
        if (!busqueda.trim()) {
            fetchInversiones();
            return;
        }
        setLoading(true);
        try {
            const response = await fetch(`${apiUrl}/api/inversiones/buscar?texto=${busqueda}`);

            if (!response.ok) {
                throw new Error('Error en la búsqueda');
            }

            const data = await response.json();

            setInversiones(data);
            console.log("Datos cargados:", data);
        } catch (error) {
            console.error("Error fetching data:", error);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchInversiones();
    }, []);

    return (
        <div className="max-w-4xl mx-auto w-full pt-6">

            <div className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-foreground">Historial de Inversiones</h1>
                <button
                    onClick={fetchInversiones}
                    className="text-sm text-muted-foreground hover:text-primary flex items-center gap-2 transition-colors"
                >
                    <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
                    Actualizar lista
                </button>
            </div>

            {/* TARJETA DE BÚSQUEDA */}
            <div className="bg-card p-4 rounded-xl border border-border shadow-sm mb-6">
                <div className="flex gap-3">
                    <input
                        type="text"
                        placeholder="Ej: Acciones de Apple, Bitcoin, Bonos..."
                        className="flex-1 p-3 border border-input rounded-lg bg-background focus:ring-2 focus:ring-primary focus:border-primary outline-none text-foreground placeholder:text-muted-foreground transition-all"
                        value={busqueda}
                        onChange={(e) => setBusqueda(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && buscarInversiones()}
                    />
                    <button
                        onClick={buscarInversiones}
                        className="bg-primary text-primary-foreground px-6 py-2 rounded-lg hover:bg-primary/90 transition-all font-medium flex items-center gap-2 shadow-sm"
                    >
                        {loading ? <Loader2 className="w-5 h-5 animate-spin" /> : <Search className="w-5 h-5" />}
                        Buscar
                    </button>
                </div>
            </div>

            {/* LISTADO DE RESULTADOS */}
            <div className="bg-card rounded-xl border border-border shadow-sm overflow-hidden">
                {/* Cabecera de la tabla */}
                <div className="px-6 py-4 border-b border-border bg-muted/20">
                    <h2 className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">
                        Últimos Registros
                    </h2>
                </div>

                <ul className="divide-y divide-border">
                    {inversiones.length === 0 ? (
                        <li className="p-10 text-center text-muted-foreground">
                            {loading ? "Cargando datos..." : "No hay movimientos registrados. ¡Prueba a buscar o cargar el listado!"}
                        </li>
                    ) : (
                        inversiones.map((inversion: any) => (
                            <li key={inversion.id} className="p-5 hover:bg-muted/30 transition-colors flex justify-between items-center group">
                                <div className="flex flex-col gap-1">
                                    {/* Categoría como etiqueta pequeña */}
                                    <span className="w-fit text-xs font-bold text-primary bg-primary/10 px-2 py-0.5 rounded-full border border-primary/20">
                                        {inversion.categoria}
                                    </span>
                                    {/* Descripción */}
                                    <p className="text-foreground font-medium text-lg">
                                        {inversion.descripcion}
                                    </p>
                                    {/* Fecha */}
                                    <span className="text-xs text-muted-foreground">
                                        {inversion.fecha || "Fecha no registrada"}
                                    </span>
                                </div>

                                {/* Importe */}
                                <div className="text-right">
                                    <span className="block font-bold text-xl text-foreground group-hover:text-primary transition-colors">
                                        {inversion.importe} €
                                    </span>
                                </div>
                            </li>
                        ))
                    )}
                </ul>
            </div>

            <p className="mt-8 text-center text-xs text-muted-foreground/60">
                Mostrando {inversiones.length} resultados
            </p>
        </div>
    )
}