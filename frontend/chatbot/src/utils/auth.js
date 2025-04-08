export const getAccessToken = () => {
  return (
    localStorage.getItem('token') ||   // 여기만 바꾸면 됨
    sessionStorage.getItem('token') ||
    getCookie('token')
  );
};

export const setTokens = (access, refresh) => {
  localStorage.setItem('token', access);
  sessionStorage.setItem('token', access);
  document.cookie = `token=${access}; path=/; max-age=3600`;

  localStorage.setItem('refreshToken', refresh);
  sessionStorage.setItem('refreshToken', refresh);
  document.cookie = `refreshToken=${refresh}; path=/; max-age=86400`;
};

export const clearTokens = () => {
  localStorage.removeItem('token');
  sessionStorage.removeItem('token');
  document.cookie = 'token=; path=/; max-age=0';

  localStorage.removeItem('refreshToken');
  sessionStorage.removeItem('refreshToken');
  document.cookie = 'refreshToken=; path=/; max-age=0';
};

function getCookie(name) {
  const matches = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
  return matches ? decodeURIComponent(matches[2]) : null;
}
