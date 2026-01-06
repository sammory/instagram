import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom"; // 페이지 이동을 위한 useNavigate 추가

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = (e) => {
    e.preventDefault();
    console.log("로그인 시도:", { username, password });
  };

  return (
    <div style={styles.container}>
      <h2>로그인</h2>
      <form>
        <input
          type="text"
          placeholder="아이디"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          style={styles.input}
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={styles.input}
        />
        <button type="submit" style={styles.button}>로그인</button>
      </form>

      {/* 기존 <a> 태그 대신 <Link> 사용 */}
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
