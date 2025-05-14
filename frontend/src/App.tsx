import React, { useState } from 'react';
import Auth from './Auth';
import KanbanBoard from './KanbanBoard';

const App: React.FC = () => {
    const [token, setToken] = useState<string | null>(null);
    const [username, setUsername] = useState<string | null>(null);

    const handleLogin = (token: string, username: string) => {
        setToken(token);
        setUsername(username);
    };

    return token && username ? (
        <KanbanBoard token={token} username={username} />
    ) : (
        <Auth onLogin={handleLogin} />
    );
};

export default App;
