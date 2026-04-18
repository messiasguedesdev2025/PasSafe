import React, { useState, useEffect } from 'react';
import api from '../services/api';
import logoLight from '../assets/PasSafe.png';
import logoDark from '../assets/logo-dark3.png';
import { FiSun, FiMoon } from 'react-icons/fi';
import { useTranslation } from 'react-i18next';
import '../i18n';

const Verify = () => {
  const { t, i18n } = useTranslation();
  const query = new URLSearchParams(window.location.search);
  const email = query.get('email');
  
  const [code, setCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  
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

  const handleVerify = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await api.post('/auth/verify', { email, code });
      setMessage(t('success_full'));
      setTimeout(() => window.location.href = '/login', 2000);
    } catch (err) {
      setMessage(t('error_code'));
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
        <img src={isDarkMode ? logoDark : logoLight} alt="PasSafe Logo" style={{...styles.logoImage, width: isDarkMode ? '280px' : '380px', backgroundColor: 'transparent'}} />
        
        <h2 style={{...styles.title, color: theme.text}}>{t('verify_email_title')}</h2>
        
        <p style={{...styles.subtitle, color: theme.subtext}}>
          {t('verify_email_sub')} <strong>{email}</strong>
        </p>

        <form onSubmit={handleVerify} style={styles.form}>
          <input
            type="text"
            placeholder="000000"
            maxLength={6}
            value={code}
            onChange={(e) => setCode(e.target.value)}
            style={{...styles.input, backgroundColor: theme.inputBg}}
            required
          />
          
          {message && <p style={{...styles.message, color: '#0d9488'}}>{message}</p>}

          <button type="submit" style={styles.button} disabled={loading}>
            {loading ? t('validating') : t('confirm_btn')}
          </button>
        </form>
      </div>
    </div>
  );
};

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', width: '100vw', position: 'absolute', top: 0, left: 0, fontFamily: "'Inter', sans-serif", transition: '0.3s' },
  topBar: { position: 'absolute', top: '20px', right: '30px', display: 'flex', gap: '15px', alignItems: 'center' },
  langButtons: { display: 'flex', gap: '10px' },
  langBtn: { background: 'none', border: 'none', cursor: 'pointer', fontSize: '14px', fontWeight: 'bold', color: '#6b7280' },
  themeBtn: { background: 'none', border: '1px solid #ddd', padding: '8px', borderRadius: '10px', cursor: 'pointer', display: 'flex' },
  card: { padding: '50px', borderRadius: '20px', boxShadow: '0 10px 50px rgba(0,0,0,0.1)', width: '90%', maxWidth: '480px', textAlign: 'center', transition: '0.3s' },
  logoImage: { height: 'auto', marginBottom: '20px', display: 'block', marginLeft: 'auto', marginRight: 'auto' },
  title: { fontSize: '22px', marginBottom: '10px' },
  subtitle: { fontSize: '14px', marginBottom: '25px' },
  form: { display: 'flex', flexDirection: 'column', gap: '20px' },
  input: { padding: '15px', borderRadius: '10px', border: 'none', color: '#ffffff', textAlign: 'center', fontSize: '28px', letterSpacing: '8px', outline: 'none' },
  button: { padding: '16px', backgroundImage:'linear-gradient(135deg, #0d9488 0%, #0f172a 100%)', color: '#ffffff', border: 'none', borderRadius: '8px', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer' },
  message: { fontSize: '14px', margin: '0' }
};

export default Verify;
