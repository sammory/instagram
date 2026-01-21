import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { login } from "../../api/auth";  // API 함수 import

const Login = () => {
  const [email, setEmail] = useState("");  // username → email 변경
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);  // 로딩 상태

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    
    if (!email || !password) {
      alert("이메일과 비밀번호를 입력해주세요!");
      return;
    }

    setIsLoading(true);

    try {
      // 로그인 API 호출
      const loginData = {
        email: email,
        password: password
      };

      const response = await login(loginData);
      
      console.log("로그인 성공:", response);
      
      // JWT 토큰 저장 (나중에 사용)
      if (response.accessToken) {
        localStorage.setItem('accessToken', response.accessToken);
      }
      
      alert("로그인 성공!");
      navigate("/");  // 홈으로 이동

    } catch (error) {
      console.error("로그인 실패:", error);
      alert(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <h2>로그인</h2>
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
        <button 
          type="submit" 
          style={styles.button}
          disabled={isLoading}
        >
          {isLoading ? "로그인 중..." : "로그인"}
        </button>
      </form>

      <p>
        계정이 없으신가요? <Link to="/register">회원가입</Link>
      </p>
    </div>
  );
};

const styles = {
  container: { textAlign: "center", marginTop: "50px" },
  input: { display: "block", margin: "10px auto", padding: "10px", width: "250px" },
  button: { padding: "10px 20px", cursor: "pointer" },
};

export default Login;