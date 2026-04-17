import React, { useState } from 'react';
import api from '../services/api';

const AddSecret = () => {
  const [formData, setFormData] = useState({
    title: '',
    siteUrl: '',
    usernameInSite: '',
    password: ''
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const generateStrongPassword = () => {
    const chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
    let password = "";
    for (let i = 0; i < 16; i++) {
      password += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    setFormData({ ...formData, password });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api.post('/secrets', formData);
      setMessage('Senha salva com sucesso no cofre!');
      setTimeout(() => window.location.href = '/dashboard', 2000);
    } catch (err) {
      setMessage('Erro ao salvar. Verifique se você está logado.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <button onClick={() => window.location.href = '/dashboard'} style={styles.backBtn}>← Voltar</button>
        <h2 style={styles.title}>Novo Item no Cofre</h2>
        
        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Título (ex: GitHub)</label>
            <input 
              type="text" 
              style={styles.input}
              value={formData.title}
              onChange={(e) => setFormData({...formData, title: e.target.value})}
              required
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>URL do Site</label>
            <input 
              type="text" 
              style={styles.input}
              value={formData.siteUrl}
              onChange={(e) => setFormData({...formData, siteUrl: e.target.value})}
              required
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Usuário/E-mail no site</label>
            <input 
              type="text" 
              style={styles.input}
              value={formData.usernameInSite}
              onChange={(e) => setFormData({...formData, usernameInSite: e.target.value})}
              required
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Senha</label>
            <div style={styles.passWrapper}>
              <input 
                type="text" 
                style={styles.inputPass}
                value={formData.password}
                onChange={(e) => setFormData({...formData, password: e.target.value})}
                required
              />
              <button type="button" onClick={generateStrongPassword} style={styles.genBtn}>Gerar</button>
            </div>
          </div>

          {message && <p style={styles.message}>{message}</p>}

          <button type="submit" style={styles.saveBtn} disabled={loading}>
            {loading ? 'Salvando...' : 'Salvar no PasSafe'}
          </button>
        </form>
      </div>
    </div>
  );
};

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#f4f7f9', fontFamily: "'Inter', sans-serif" },
  card: { backgroundColor: 'white', padding: '30px', borderRadius: '16px', boxShadow: '0 10px 25px rgba(0,0,0,0.05)', width: '100%', maxWidth: '450px' },
  backBtn: { backgroundColor: 'transparent', border: 'none', color: '#6b7280', cursor: 'pointer', marginBottom: '10px', fontSize: '14px' },
  title: { color: '#1a1a1b', marginBottom: '25px', textAlign: 'center' },
  form: { display: 'flex', flexDirection: 'column', gap: '15px' },
  inputGroup: { display: 'flex', flexDirection: 'column', gap: '5px' },
  label: { fontSize: '13px', fontWeight: '600', color: '#374151' },
  input: { padding: '12px', borderRadius: '8px', border: '1px solid #d1d5db', outline: 'none' },
  passWrapper: { display: 'flex', gap: '10px' },
  inputPass: { flex: 1, padding: '12px', borderRadius: '8px', border: '1px solid #d1d5db', outline: 'none' },
  genBtn: { padding: '0 15px', backgroundColor: '#f3f4f6', border: '1px solid #d1d5db', borderRadius: '8px', cursor: 'pointer', fontSize: '12px' },
  saveBtn: { padding: '14px', backgroundColor: '#6d5dfc', color: 'white', border: 'none', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', marginTop: '10px' },
  message: { textAlign: 'center', fontSize: '14px', color: '#059669', margin: 0 }
};

export default AddSecret;
