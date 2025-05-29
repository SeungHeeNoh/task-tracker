const API_BASE = 'http://localhost:8080';

export const getCheckLists = async () => {
  const res = await fetch(`${API_BASE}/checklists`);
  if (!res.ok) throw new Error('체크리스트 조회 실패');
  return res.json();
};