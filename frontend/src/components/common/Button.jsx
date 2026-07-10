export default function Button({
    children,
    type = "button",
    variant = "primary",
    loading = false,
    disabled = false,
    className = "",
    ...props
}) {
    const baseClasses =
        "rounded-md px-4 py-2 font-medium transition-colors disabled:cursor-not-allowed disabled:opacity-50";

    const variants = {
        primary: "bg-blue-600 text-white hover:bg-blue-700",
        secondary: "bg-gray-200 text-gray-800 hover:bg-gray-300",
        danger: "bg-red-600 text-white hover:bg-red-700",
    };

    return (
        <button type={type} disabled={disabled || loading} className={`${baseClasses} ${variants[variant]} ${className}`}
            {...props}>
            {loading ? "Please wait..." : children}
        </button>
    );
}