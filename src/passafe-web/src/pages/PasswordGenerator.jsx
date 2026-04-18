import React, { useState } from 'react';

const PasswordGenerator = () => {
  const [password, setPassword] = useState('');
  const [length, setLength] = useState(16);
  const [includeUppercase, setIncludeUppercase] = useState(true);
  const [includeLowercase, setIncludeLowercase] = useState(true);
  const [includeNumbers, setIncludeNumbers] = useState(true);
  const [includeSymbols, setIncludeSymbols] = useState(true);

  const generatePassword = () => {
    let charset = '';
    if (includeLowercase) charset += 'abcdefghijklmnopqrstuvwxyz';
    if (includeUppercase) charset += 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    if (includeNumbers) charset += '0123456789';
    if (includeSymbols) charset += '!@#$%^&*()-_+=[]{}|;:,.<>?';

    if (charset === '') {
      setPassword('Selecione pelo menos um tipo de caractere.');
      return;
    }

    let newPassword = '';
    for (let i = 0; i < length; i++) {
      newPassword += charset.charAt(Math.floor(Math.random() * charset.length));
    }
    setPassword(newPassword);
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(password);
    alert('Senha copiada para a área de transferência!');
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <button onClick={() => window.history.back()} style={styles.backBtn}>← Voltar</button>
        <h2 style={styles.title}>Gerador de Senhas Fortes</h2>

        <div style={styles.generatorControls}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Comprimento da Senha:</label>
            <input
              type="number"
              min="8"
              max="64"
              value={length}
              onChange={(e) => setLength(Math.max(8, Math.min(64, Number(e.target.value))))}
              style={styles.input}
            />
          </div>

          <div style={styles.checkboxGroup}>
            <label style={styles.checkboxLabel}>
              <input type="checkbox" checked={includeUppercase} onChange={() => setIncludeUppercase(!includeUppercase)} />
              Maiúsculas (A-Z)
            </label>
            <label style={styles.checkboxLabel}>
              <input type="checkbox" checked={includeLowercase} onChange={() => setIncludeLowercase(!includeLowercase)} />
              Minúsculas (a-z)
            </label>
            <label style={styles.checkboxLabel}>
              <input type="checkbox" checked={includeNumbers} onChange={() => setIncludeNumbers(!includeNumbers)} />
              Números (0-9)
            </label>
            <label style={styles.checkboxLabel}>
              <input type="checkbox" checked={includeSymbols} onChange={() => setIncludeSymbols(!includeSymbols)} />
              Símbolos (!@#$%)
            </label>
          </div>

          <button onClick={generatePassword} style={styles.generateBtn}>Gerar Senha</button>
        </div>

        {password && (
          <div style={styles.passwordDisplay}>
            <input type="text" value={password} readOnly style={styles.generatedPasswordInput} />
            <button onClick={copyToClipboard} style={styles.copyBtn}>Copiar</button>
          </div>
        )}
      </div>
    </div>
  );
};

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', backgroundColor: '#f4f7f9', fontFamily: "'Inter', sans-serif" },
  card: { backgroundColor: 'white', padding: '30px', borderRadius: '16px', boxShadow: '0 10px 25px rgba(0,0,0,0.05)', width: '100%', maxWidth: '500px' },
  backBtn: { backgroundColor: 'transparent', border: 'none', color: '#6b7280', cursor: 'pointer', marginBottom: '10px', fontSize: '14px' },
  title: { color: '#1a1a1b', marginBottom: '25px', textAlign: 'center' },
  generatorControls: { marginBottom: '20px', display: 'flex', flexDirection: 'column', gap: '15px' },
  inputGroup: { display: 'flex', flexDirection: 'column', gap: '5px' },
  label: { fontSize: '13px', fontWeight: '600', color: '#374151' },
  input: { padding: '10px', borderRadius: '8px', border: '1px solid #d1d5db', outline: 'none', width: '80px' },
  checkboxGroup: { display: 'flex', flexDirection: 'column', gap: '8px' },
  checkboxLabel: { fontSize: '14px', color: '#374151', display: 'flex', alignItems: 'center', gap: '8px' },
  generateBtn: { padding: '12px', backgroundColor: '#6d5dfc', color: 'white', border: 'none', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', marginTop: '10px' },
  passwordDisplay: { display: 'flex', gap: '10px', marginTop: '20px', alignItems: 'center' },
  generatedPasswordInput: { flex: 1, padding: '12px', borderRadius: '8px', border: '1px solid #d1d5db', outline: 'none', backgroundColor: '#e9ecef', fontSize: '16px', fontWeight: 'bold' },
  copyBtn: { padding: '10px 15px', backgroundColor: '#f3f4f6', border: '1px solid #d1d5db', borderRadius: '8px', cursor: 'pointer', fontSize: '14px', fontWeight: '600' }
};

export default PasswordGenerator;
