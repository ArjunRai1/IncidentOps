import { Bot } from "lucide-react";
import { Button } from "../ui/button";

export default function AIChatButton({ onClick }) {
    return (
        <Button size="icon" onClick={onClick} aria-label="Open AI Assistant" className="fixed bottom-6 right-6 z-40 h-14 w-14 rounded-full shadow-lg shadow-primary/20">
            <Bot className="h-6 w-6" />
        </Button>
    );
}