export default function Header({ user }) {
  return (
      <header className="header">
        <div className="header-inner">
          <div className="header-left">
            <span className="logo">✔ check-tracker</span>
          </div>
          <nav className="header-right">
            <a href="/" className="menu-item">홈</a>
            <a href="/checklists" className="menu-item">CheckList 목록 보기</a>
            <a href="/login" className="menu-item">Login</a>
          </nav>
        </div>
      </header>
  );
}