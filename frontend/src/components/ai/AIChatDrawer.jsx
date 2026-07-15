import { useEffect, useRef, useState } from "react";
import { X, Send } from "lucide-react";
import { chat } from "../../api/aiApi";

export default function AIChatDrawer({ open, onClose }) {

    const [messages, setMessages] = useState([]);

    const [input, setInput] = useState("");

    const [loading, setLoading] = useState(false);

    const bottomRef = useRef(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({
            behavior: "smooth"
        });
    }, [messages]);

    const sendMessage = async () => {

        const question = input.trim();

        if (!question || loading) {
            return;
        }

        setMessages((previous) => [
            ...previous,
            {
                role: "user",
                content: question
            }
        ]);

        setInput("");

        try {

            setLoading(true);

            const response = await chat(question);

            setMessages((previous) => [
                ...previous,
                {
                    role: "assistant",
                    content: response.answer
                }
            ]);

        } finally {

            setLoading(false);

        }

    };

    const handleKeyDown = (event) => {

        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }

    };

    if (!open) {
        return null;
    }

    return (
        <div className="fixed inset-y-0 right-0 z-50 flex w-full max-w-md flex-col bg-white shadow-2xl">
            <div className="flex items-center justify-between border-b p-4">
                <h2 className="text-lg font-semibold">IncidentOps AI</h2>
                <button onClick={onClose}><X size={22} /></button>
            </div>

            <div className="flex-1 overflow-y-auto p-4 space-y-4">
                {messages.map((message, index) => (
                    <div key={index} className={`rounded-lg p-3 max-w-[85%] ${message.role === "user" ? "ml-auto bg-blue-600 text-white": "bg-gray-100 text-gray-800"}`}>
                        {message.content}
                    </div>

                ))}

                {loading && (

                    <div className="bg-gray-100 rounded-lg p-3 w-fit">
                        Thinking...
                    </div>
                )}
                <div ref={bottomRef} />
            </div>
            <div className="border-t p-4">
                <div className="flex gap-2">
                    <textarea rows={2} value={input} onChange={(e) => setInput(e.target.value)} onKeyDown={handleKeyDown}
                        placeholder="Ask about incidents..."
                        className="flex-1 rounded border p-2 resize-none"
                    />

                    <button onClick={sendMessage} disabled={loading} className="rounded bg-blue-600 p-3 text-white hover:bg-blue-700 disabled:bg-gray-400">
                        <Send size={18} />
                    </button>
                </div>
            </div>
        </div>

    );

}