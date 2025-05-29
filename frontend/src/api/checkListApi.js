const API_BASE = 'http://localhost:8080';

export const getCheckLists = async () => {
  const res = await fetch(`${API_BASE}/checklists`);
  if (!res.ok) throw new Error('체크리스트 조회 실패');
  return res.json();
};

export const confirmCheck = async (checkListId, checkDate) => {
  const res = await fetch(`${API_BASE}/checklogs/confirm`, {
    method: 'POST',
    headers: {'Content-Type' : 'application/json'},
    body: JSON.stringify({
      checkListId: checkListId,
      checkDate: checkDate
    })
  });

  if(!res.ok) throw new Error("알 수 없는 에러 발생.");
  return res.json();
}

export const releaseCheck = async (checkListId, checkDate) => {
  const res = await fetch(`${API_BASE}/checklogs/release`, {
    method: 'POST',
    headers: {'Content-Type' : 'application/json'},
    body: JSON.stringify({
      checkListId: checkListId,
      checkDate: checkDate
    })
  });

  if (!res.ok) throw new Error("알 수 없는 에러 발생.");
  return res.json();
};