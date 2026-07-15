import { Bot } from "lucide-react";

export default function AIChatButton({ onClick }) {
    return (
        <button onClick={onClick}  className="fixed bottom-6 right-6 z-40 flex h-14 w-14 items-center justify-center rounded-full bg-blue-600 text-white shadow-lg transition hover:bg-blue-700 hover:shadow-xl"
            aria-label="Open AI Assistant">
            <Bot size={26} />
        </button>
    );
}