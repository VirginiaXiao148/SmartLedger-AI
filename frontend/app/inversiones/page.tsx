'use client';
import { Search, Loader2, RefreshCw } from 'lucide-react';
import { useState } from 'react';



export default function Page() {
    const [inputText, setInputText] = useState('');
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState<any>(null); // Guardaremos el JSON aquí
    const [error, setError] = useState('');
    
    const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

    const analizarInversion = async () => {
        if (!inputText.trim()) return;
        
        setLoading(true);
        setError('');
        setResult(null);

        try {
            // Llamada a tu Backend Spring Boot
            const response = await fetch(`${apiUrl}/api/inversiones/analizar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: inputText,
            });

            if (!response.ok) throw new Error('Error al conectar con el servidor');

            const data = await response.json();
            setResult(data);
        } catch (err) {
            setError('No se pudo analizar la inversión. ¿Está el backend encendido?');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const fetchInversiones = async () => {
        try {
            setLoading(true);
            setError('');
            setResult(null);

            const response = await fetch(`${apiUrl}/api/inversiones`);
            if (!response.ok) throw new Error('Error al conectar con el servidor');

            const data = await response.json();
            setResult(data);
        } catch (err) {
            setError('No se pudo obtener el historial de inversiones.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const buscarInversiones = async () => {
        if (!inputText.trim()) return;
        
        setLoading(true);
        setError('');
        setResult(null);

        try {
            // Llamada a tu Backend Spring Boot
            const response = await fetch(`${apiUrl}/api/inversiones/buscar?texto=${inputText}`);
            if (!response.ok) throw new Error('Error al conectar con el servidor');

            const data = await response.json();
            setResult(data);
        } catch (err) {
            setError('No se pudo buscar el historial de inversiones.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };
    
    return (
        <div>
            <h1>Historial de Inversiones</h1>
        </div>
    );
}