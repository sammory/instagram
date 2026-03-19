import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../../api/auth';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setErrorMsg('');

    if (!email || !password) {
      setErrorMsg('이메일과 비밀번호를 입력해주세요.');
      return;
    }

    setIsLoading(true);

    try {
      const response = await login({ email, password });

      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('username', response.username);
      localStorage.setItem('name', response.name);

      navigate('/');
    } catch (error) {
      setErrorMsg(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <h1 style={styles.logo}>Instagram</h1>

        <form onSubmit={handleLogin}>
          <input
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            style={styles.input}
            disabled={isLoading}
          />
          <input
            type="password"
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={styles.input}
            disabled={isLoading}
          />

          {errorMsg && <p style={styles.error}>{errorMsg}</p>}

          <button type="submit" style={styles.button} disabled={isLoading}>
            {isLoading ? '로그인 중...' : '로그인'}
          </button>
        </form>
      </div>

      <div style={styles.registerCard}>
        <p style={styles.registerText}>
          계정이 없으신가요?{' '}
          <Link to="/register" style={styles.link}>
            가입하기
          </Link>
        </p>
      </div>
    </div>
  );
};

const styles = {
  page: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: '100vh',
    background: '#fafafa',
  },
  card: {
    width: '350px',
    background: '#fff',
    border: '1px solid #dbdbdb',
    borderRadius: '4px',
    padding: '40px',
    textAlign: 'center',
  },
  logo: {
    fontFamily: 'cursive',
    fontSize: '36px',
    fontWeight: 'bold',
    marginBottom: '24px',
    letterSpacing: '-1px',
  },
  input: {
    display: 'block',
    width: '100%',
    padding: '9px 8px',
    marginBottom: '6px',
    background: '#fafafa',
    border: '1px solid #dbdbdb',
    borderRadius: '3px',
    fontSize: '14px',
    boxSizing: 'border-box',
    outline: 'none',
  },
  error: {
    color: '#ed4956',
    fontSize: '13px',
    marginBottom: '8px',
  },
  button: {
    width: '100%',
    padding: '8px',
    marginTop: '8px',
    background: '#0095f6',
    color: '#fff',
    border: 'none',
    borderRadius: '8px',
    fontSize: '14px',
    fontWeight: '600',
    cursor: 'pointer',
  },
  registerCard: {
    width: '350px',
    background: '#fff',
    border: '1px solid #dbdbdb',
    borderRadius: '4px',
    padding: '20px',
    textAlign: 'center',
    marginTop: '10px',
  },
  registerText: {
    fontSize: '14px',
    color: '#262626',
    margin: 0,
  },
  link: {
    color: '#0095f6',
    fontWeight: '600',
    textDecoration: 'none',
  },
};

export default Login;
