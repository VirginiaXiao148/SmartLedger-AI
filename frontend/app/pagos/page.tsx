'use client';
import { useState } from 'react';



export default function Home(){

    const [pagos] = useState([]);
    
    const fetchPagos = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/pagos');
            const data = await response.json();
            return data;
        } catch (error) {
            console.error(error);
        }
    }

    return(
        <main>
            <h1>Pagos</h1>
            <button onClick={fetchPagos}>Obtener pagos</button>
            <ul>
                {pagos.map((pago: any) => (
                    <li key={pago.id}>{pago.textoOriginal}</li>
                ))}
            </ul>
        </main>
    )
}


