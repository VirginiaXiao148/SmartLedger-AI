

export default function ConfiguracionPage() {
    return (
        <div className="max-w-md mx-auto p-6">
            <h1 className="text-2xl font-bold mb-4">Configuración</h1>
            <div className="bg-card p-4 rounded-xl border border-border">
                <label className="block text-sm font-medium mb-2">Presupuesto Mensual (€)</label>
                <input 
                    type="number" 
                    placeholder="Ej: 1200" 
                    className="w-full p-2 border rounded mb-4 bg-background"
                />
                <button className="w-full bg-primary text-primary-foreground py-2 rounded-lg">
                    Guardar Presupuesto
                </button>
            </div>
        </div>
    );
}