function CheckListItem({ item }) {
  return (
    <li style={{ margin: '1rem 0', display: 'flex', alignItems: 'center' }}>
      <span style={{ flexGrow: 1 }}>{item.title}</span>
    </li>
  );
}

export default CheckListItem;
