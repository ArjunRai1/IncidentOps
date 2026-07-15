import { useState } from "react";

export default function LogUpload({ onUpload }) {
    const [file, setFile] = useState(null);
    const [uploading, setUploading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if(!file){
            return;
        }

        try{
            setUploading(true);
            await onUpload(file);
            setFile(null);
            e.target.reset();
        } finally{
            setUploading(false);
        }
    };

    return (
        <div className="bg-white rounded-lg shadow p-6 mt-6">
            <h2 className="text-xl font-semibold mb-4">Upload Incident Log</h2>
            <form onSubmit={handleSubmit} className="flex gap-4 items-center">
                <input type="file" accept=".txt,.log" onChange={(e) => setFile(e.target.files[0])} className="block w-full text-sm border rounded p-2"/>
                <button type="submit" disabled={!file || uploading} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:bg-gray-400">
                    {uploading ? "Uploading..." : "Upload"}
                </button>
            </form>
        </div>
    );
}