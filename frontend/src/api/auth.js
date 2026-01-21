import axios from 'axios';

const API_BASE_URL = 'http://localhost:9090/api/user';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 회원가입 API
export const signup = async (signupData) => {
  try {
    const response = await api.post('/signup', signupData);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.error || '회원가입에 실패했습니다.');
    } else if (error.request) {
      throw new Error('서버와 연결할 수 없습니다.');
    } else {
      throw new Error('회원가입 요청 중 오류가 발생했습니다.');
    }
  }
};

// 로그인 API
export const login = async (loginData) => {
  try {
    const response = await api.post('/login', loginData);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.error || '로그인에 실패했습니다.');
    } else if (error.request) {
      throw new Error('서버와 연결할 수 없습니다.');
    } else {
      throw new Error('로그인 요청 중 오류가 발생했습니다.');
    }
  }
};