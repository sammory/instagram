import axios from 'axios';

const API_BASE_URL = 'http://localhost:9090/api/user';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true, // httpOnly 쿠키 전송을 위해 필요
});

// ─── Request Interceptor ───────────────────────────────────────────────────────
// 모든 요청에 Access Token을 Authorization 헤더로 자동 첨부
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ─── Response Interceptor ─────────────────────────────────────────────────────
// 401 응답 시 Refresh Token으로 Access Token 재발급 후 요청 재시도
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) prom.reject(error);
    else prom.resolve(token);
  });
  failedQueue = [];
};

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        // 이미 갱신 중이면 대기열에 추가
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then((token) => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return api(originalRequest);
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const res = await axios.post(
          `${API_BASE_URL}/refresh`,
          {},
          { withCredentials: true }
        );
        const { accessToken } = res.data;
        localStorage.setItem('accessToken', accessToken);
        processQueue(null, accessToken);
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError, null);
        localStorage.removeItem('accessToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

// ─── API 함수 ─────────────────────────────────────────────────────────────────

export const signup = async (signupData) => {
  try {
    const response = await api.post('/signup', signupData);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.error || '회원가입에 실패했습니다.');
    } else if (error.request) {
      throw new Error('서버와 연결할 수 없습니다.');
    }
    throw new Error('회원가입 요청 중 오류가 발생했습니다.');
  }
};

export const login = async (loginData) => {
  try {
    const response = await api.post('/login', loginData);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error('이메일 또는 비밀번호가 올바르지 않습니다.');
    } else if (error.request) {
      throw new Error('서버와 연결할 수 없습니다.');
    }
    throw new Error('로그인 요청 중 오류가 발생했습니다.');
  }
};

export const logout = async () => {
  try {
    await api.post('/logout');
  } finally {
    localStorage.removeItem('accessToken');
  }
};
