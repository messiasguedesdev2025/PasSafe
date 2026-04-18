import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register'; // Importando a tela de cadastro
import Verify from './pages/Verify';
import Dashboard from './pages/Dashboard';
import AddSecret from './pages/AddSecret';
import PasswordGenerator from './pages/PasswordGenerator';

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
        
        {/* CORREÇÃO: Habilitando a rota de Cadastro */}
        <Route path="/register" element={<Register />} />
        
        {/* Rota de Verificação */}
        <Route path="/verify" element={<Verify />} />

        {/* Rotas Protegidas */}
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
        <Route 
          path="/generator" 
          element={
            <PrivateRoute>
              <PasswordGenerator />
            </PrivateRoute>
          } 
        />

        {/* Redireciona para o login se a rota não for encontrada */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
