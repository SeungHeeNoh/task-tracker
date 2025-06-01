const API_BASE = 'http://localhost:8080/api/';

export const getCheckLists = async () => {
  const res = await fetch(`${API_BASE}v1/checklists`);
  if (!res.ok) throw new Error('체크리스트 조회 실패');
  return res.json();
};

export const confirmCheck = async (checkListId, checkDate) => {
  const res = await fetch(`${API_BASE}v1/checklogs/confirm`, {
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
  const res = await fetch(`${API_BASE}v1/checklogs/release`, {
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

export const registerCheckList = async(title) => {
  const res = await fetch(`${API_BASE}v1/checklists`, {
    method: 'POST',
    headers: {'Content-Type' : 'application/json'},
    body: JSON.stringify({
      title: title
    })
  });

  if (!res.ok) throw new Error("알 수 없는 에러 발생.");
  return res.json();
}