import React, { useState, useEffect } from 'react';
import api from '../services/api';
import logoLight from '../assets/Logo -light.png';
import logoDark from '../assets/logo-dark3.png'; // Usando a mesma logo dark do login
import { FiMail, FiLock, FiUser, FiSun, FiMoon } from 'react-icons/fi';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import '../i18n';

const Register = () => {
  const { t, i18n } = useTranslation();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const [isDarkMode, setIsDarkMode] = useState(() => localStorage.getItem('theme') === 'dark');

  const toggleTheme = () => setIsDarkMode(!isDarkMode);
  const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
    localStorage.setItem('lng', lng);
  };

  useEffect(() => {
    localStorage.setItem('theme', isDarkMode ? 'dark' : 'light');
    document.body.style.backgroundColor = isDarkMode ? '#0f172a' : '#ffffff';
  }, [isDarkMode]);

  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api.post('/auth/register', { username, email, password });
      setMessage(t('success_email'));
      setTimeout(() => window.location.href = `/verify?email=${email}`, 3000);
    } catch (err) {
      setMessage('Erro ao cadastrar. Verifique os dados.');
    } finally {
      setLoading(false);
    }
  };

  const theme = {
    bg: isDarkMode ? '#0f172a' : '#ffffff',
    card: isDarkMode ? '#1e293b' : '#ffffff',
    text: isDarkMode ? '#f8fafc' : '#1a1a1b',
    subtext: isDarkMode ? '#94a3b8' : '#6b7280',
    inputBg: isDarkMode ? '#334155' : '#374151',
  };

  return (
    <div style={{...styles.container, backgroundColor: theme.bg}}>
      <div style={styles.topBar}>
        <div style={styles.langButtons}>
          <button onClick={() => changeLanguage('pt')} style={styles.langBtn}>🇧🇷 PT</button>
          <button onClick={() => changeLanguage('en')} style={styles.langBtn}>🇺🇸 EN</button>
        </div>
        <button onClick={toggleTheme} style={styles.themeBtn}>
          {isDarkMode ? <FiSun color="#fbbf24" /> : <FiMoon color="#6d5dfc" />}
        </button>
      </div>

      <div style={{...styles.card, backgroundColor: theme.card, border: isDarkMode ? '1px solid #334155' : '1px solid #f0f0f0'}}>
        <div style={styles.logoContainer}>
          <img src={isDarkMode ? logoDark : logoLight} alt="PasSafe Logo" style={styles.logoImage} />
        </div>

        <form onSubmit={handleRegister} style={styles.form}>
          <div style={styles.inputWrapper}>
            <FiUser style={styles.icon} />
            <input
              type="text"
              placeholder="Nome de Usuário"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={{...styles.input, backgroundColor: theme.inputBg}}
              required
            />
          </div>

          <div style={styles.inputWrapper}>
            <FiMail style={styles.icon} />
            <input
              type="email"
              placeholder={t('email')}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={{...styles.input, backgroundColor: theme.inputBg}}
              required
            />
          </div>

          <div style={styles.inputWrapper}>
            <FiLock style={styles.icon} />
            <input
              type="password"
              placeholder={t('password')}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{...styles.input, backgroundColor: theme.inputBg}}
              required
            />
          </div>

          {message && <p style={{...styles.message, color: '#0d9488'}}>{message}</p>}

          <button type="submit" style={styles.button} disabled={loading}>
            {loading ? '...' : t('register_btn')}
          </button>
        </form>
        
        <div style={styles.footer}>
           <p style={{color: theme.subtext}}>
             {t('already_have_account')}{' '}
             <Link to="/login" style={styles.link}>Login</Link>
           </p>
        </div>
      </div>
    </div>
  );
};

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', width: '100%', position: 'absolute', top: 0, left: 0, fontFamily: "'Inter', sans-serif", transition: '0.3s' },
  topBar: { position: 'absolute', top: '20px', right: '30px', display: 'flex', gap: '15px', alignItems: 'center' },
  langButtons: { display: 'flex', gap: '10px' },
  langBtn: { background: 'none', border: 'none', cursor: 'pointer', fontSize: '14px', fontWeight: 'bold', color: '#6b7280' },
  themeBtn: { background: 'none', border: '1px solid #ddd', padding: '8px', borderRadius: '10px', cursor: 'pointer', display: 'flex' },
  card: { padding: '50px', borderRadius: '20px', boxShadow: '0 10px 50px rgba(0,0,0,0.1)', width: '90%', maxWidth: '480px', textAlign: 'center', transition: '0.3s' },
  logoContainer: { textAlign: 'center', marginBottom: '35px' },
  // TAMANHO UNIFICADO: 280px
  logoImage: { width: '280px', height: 'auto', display: 'block', marginLeft: 'auto', marginRight: 'auto', backgroundColor: 'transparent' },
  form: { display: 'flex', flexDirection: 'column', gap: '18px' },
  inputWrapper: { position: 'relative', display: 'flex', alignItems: 'center' },
  icon: { position: 'absolute', left: '15px', color: '#9ca3af', fontSize: '20px', zIndex: 10 }, 
  input: { width: '100%', padding: '14px 16px 14px 45px', borderRadius: '8px', border: 'none', color: '#ffffff', fontSize: '16px', outline: 'none' },
  button: { padding: '16px', backgroundImage:'linear-gradient(135deg, #0d9488 0%, #0f172a 100%)', color: '#ffffff', border: 'none', borderRadius: '8px', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer' },
  message: { textAlign: 'center', fontSize: '14px', margin: 0 },
  footer: { marginTop: '25px', textAlign: 'center', fontSize: '14px' },
  link: { color: '#0d9488', textDecoration: 'none', fontWeight: 'bold' }
};

export default Register;
