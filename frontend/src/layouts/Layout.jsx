import '../assets/css/Layout.css'

import Header from './Header';
import Footer from './Footer';

export default function Layout({ children }) {
  return (
    <div className="layout">
      <Header />
      <div className="main-wrapper">
        <main className="main">{children}</main>
      </div>
      <Footer />
    </div>
  );
}