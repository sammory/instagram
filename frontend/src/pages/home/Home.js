import React, { useState } from 'react';
import Navbar from '../../components/layout/Navbar';
import './Home.css';

// ─── 더미 데이터 ────────────────────────────────────────────────────────────────
const DUMMY_STORIES = [
  { id: 1, username: 'my_story', avatar: 'https://i.pravatar.cc/56?img=1', isMe: true },
  { id: 2, username: 'travel_kr', avatar: 'https://i.pravatar.cc/56?img=2' },
  { id: 3, username: 'food_lover', avatar: 'https://i.pravatar.cc/56?img=3' },
  { id: 4, username: 'daily_pic', avatar: 'https://i.pravatar.cc/56?img=4' },
  { id: 5, username: 'sunset_kr', avatar: 'https://i.pravatar.cc/56?img=5' },
  { id: 6, username: 'photo_lab', avatar: 'https://i.pravatar.cc/56?img=6' },
  { id: 7, username: 'city_walk', avatar: 'https://i.pravatar.cc/56?img=7' },
];

const DUMMY_POSTS = [
  {
    id: 1,
    username: 'travel_kr',
    avatar: 'https://i.pravatar.cc/40?img=2',
    image: 'https://picsum.photos/468/468?random=10',
    likes: 1243,
    caption: '서울의 아름다운 야경 ✨ 오늘도 좋은 하루!',
    timeAgo: '2시간 전',
    liked: false,
    saved: false,
  },
  {
    id: 2,
    username: 'food_lover',
    avatar: 'https://i.pravatar.cc/40?img=3',
    image: 'https://picsum.photos/468/468?random=20',
    likes: 842,
    caption: '오늘 점심은 파스타 🍝 너무 맛있었다!',
    timeAgo: '4시간 전',
    liked: false,
    saved: false,
  },
  {
    id: 3,
    username: 'daily_pic',
    avatar: 'https://i.pravatar.cc/40?img=4',
    image: 'https://picsum.photos/468/468?random=30',
    likes: 3102,
    caption: '제주도 한 달 살기 D-7 🌊 기대돼',
    timeAgo: '6시간 전',
    liked: false,
    saved: false,
  },
];

const DUMMY_SUGGESTIONS = [
  { id: 1, username: 'photo_lab', name: '사진작가 김민수', avatar: 'https://i.pravatar.cc/32?img=6' },
  { id: 2, username: 'city_walk', name: '도시탐험가', avatar: 'https://i.pravatar.cc/32?img=7' },
  { id: 3, username: 'nature_kr', name: '자연사랑', avatar: 'https://i.pravatar.cc/32?img=8' },
  { id: 4, username: 'art_daily', name: '일러스트레이터', avatar: 'https://i.pravatar.cc/32?img=9' },
  { id: 5, username: 'cafe_tour', name: '카페투어러', avatar: 'https://i.pravatar.cc/32?img=10' },
];

// ─── 컴포넌트 ──────────────────────────────────────────────────────────────────
const Home = () => {
  const [posts, setPosts] = useState(DUMMY_POSTS);
  const currentUser = {
    username: localStorage.getItem('username') || 'me',
    name: localStorage.getItem('name') || '내 계정',
    avatar: 'https://i.pravatar.cc/56?img=1',
  };

  const toggleLike = (postId) => {
    setPosts((prev) =>
      prev.map((p) =>
        p.id === postId
          ? { ...p, liked: !p.liked, likes: p.liked ? p.likes - 1 : p.likes + 1 }
          : p
      )
    );
  };

  const toggleSave = (postId) => {
    setPosts((prev) =>
      prev.map((p) => (p.id === postId ? { ...p, saved: !p.saved } : p))
    );
  };

  return (
    <div className="home-wrapper">
      <Navbar />

      <div className="home-container">
        {/* ── 피드 영역 ── */}
        <main className="feed-area">
          {/* 스토리 */}
          <div className="stories-card">
            {DUMMY_STORIES.map((story) => (
              <div className="story-item" key={story.id}>
                <div className={`story-ring ${story.isMe ? 'story-ring--me' : ''}`}>
                  <img src={story.avatar} alt={story.username} />
                </div>
                <span>{story.isMe ? '내 스토리' : story.username}</span>
              </div>
            ))}
          </div>

          {/* 게시물 목록 */}
          {posts.map((post) => (
            <article className="post-card" key={post.id}>
              {/* 헤더 */}
              <div className="post-header">
                <img src={post.avatar} alt={post.username} className="post-avatar" />
                <span className="post-username">{post.username}</span>
                <button className="more-btn">···</button>
              </div>

              {/* 이미지 */}
              <img
                src={post.image}
                alt="post"
                className="post-image"
                onDoubleClick={() => toggleLike(post.id)}
              />

              {/* 액션 버튼 */}
              <div className="post-actions">
                <div className="post-actions-left">
                  <button className="action-btn" onClick={() => toggleLike(post.id)}>
                    {post.liked ? (
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="#ed4956">
                        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
                      </svg>
                    ) : (
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                        <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
                      </svg>
                    )}
                  </button>
                  <button className="action-btn">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                      <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
                    </svg>
                  </button>
                  <button className="action-btn">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                      <line x1="22" y1="2" x2="11" y2="13" />
                      <polygon points="22 2 15 22 11 13 2 9 22 2" />
                    </svg>
                  </button>
                </div>
                <button className="action-btn" onClick={() => toggleSave(post.id)}>
                  {post.saved ? (
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                      <path d="M17 3H7a2 2 0 0 0-2 2v16l7-3 7 3V5a2 2 0 0 0-2-2z" />
                    </svg>
                  ) : (
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                      <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z" />
                    </svg>
                  )}
                </button>
              </div>

              <div className="post-likes">좋아요 {post.likes.toLocaleString()}개</div>
              <div className="post-caption">
                <span className="post-caption-username">{post.username}</span>{' '}
                {post.caption}
              </div>
              <div className="post-time">{post.timeAgo}</div>
            </article>
          ))}
        </main>

        {/* ── 사이드바 ── */}
        <aside className="sidebar">
          <div className="sidebar-profile">
            <img src={currentUser.avatar} alt="me" className="sidebar-avatar" />
            <div className="sidebar-user-info">
              <span className="sidebar-username">{currentUser.username}</span>
              <span className="sidebar-name">{currentUser.name}</span>
            </div>
          </div>

          <div className="suggestions-header">
            <span>회원님을 위한 추천</span>
            <button className="see-all-btn">모두 보기</button>
          </div>

          {DUMMY_SUGGESTIONS.map((user) => (
            <div className="suggestion-item" key={user.id}>
              <img src={user.avatar} alt={user.username} className="suggestion-avatar" />
              <div className="suggestion-info">
                <span className="suggestion-username">{user.username}</span>
                <span className="suggestion-sub">{user.name}</span>
              </div>
              <button className="follow-btn">팔로우</button>
            </div>
          ))}

          <p className="footer-links">
            소개 · 도움말 · 홍보 · 개인정보처리방침<br />
            © 2024 INSTAGRAM
          </p>
        </aside>
      </div>
    </div>
  );
};

export default Home;
