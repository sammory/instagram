import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../../api/auth";

const Register = () => {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    name: "",  // ← 추가!
  });

  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    
    console.log("회원가입 시도:", form);
    
    // 비밀번호 확인
    if (form.password !== form.confirmPassword) {
      alert("비밀번호가 일치하지 않습니다!");
      return;
    }

    // 필수값 확인
    if (!form.username || !form.email || !form.password || !form.name) {
      alert("모든 필수 항목을 입력해주세요!");
      return;
    }

    setIsLoading(true);

    try {
      const signupData = {
        username: form.username,
        email: form.email,
        password: form.password,
        name: form.name,  // ← name 포함
      };

      console.log("전송 데이터:", signupData);

      const response = await signup(signupData);
      
      console.log("회원가입 성공:", response);
      
      alert(`${response.name}님, 회원가입이 완료되었습니다!`);
      navigate("/login");

    } catch (error) {
      console.error("회원가입 실패:", error);
      alert(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    navigate("/login");
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
          disabled={isLoading}
        />
        <input
          type="email"
          name="email"
          placeholder="이메일"
          value={form.email}
          onChange={handleChange}
          style={styles.input}
          disabled={isLoading}
        />
        
        {/* 이름 입력 필드 추가! */}
        <input
          type="text"
          name="name"
          placeholder="이름"
          value={form.name}
          onChange={handleChange}
          style={styles.input}
          disabled={isLoading}
        />
        
        <input
          type="password"
          name="password"
          placeholder="비밀번호 (8자 이상)"
          value={form.password}
          onChange={handleChange}
          style={styles.input}
          disabled={isLoading}
        />
        <input
          type="password"
          name="confirmPassword"
          placeholder="비밀번호 확인"
          value={form.confirmPassword}
          onChange={handleChange}
          style={styles.input}
          disabled={isLoading}
        />
        
        <div style={styles.buttonContainer}>
          <button 
            type="submit" 
            style={styles.submitButton}
            disabled={isLoading}
          >
            {isLoading ? "처리중..." : "가입"}
          </button>
          <button 
            type="button" 
            style={styles.cancelButton} 
            onClick={handleCancel}
            disabled={isLoading}
          >
            취소
          </button>
        </div>
      </form>

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
  input: { 
    display: "block", 
    margin: "10px auto", 
    padding: "10px", 
    width: "250px",
    boxSizing: "border-box"
  },
  buttonContainer: { 
    display: "flex", 
    justifyContent: "center", 
    gap: "10px", 
    marginTop: "10px" 
  },
  submitButton: { ...buttonStyle, backgroundColor: "#4CAF50", color: "white" },
  cancelButton: { ...buttonStyle, backgroundColor: "#f44336", color: "white" },
};

export default Register;