export default function Input({
    label,
    error,
    className = "",
    ...props
}) {
    return (
        <div>
            {label && (
                <label className="mb-1 block text-sm font-medium text-gray-700">{label}</label>
            )}

            <input className={`w-full rounded-md border border-gray-300 p-2 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500 ${className}`}
                {...props}
            />

            {error && (
                <p className="mt-1 text-sm text-red-500">{error}</p>
            )}
        </div>
    );
}