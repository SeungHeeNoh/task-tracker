function CheckListItem({ checkList, onClick }) {
  return (
    <li style={{ margin: '1rem 0', display: 'flex', alignItems: 'center' }}>
      <span style={{ flexGrow: 1 }}>{checkList.title}</span>
      <button onClick={ onClick } type="button">{checkList.isChecked ? "❌ 해제" : "✅ 체크"}</button>
    </li>
  );
}


export default CheckListItem;