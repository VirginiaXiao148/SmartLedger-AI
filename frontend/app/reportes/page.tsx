'use client';
import dynamic from 'next/dynamic';
import { useState, useEffect } from 'react';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

export default function Home() {

    const Bar = dynamic(() => import('react-chartjs-2').then((mod) => mod.Bar), {
        ssr: false,
    });

    const [chartData, setChartData] = useState<number[]>([0, 0, 0, 0, 0]);
    const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

    const fetchGastos = async () => {
        try {
            const response = await fetch(`${apiUrl}/api/gastos`);
            if (!response.ok) throw new Error('Error al conectar con el servidor');
            const data = await response.json();

            // Agrupar por categoría
            let vivienda = 0, transporte = 0, alimentacion = 0, ocio = 0, otros = 0;

            data.forEach((gasto: any) => {
                const cat = gasto.categoria ? gasto.categoria.toLowerCase() : '';
                const importe = Number(gasto.importe) || 0;

                if (cat.includes('casa') || cat.includes('vivienda')) vivienda += importe;
                else if (cat.includes('transporte')) transporte += importe;
                else if (cat.includes('alimentación') || cat.includes('alimentacion')) alimentacion += importe;
                else if (cat.includes('ocio')) ocio += importe;
                else otros += importe;
            });

            setChartData([vivienda, transporte, alimentacion, ocio, otros]);

        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchGastos();
    }, []);

    const dataChart = {
        labels: ['Vivienda', 'Transporte', 'Alimentación', 'Ocio', 'Otros'],
        datasets: [
            {
                label: 'Gastos por Categoría (€)',
                data: chartData,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.4)',
                    'rgba(54, 162, 235, 0.4)',
                    'rgba(255, 206, 86, 0.4)',
                    'rgba(75, 192, 192, 0.4)',
                    'rgba(153, 102, 255, 0.4)',
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                ],
                borderWidth: 1,
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top' as const,
            },
            title: {
                display: true,
                text: 'Resumen de Gastos',
            },
        },
    };

    return (
        <div className="max-w-4xl mx-auto w-full pt-6">
            <h1 className="text-3xl font-bold text-foreground mb-8">Reportes</h1>

            <div className="bg-card p-6 rounded-xl border border-border shadow-sm">
                <Bar options={options} data={dataChart} />
            </div>

        </div>
    );
}