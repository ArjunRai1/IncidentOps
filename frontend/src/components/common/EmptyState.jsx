export default function EmptyState({
    title,
    message,
}) {
    return (
        <div className="rounded-lg border border-dashed border-gray-300 bg-white p-10 text-center">
            <h2 className="text-xl font-semibold">{title}</h2>
            <p className="mt-2 text-gray-500">{message}</p>
        </div>
    );
}