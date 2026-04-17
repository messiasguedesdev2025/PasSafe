import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import AddSecret from './pages/AddSecret';

// Componente para proteger rotas que exigem autenticação
const PrivateRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  return token ? children : <Navigate to="/login" />;
};

function App() {
  return (
    <Router>
      <Routes>
        {/* Rota de Login */}
        <Route path="/login" element={<Login />} />
        
        {/* Rota de Cadastro (se você criar uma) */}
        {/* <Route path="/register" element={<Register />} /> */}

        {/* Rotas Protegidas (exigem Token JWT) */}
        <Route 
          path="/dashboard" 
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          } 
        />
        <Route 
          path="/add" 
          element={
            <PrivateRoute>
              <AddSecret />
            </PrivateRoute>
          } 
        />

        {/* Redireciona para o login se a rota não for encontrada ou for a raiz */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
