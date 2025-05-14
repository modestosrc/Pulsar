export interface Card {
    id: string;
    content: string;
}

export interface Column {
    id: string;
    name: string;
    user: string;
    cards: Card[];
}
