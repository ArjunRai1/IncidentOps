export default function Badge({ children, variant = "default" }) {
    const variants = {
        default: "bg-gray-100 text-gray-700",
        open: "bg-blue-100 text-blue-700",
        inProgress: "bg-amber-100 text-amber-700",
        resolved: "bg-green-100 text-green-700",
        low: "bg-green-100 text-green-700",
        medium: "bg-yellow-100 text-yellow-700",
        high: "bg-red-100 text-red-700",
    };

    return (
        <span className={`inline-flex rounded-full px-3 py-1 text-xs font-medium ${variants[variant]}`}>
            {children}
        </span>
    );
}