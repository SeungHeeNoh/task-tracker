import CheckListPage from './pages/CheckListPage';
import Layout from './layouts/Layout';

function App() {

  return (
    <Layout children={<CheckListPage />}/>
  );
}

export default App;
