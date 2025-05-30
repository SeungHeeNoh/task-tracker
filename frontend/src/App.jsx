import CheckListGroup from './components/CheckListGroup';
import Layout from './layouts/Layout';

function App() {

  return (
    <Layout children={<CheckListGroup />}/>
  );
}

export default App;
