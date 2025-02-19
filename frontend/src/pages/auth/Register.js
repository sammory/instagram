import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom"; // 페이지 이동을 위한 useNavigate 추가

const Register = () => {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const navigate = useNavigate(); // 페이지 이동을 위한 훅

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = (e) => {
    e.preventDefault();
    if (form.password !== form.confirmPassword) {
      alert("비밀번호가 일치하지 않습니다!");
      return;
    }
    console.log("회원가입 시도:", form);
    navigate("/login"); // 회원가입 후 로그인 페이지로 이동
  };

  const handleCancel = () => {
    navigate("/login"); // 취소 시 로그인 페이지로 이동
  };

  return (
    <div style={styles.container}>
      <h2>회원가입</h2>
      <form onSubmit={handleRegister}>
        <input
          type="text"
          name="username"
          placeholder="아이디"
          value={form.username}
          onChange={handleChange}
          style={styles.input}
        />
        <input
          type="email"
          name="email"
          placeholder="이메일"
          value={form.email}
          onChange={handleChange}
          style={styles.input}
        />
        <input
          type="password"
          name="password"
          placeholder="비밀번호"
          value={form.password}
          onChange={handleChange}
          style={styles.input}
        />
        <input
          type="password"
          name="confirmPassword"
          placeholder="비밀번호 확인"
          value={form.confirmPassword}
          onChange={handleChange}
          style={styles.input}
        />
        <div style={styles.buttonContainer}>
          <button type="submit" style={styles.submitButton}>가입</button>
          <button type="button" style={styles.cancelButton} onClick={handleCancel}>취소</button>
        </div>
      </form>

      {/* 기존 <a> 태그 대신 <Link> 사용 */}
      <p>
        이미 계정이 있으신가요? <Link to="/login">로그인</Link>
      </p>
    </div>
  );
};

const buttonStyle = { 
    padding: "10px 20px", 
    cursor: "pointer",
    width: "80px",  
    textAlign: "center",
    display: "inline-block" 
  };
  
  const styles = {
    container: { textAlign: "center", marginTop: "50px" },
    input: { display: "block", margin: "10px auto", padding: "10px", width: "250px" },
    buttonContainer: { display: "flex", justifyContent: "center", gap: "10px", marginTop: "10px" },
    submitButton: { ...buttonStyle, backgroundColor: "#4CAF50", color: "white" }, // 가입 버튼
    cancelButton: { ...buttonStyle, backgroundColor: "#f44336", color: "white" }, // 취소 버튼
  };

export default Register;
