import React, { useState, useEffect } from 'react';
import api from '../services/api';
import logoLight from '../assets/PasSafe.png';
import logoDark from '../assets/logo-dark2.png';
import { FiMail, FiLock, FiSun, FiMoon } from 'react-icons/fi';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import '../i18n';

const Login = () => {
  const { t, i18n } = useTranslation();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [stayConnected, setStayConnected] = useState(false);
  const [error, setError] = useState('');
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

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const response = await api.post('/auth/login', { email, password });
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('stayConnected', stayConnected);
      window.location.href = '/dashboard';
    } catch (err) {
      setError(t('error_code'));
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
    inputBorder: isDarkMode ? '#475569' : '#374151',
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
          {/* TEXTO LOGIN REMOVIDO */}
          <img 
            src={isDarkMode ? logoDark : logoLight} 
            alt="PasSafe Logo" 
            style={{...styles.logoImage, backgroundColor: 'transparent'}} 
          />
        </div>

        <form onSubmit={handleLogin} style={styles.form}>
          <div style={styles.inputWrapper}>
            <FiMail style={styles.icon} />
            <input
              type="email"
              placeholder={t('email')}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              style={{...styles.input, backgroundColor: theme.inputBg, borderColor: theme.inputBorder}}
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
              style={{...styles.input, backgroundColor: theme.inputBg, borderColor: theme.inputBorder}}
              required
            />
          </div>

          <div style={styles.stayConnectedWrapper}>
            <input 
              type="checkbox" 
              id="stayConnected" 
              checked={stayConnected}
              onChange={(e) => setStayConnected(e.target.checked)}
              style={styles.checkbox}
            />
            <div style={styles.checkboxTexts}>
              <label htmlFor="stayConnected" style={{...styles.checkboxLabel, color: theme.text}}>{t('stay_connected')}</label>
              <span style={styles.checkboxSubtext}>Recomendado em dispositivos de confiança.</span>
            </div>
          </div>

          {error && <div style={styles.errorBanner}>{error}</div>}

          <button type="submit" style={loading ? {...styles.button, opacity: 0.7} : styles.button} disabled={loading}>
            {loading ? t('validating') : t('enter')}
          </button>
        </form>

        <div style={styles.footer}>
          <p style={{color: theme.subtext}}>
            {t('no_account')}{' '}
            <Link to="/register" style={styles.link}>{t('create_account')}</Link>
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
  themeBtn: { background: 'none', border: '1px solid #ddd', padding: '8px', borderRadius: '10px', cursor: 'pointer', display: 'flex', alignItems: 'center' },
  card: { padding: '50px', borderRadius: '20px', boxShadow: '0 10px 50px rgba(0,0,0,0.1)', width: '90%', maxWidth: '480px', zIndex: 10, transition: '0.3s' },
  logoContainer: { textAlign: 'center', marginBottom: '35px' },
  logoImage: { width: '250px', height: 'auto', marginBottom: '5px', display: 'block', marginLeft: 'auto', marginRight: 'auto' },
  form: { display: 'flex', flexDirection: 'column', gap: '22px' },
  inputWrapper: { position: 'relative', display: 'flex', alignItems: 'center' },
  icon: { position: 'absolute', left: '18px', color: '#9ca3af', fontSize: '22px' }, 
  input: { width: '100%', padding: '14px 16px 14px 55px', borderRadius: '10px', border: '1px solid', color: '#ffffff', fontSize: '18px', outline: 'none' },
  stayConnectedWrapper: { display: 'flex', alignItems: 'flex-start', gap: '12px', marginTop: '-5px' },
  checkbox: { cursor: 'pointer', width: '18px', height: '18px', marginTop: '3px', accentColor: '#0d9488' },
  checkboxTexts: { display: 'flex', flexDirection: 'column' },
  checkboxLabel: { fontSize: '15px', fontWeight: '600', cursor: 'pointer' },
  checkboxSubtext: { fontSize: '12px', color: '#9ca3af' },
  button: { padding: '16px', backgroundImage:'linear-gradient(135deg, #0d9488 0%, #0f172a 100%)', color: '#ffffff', border: 'none', borderRadius: '10px', fontSize: '18px', fontWeight: '700', cursor: 'pointer', marginTop: '10px' },
  errorBanner: { backgroundColor: '#fee2e2', color: '#dc2626', padding: '10px', borderRadius: '6px', fontSize: '14px', textAlign: 'center' },
  footer: { marginTop: '30px', textAlign: 'center', fontSize: '15px' },
  link: { color: '#0d9488', textDecoration: 'none', fontWeight: '700' }
};

export default Login;
