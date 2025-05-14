import React, { useState, useEffect } from 'react';
import { Column } from './types';

interface KanbanBoardProps {
    token: string;
    username: string;
}

const KanbanBoard: React.FC<KanbanBoardProps> = ({ token, username }) => {
    const [columns, setColumns] = useState<Column[]>([]);

    useEffect(() => {
        fetch('http://localhost:8080/$username/columns', {
            headers: {
                Authorization: `Bearer ${token}`,
            },
            credentials: 'include',
        })
            .then((response) => response.json())
            .then((data: Column[]) => setColumns(data))
            .catch((error) => console.error('Error fetching columns:', error));
    }, [token, username]);

    return (
        <div>
            <h1>Kanban Board</h1>
            {columns.map((column) => (
                <div key={column.id}>
                    <h2>{column.name}</h2>
                    <ul>
                        {column.cards.map((card) => (
                            <li key={card.id}>{card.content}</li>
                        ))}
                    </ul>
                </div>
            ))}
        </div>
    );
}

export default KanbanBoard;
