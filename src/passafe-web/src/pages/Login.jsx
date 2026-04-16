import React, { useState } from 'react';
import api from '../services/api';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
      const response = await api.post('/auth/login', { email, password });
      localStorage.setItem('token', response.data.token);
      window.location.href = '/dashboard';
    } catch (err) {
      setError('E-mail ou senha incorretos. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <div style={styles.logoContainer}>
          <h1 style={styles.logoText}>Pas<span>Safe</span></h1>
          <p style={styles.subtitle}>Seu cofre de senhas seguro</p>
        </div>

        <form onSubmit={handleLogin} style={styles.form}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>E-mail</label>
            <input
              type="email"
              placeholder="exemplo@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={styles.input}
              required
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Senha Mestra</label>
            <input
              type="password"
              placeholder="••••••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={styles.input}
              required
            />
          </div>

          {error && <div style={styles.errorBanner}>{error}</div>}

          <button 
            type="submit" 
            style={loading ? {...styles.button, opacity: 0.7} : styles.button}
            disabled={loading}
          >
            {loading ? 'Entrando...' : 'Acessar Cofre'}
          </button>
        </form>

        <div style={styles.footer}>
          <p>Não tem uma conta? <a href="/register" style={styles.link}>Criar conta</a></p>
          <a href="/forgot" style={styles.forgotLink}>Esqueceu a senha?</a>
        </div>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',
    backgroundColor: '#f4f7f9', // Fundo leve e profissional
    fontFamily: "'Inter', sans-serif",
  },
  card: {
    backgroundColor: '#ffffff',
    padding: '40px',
    borderRadius: '16px',
    boxShadow: '0 10px 25px rgba(0,0,0,0.05)',
    width: '100%',
    maxWidth: '400px',
  },
  logoContainer: {
    textAlign: 'center',
    marginBottom: '30px',
  },
  logoText: {
    fontSize: '28px',
    fontWeight: '800',
    color: '#1a1a1b',
    margin: 0,
    letterSpacing: '-1px',
  },
  subtitle: {
    color: '#6b7280',
    fontSize: '14px',
    marginTop: '5px',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '20px',
  },
  inputGroup: {
    display: 'flex',
    flexDirection: 'column',
    gap: '8px',
  },
  label: {
    fontSize: '14px',
    fontWeight: '600',
    color: '#374151',
  },
  input: {
    padding: '12px 16px',
    borderRadius: '8px',
    border: '1px solid #d1d5db',
    fontSize: '16px',
    outline: 'none',
    transition: 'border-color 0.2s',
  },
  button: {
    padding: '14px',
    backgroundColor: '#6d5dfc', // Roxo moderno
    color: '#ffffff',
    border: 'none',
    borderRadius: '8px',
    fontSize: '16px',
    fontWeight: '600',
    cursor: 'pointer',
    marginTop: '10px',
    transition: 'background-color 0.2s',
  },
  errorBanner: {
    backgroundColor: '#fee2e2',
    color: '#dc2626',
    padding: '10px',
    borderRadius: '6px',
    fontSize: '14px',
    textAlign: 'center',
  },
  footer: {
    marginTop: '25px',
    textAlign: 'center',
    fontSize: '14px',
    color: '#6b7280',
  },
  link: {
    color: '#6d5dfc',
    textDecoration: 'none',
    fontWeight: '600',
  },
  forgotLink: {
    display: 'block',
    marginTop: '10px',
    color: '#6b7280',
    textDecoration: 'none',
    fontSize: '13px',
  }
};

export default Login;
