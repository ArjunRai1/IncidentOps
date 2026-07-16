import { useRef, useState } from "react";
import { FileText, Upload, Loader2, CheckCircle2 } from "lucide-react";

import { Button } from "../ui/button";

export default function LogUpload({ onUpload }) {
    const inputRef = useRef(null);

    const [selectedFile, setSelectedFile] = useState(null);
    const [uploading, setUploading] = useState(false);

    const handleSelect = (event) => {
        const file = event.target.files?.[0];

        if (!file) {
            return;
        }

        setSelectedFile(file);
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            return;
        }

        try {
            setUploading(true);
            await onUpload(selectedFile);
            setSelectedFile(null);

            if (inputRef.current) {
                inputRef.current.value = "";
            }
        } finally {
            setUploading(false);
        }
    };

    return (
        <div className="space-y-6">
                <div className="flex cursor-pointer flex-col items-center justify-center rounded-lg border border-dashed border-muted-foreground/20 bg-muted/30 px-6 py-10 text-center transition-colors hover:bg-muted/50" onClick={() => inputRef.current?.click()}>
                    <Upload className="mb-4 h-10 w-10 text-primary" />
                    <h3 className="text-lg font-semibold">Upload Log File</h3>
                    <p className="mt-2 max-w-md text-sm text-muted-foreground">Upload application or server logs for AI-powered root cause analysis.</p>
                    <input ref={inputRef} type="file" className="hidden" onChange={handleSelect}/>
                </div>
                {selectedFile && (
                    <div className="flex items-center justify-between rounded-lg border bg-background p-4">
                        <div className="flex items-center gap-3">
                            <FileText className="h-5 w-5 text-primary" />
                            <div>
                                <p className="font-medium">{selectedFile.name}</p>
                                <p className="text-sm text-muted-foreground">{(selectedFile.size / 1024).toFixed(1)} KB</p>
                            </div>
                        </div>

                        <Button loading={uploading} onClick={handleUpload}>
                            {uploading ? (
                                <>
                                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                    Uploading...
                                </>
                            ) : (
                                "Upload"
                            )}
                        </Button>
                    </div>
                )}

                {!selectedFile && (
                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                        <CheckCircle2 className="h-4 w-4 text-green-600" />Supported log formats accepted by your backend.
                    </div>
                )}
            </div>
    );
}