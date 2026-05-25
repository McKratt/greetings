export interface CreateGreetingRequest {
    type: string;
    name: string;
}

export interface CreateGreetingResponse {
    id: string;
    message: string;
}

export interface GetGreetingResponse {
    type: string;
    name: string;
}

export interface UpdateGreetingRequest {
    newType: string;
}

export interface UpdateGreetingResponse {
    message: string;
}

export async function createGreeting(req: CreateGreetingRequest): Promise<CreateGreetingResponse> {
    const response = await fetch('/api/greetings', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(req),
    });
    if (!response.ok) throw new Error(`API error: ${response.status}`);
    return response.json();
}

export async function getGreetingById(id: string): Promise<GetGreetingResponse | null> {
    const response = await fetch(`/api/greetings/${id}`);
    if (response.status === 404) return null;
    if (!response.ok) throw new Error(`API error: ${response.status}`);
    return response.json();
}

export async function updateGreeting(id: string, req: UpdateGreetingRequest): Promise<UpdateGreetingResponse> {
    const response = await fetch(`/api/greetings/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(req),
    });
    if (!response.ok) throw new Error(`API error: ${response.status}`);
    return response.json();
}
