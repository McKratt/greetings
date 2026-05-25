export interface StatsResponse {
    counters: Record<string, number>;
}

export async function getStats(): Promise<StatsResponse | null> {
    const response = await fetch('/api/stats', {
        headers: {'Accept': 'application/json'},
    });
    if (response.status === 204) return null;
    if (!response.ok) throw new Error(`API error: ${response.status}`);
    return response.json();
}
