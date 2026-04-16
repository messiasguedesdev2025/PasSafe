import React, { useEffect, useState } from 'react';
import api from '../services/api';

const Dashboard = () => {
  const [secrets, setSecrets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  // Busca as senhas do seu servidor Java assim que a tela abre
  useEffect(() => {
    const fetchSecrets = async () => {
      try {
        const response = await api.get('/secrets'); // O Token JWT já vai no Header pelo api.js
        setSecrets(response.data);
      } catch (err) {
        console.error('Erro ao buscar senhas:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchSecrets();
  }, []);

  const handleCopy = (text) => {
    navigator.clipboard.writeText(text);
    alert('Senha copiada para a área de transferência!');
  };

  const filteredSecrets = secrets.filter(s => 
    s.title.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div style={styles.container}>
      <header style={styles.header}>
        <h1 style={styles.logoText}>Pas<span>Safe</span></h1>
        <button style={styles.logoutBtn} onClick={() => { localStorage.clear(); window.location.href = '/login'; }}>Sair</button>
      </header>

      <main style={styles.main}>
        <div style={styles.searchBar}>
          <input 
            type="text" 
            placeholder="Buscar senhas (ex: GitHub, Google...)" 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            style={styles.searchInput}
          />
          <button style={styles.addBtn} onClick={() => window.location.href = '/add'}>+ Novo Item</button>
        </div>

        {loading ? (
          <p style={styles.loading}>Carregando seu cofre...</p>
        ) : (
          <div style={styles.list}>
            {filteredSecrets.length > 0 ? (
              filteredSecrets.map((secret) => (
                <div key={secret.id} style={styles.card}>
                  <div style={styles.cardInfo}>
                    <h3 style={styles.cardTitle}>{secret.title}</h3>
                    <p style={styles.cardUser}>{secret.usernameInSite}</p>
                    <p style={styles.cardUrl}>{secret.siteUrl}</p>
                  </div>
                  <button style={styles.copyBtn} onClick={() => handleCopy(secret.password)}>Copiar Senha</button>
                </div>
              ))
            ) : (
              <p style={styles.empty}>Nenhum segredo encontrado.</p>
            )}
          </div>
        )}
      </main>
    </div>
  );
};

const styles = {
  container: { backgroundColor: '#f4f7f9', minHeight: '100vh', fontFamily: "'Inter', sans-serif" },
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '20px 50px', backgroundColor: 'white', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' },
  logoText: { fontSize: '24px', fontWeight: '800', color: '#1a1a1b' },
  logoutBtn: { backgroundColor: 'transparent', border: 'none', color: '#6b7280', cursor: 'pointer', fontWeight: '600' },
  main: { maxWidth: '800px', margin: '40px auto', padding: '0 20px' },
  searchBar: { display: 'flex', gap: '15px', marginBottom: '30px' },
  searchInput: { flex: 1, padding: '12px 20px', borderRadius: '10px', border: '1px solid #d1d5db', fontSize: '16px', outline: 'none' },
  addBtn: { backgroundColor: '#6d5dfc', color: 'white', border: 'none', borderRadius: '10px', padding: '0 20px', cursor: 'pointer', fontWeight: '600' },
  list: { display: 'flex', flexDirection: 'column', gap: '15px' },
  card: { backgroundColor: 'white', padding: '20px', borderRadius: '12px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', boxShadow: '0 4px 12px rgba(0,0,0,0.03)' },
  cardInfo: { display: 'flex', flexDirection: 'column', gap: '4px' },
  cardTitle: { margin: 0, fontSize: '18px', color: '#1a1a1b' },
  cardUser: { margin: 0, fontSize: '14px', color: '#6b7280' },
  cardUrl: { margin: 0, fontSize: '12px', color: '#9ca3af' },
  copyBtn: { backgroundColor: '#f3f4f6', border: 'none', padding: '10px 15px', borderRadius: '8px', color: '#374151', cursor: 'pointer', fontWeight: '600' },
  loading: { textAlign: 'center', color: '#6b7280', marginTop: '40px' },
  empty: { textAlign: 'center', color: '#9ca3af', marginTop: '40px' }
};

export default Dashboard;
