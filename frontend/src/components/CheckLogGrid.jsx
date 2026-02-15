import { useEffect, useState } from 'react';
import { getCheckDates } from '../api/checkListApi';

const getPastNDates = (n) => {
  const today = new Date();
  return Array.from({ length: n }, (_, i) => {
    const d = new Date(today);
    d.setDate(today.getDate() - n + i + 1);
    return d.toISOString().split('T')[0]; // YYYY-MM-DD
  });
};

function CheckLogGrid({ checkListId }) {
  const [checkedDates, setCheckedDates] = useState([]);

  useEffect(() => {
    getCheckDates(checkListId).then(setCheckedDates).catch(console.error);
  }, [checkListId]);

  const dates = getPastNDates(35); // 5주 = 35칸
  const dayOfWeek = ['월', '화', '수', '목', '금', '토', '일'];

  return (
    <div>
      <h4>🟩 체크 기록 (최근 5주)</h4>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 24px)', gap: '4px' }}>
        {dates.map((date, i) => {
          const isChecked = checkedDates.includes(date);
          return (
            <div
              key={date}
              title={date}
              style={{
                width: 20,
                height: 20,
                borderRadius: 4,
                backgroundColor: isChecked ? '#2ecc71' : '#ddd',
              }}
            />
          );
        })}
      </div>
      <div style={{ marginTop: 4, fontSize: 12, color: '#888' }}>
        {dayOfWeek.map((d, i) => (
          <span key={i} style={{ display: 'inline-block', width: 24, textAlign: 'center' }}>
            {d}
          </span>
        ))}
      </div>
    </div>
  );
}

export default CheckLogGrid;
