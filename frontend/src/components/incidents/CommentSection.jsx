import { useEffect, useState } from "react";

import {addComment,getComments} from "../../api/commentApi";

import { Card } from "../ui/card";
import { Button } from "../ui/Button";
import Loader from "../common/Loader";
import EmptyState from "../common/EmptyState";

import { formatDate } from "../../utils/formatters";

export default function CommentsSection({ incidentId }) {
    const [comments, setComments] = useState([]);
    const [comment, setComment] = useState("");

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [error, setError] = useState("");

    useEffect(() => {
        loadComments();
    }, [incidentId]);

    const loadComments = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getComments(incidentId);

            setComments(data);

        } catch (err) {
            setError(err.response?.data?.message ??"Failed to load comments.");
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!comment.trim()) {
            return;
        }

        try {
            setSaving(true);

            const created = await addComment(incidentId, {comment,});
            setComments((previous) => [created, ...previous,]);
            setComment("");

        } catch (err) {
            setError(
                err.response?.data?.message ??
                    "Failed to add comment."
            );
        } finally {
            setSaving(false);
        }
    };

    return (
        <div className="space-y-6">
            <form onSubmit={handleSubmit} className="mb-8 space-y-4">
                <textarea rows={4} value={comment} onChange={(e) =>setComment(e.target.value)}
                    placeholder="Add a comment..."
                    className="w-full rounded-md border border-gray-300 p-3 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"
                />
                <div className="flex justify-end">
                    <Button type="submit" loading={saving} disabled={!comment.trim()}>Add Comment</Button>
                </div>
            </form>

            {loading && <Loader />}

            {!loading && error && (
                <div className="rounded-md border border-red-200 bg-red-50 p-4 text-red-700">
                    {error}
                </div>
            )}

            {!loading && !error && comments.length === 0 && (
                    <EmptyState title="No Comments" message="Be the first to add a comment."/>
                )}
            <div className="space-y-4">
                {comments.map((item) => (
                    <Card key={item.id}>
                        <div className="flex items-center justify-between">
                            <h3 className="font-semibold">{item.email}</h3>
                            <span className="text-sm text-gray-500">
                                {formatDate(item.createdAt)}
                            </span>
                        </div>
                        <p className="mt-3 whitespace-pre-wrap text-gray-700">
                            {item.comment}
                        </p>
                    </Card>
                ))}
            </div>
        </div>
    );
}