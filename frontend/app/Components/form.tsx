



interface FormData {
    importe: number;
    fecha: string;
    categoria: string;
    descripcion: string;
}

interface FormProps {
    onSubmit: (data: FormData) => void;
}

export default function Form({ onSubmit }: FormProps) {
    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const form = e.currentTarget;
        const formData = new FormData(form);

        onSubmit({
            importe: Number(formData.get('importe')),
            fecha: formData.get('fecha') as string,
            categoria: formData.get('categoria') as string,
            descripcion: formData.get('descripcion') as string,
        });
    };

    return (
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <input name="importe" type="number" placeholder="Importe" required className="p-2 border rounded" />
            <input name="fecha" type="date" placeholder="Fecha" required className="p-2 border rounded" />
            <input name="categoria" type="text" placeholder="Categoria" required className="p-2 border rounded" />
            <input name="descripcion" type="text" placeholder="Descripcion" required className="p-2 border rounded" />
            <button type="submit" className="p-2 bg-blue-500 text-white rounded">Enviar</button>
        </form>
    );
}