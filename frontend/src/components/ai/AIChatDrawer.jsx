import { useEffect, useRef, useState } from "react";
import {Bot, Loader2, Send, User, X} from "lucide-react";

import { chat } from "../../api/aiApi";
import { Button } from "../ui/button";
import { Card } from "../ui/card";
import { ScrollArea } from "../ui/scroll-area";
import { Separator } from "../ui/separator";
import { Textarea } from "../ui/textarea";

export default function AIChatDrawer({ open, onClose }) {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);

    const bottomRef = useRef(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({
            behavior: "smooth",
        });
    }, [messages, loading]);

    const sendMessage = async () => {
        const question = input.trim();

        if (!question || loading) {
            return;
        }

        setMessages((previous) => [
            ...previous,
            {
                role: "user",
                content: question,
            },
        ]);

        setInput("");

        try {
            setLoading(true);

            const response = await chat(question);

            setMessages((previous) => [...previous,
                {
                    role: "assistant",
                    content: response.answer,
                },
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
        <div className="fixed inset-y-0 right-0 z-50 w-full max-w-md border-l bg-background shadow-2xl">
            <div className="flex h-full flex-col">
                <div className="flex items-center justify-between px-6 py-5">
                    <div className="flex items-center gap-3">
                        <div className="rounded-lg bg-primary/10 p-2">
                            <Bot className="h-5 w-5 text-primary" />
                        </div>

                        <div>
                            <h2 className="text-lg font-semibold">IncidentOps AI</h2>
                            <p className="text-sm text-muted-foreground">Ask about incidents and logs</p>
                        </div>
                    </div>

                    <Button size="icon" variant="ghost" onClick={onClose}>
                        <X className="h-5 w-5" />
                    </Button>
                </div>

                <Separator />

                <ScrollArea className="flex-1">
                    <div className="space-y-5 p-6">
                        {messages.length === 0 && (
                            <Card className="border-dashed shadow-none">
                                <div className="flex flex-col items-center py-10 text-center">
                                    <Bot className="mb-4 h-10 w-10 text-primary" />
                                    <h3 className="font-semibold">AI Incident Assistant</h3>
                                    <p className="mt-2 max-w-xs text-sm text-muted-foreground">Ask questions about incidents, uploaded logs, summaries or root causes.</p>
                                </div>
                            </Card>
                        )}

                        {messages.map((message, index) => (
                            <div key={index} className={`flex gap-3 ${ message.role === "user" ? "justify-end" : "justify-start"}`}>
                                {message.role === "assistant" && (
                                    <div className="rounded-full bg-primary/10 p-2">
                                        <Bot className="h-4 w-4 text-primary" />
                                    </div>
                                )}

                                <div className={`max-w-[82%] rounded-2xl px-4 py-3 text-sm leading-6 ${
                                        message.role === "user"
                                            ? "bg-primary text-primary-foreground"
                                            : "border bg-card"
                                    }`}>
                                    {message.content}
                                </div>

                                {message.role === "user" && (
                                    <div className="rounded-full bg-muted p-2">
                                        <User className="h-4 w-4" />
                                    </div>
                                )}
                            </div>
                        ))}

                        {loading && (
                            <div className="flex gap-3">
                                <div className="rounded-full bg-primary/10 p-2">
                                    <Bot className="h-4 w-4 text-primary" />
                                </div>
                                <div className="flex items-center gap-2 rounded-2xl border bg-card px-4 py-3 text-sm text-muted-foreground">
                                    <Loader2 className="h-4 w-4 animate-spin" />
                                    Thinking...
                                </div>
                            </div>
                        )}
                        <div ref={bottomRef} />
                    </div>
                </ScrollArea>

                <Separator />

                <div className="p-5">
                    <div className="flex items-end gap-3">
                        <Textarea rows={2} value={input} onChange={(e) => setInput(e.target.value)} onKeyDown={handleKeyDown} placeholder="Ask anything about your incidents..." className="min-h-[70px] resize-none"/>
                        <Button size="icon" disabled={loading} onClick={sendMessage} className="h-11 w-11">
                            <Send className="h-4 w-4" />
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}