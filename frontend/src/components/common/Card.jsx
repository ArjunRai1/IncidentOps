export default function Card({children, className = "", ...props}){
    return (
        <div className={`rounded-lg bg-white p-6 shadow ${className}`}{...props}>
            {children}
        </div>
    );
}