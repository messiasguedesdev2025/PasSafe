import React, { useState } from 'react';
import api from '../services/api';
import logo from '../assets/PasSafe.png';

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
          <p style={styles.subtitle}>LOGIN</p>
          <img src={logo} alt="PasSafe Logo" style={styles.logoImage} />

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
            <label style={styles.label}>Senha</label>
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
            {loading ? 'Entrando...' : 'Logar'}
          </button>
        </form>

        <div style={styles.footer}>
          <p>Não tem uma conta? <a href="/register" style={styles.link}>Criar conta</a></p>
        </div>
      </div>
    </div>
  );
};

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh',width: '100vh',height: '100vh', backgroundColor: '#ffffff', margin: 0, padding: 0, fontFamily: "'Inter', sans-serif" },
  card: { backgroundColor: '#ffffff', padding: '40px', borderRadius: '16px', boxShadow: '0 10px 40px rgba(0,0,0,0.1)', width: '100%', maxWidth: '400px',border: '1px solid #f0f0f0' },// Um contorno bem sutil para dar elegância
  logoContainer: { textAlign: 'center', marginBottom: '30px' },
  // Logo maior: aumentado para 250px
  logoImage: { width: '250px', height: 'auto', marginBottom: '5px' }, 
  subtitle: { color: '#6b7280', fontSize: '14px', marginTop: '0' },
  form: { display: 'flex', flexDirection: 'column', gap: '20px' },
  inputGroup: { display: 'flex', flexDirection: 'column', gap: '8px' },
  label: { fontSize: '14px', fontWeight: '600', color: '#374151' },
  input: { padding: '12px 16px', borderRadius: '8px', border: '1px solid #374151', backgroundColor: '#374151',color: '#ffffff', fontSize: '16px', outline: 'none' },
  button: { padding: '14px', backgroundImage:'linear-gradient(135deg, #0d9488 0%, #0f172a 100%)', color: '#ffffff', border: 'none', borderRadius: '8px', fontSize: '16px', fontWeight: '600', cursor: 'pointer', marginTop: '10px',transition: '0.3s' },
  errorBanner: { backgroundColor: '#fee2e2', color: '#dc2626', padding: '10px', borderRadius: '6px', fontSize: '14px', textAlign: 'center' },
  footer: { marginTop: '25px', textAlign: 'center', fontSize: '14px', color: '#6b7280' },
  link: { color: '#0d9488', // Cor esmeralda para combinar com o botão
    textDecoration: 'none',
    fontWeight: '600' }
};

export default Login;
