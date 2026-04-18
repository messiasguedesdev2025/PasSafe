import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

const resources = {
  pt: {
    translation: {
      "login": "LOGIN",
      "register": "CADASTRO",
      "email": "E-mail",
      "password": "Senha",
      "phone": "Telefone",
      "stay_connected": "Mantenha-me conectado",
      "stay_connected_subtext": "Recomendado em dispositivos de confiança.",
      "create_account": "Criar Conta",
      "already_have_account": "Já possui uma conta?",
      "no_account": "Não tem uma conta?",
      "enter": "Logar",
      "register_btn": "Registrar",
      "verify_email_title": "Verificação de E-mail",
      "verify_sms_title": "Verificação de SMS",
      "verify_email_sub": "Digite o código de 6 dígitos enviado para",
      "verify_sms_sub": "Digite o PIN de 4 dígitos enviado para o seu celular",
      "confirm_btn": "Confirmar Código",
      "validating": "Validando...",
      "success_email": "E-mail verificado! Agora digite o código do SMS.",
      "success_full": "Tudo pronto! Sua conta está ativa.",
      "error_code": "Código inválido ou expirado.",
      "error_login_credentials": "E-mail ou senha incorretos."
    }
  },
  en: {
    translation: {
      "login": "LOGIN",
      "register": "REGISTER",
      "email": "Email",
      "password": "Password",
      "phone": "Phone Number",
      "stay_connected": "Keep me logged in",
      "stay_connected_subtext": "Recommended on trusted devices.",
      "create_account": "Create Account",
      "already_have_account": "Already have an account?",
      "no_account": "Don't have an account?",
      "enter": "Login",
      "register_btn": "Sign Up",
      "verify_email_title": "Email Verification",
      "verify_sms_title": "SMS Verification",
      "verify_email_sub": "Enter the 6-digit code sent to",
      "verify_sms_sub": "Enter the 4-digit PIN sent to your phone",
      "confirm_btn": "Confirm Code",
      "validating": "Validating...",
      "success_email": "Email verified! Now enter your SMS code.",
      "success_full": "All set! Your account is active.",
      "error_code": "Invalid or expired code.",
      "error_login_credentials": "Invalid email or password."
    }
  }
};

i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: localStorage.getItem('lng') || "pt",
    interpolation: {
      escapeValue: false
    }
  });

export default i18n;
