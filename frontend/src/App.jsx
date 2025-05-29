import { useEffect, useState } from 'react';
import { getCheckLists } from './api/checkListApi';
import CheckListItem from './components/CheckListItem';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSquareCheck } from '@fortawesome/free-solid-svg-icons';



function App() {
  const [checkLists, setCheckLists] = useState([]);

  useEffect(() => {
    getCheckLists().then(setCheckLists).catch(console.error);
  }, []);


  return (
    <div style={{ padding: '2rem' }}>
      <h1><FontAwesomeIcon icon={faSquareCheck} style={{color: "#74C0FC",}} /> 나의 체크리스트</h1>
      {!checkLists || checkLists.length === 0 ? 
      (<p> 체크리스트가 없습니다.</p>) :
      (<ul>
        {checkLists.map((list) => (
          <CheckListItem
            key={list.checkListId}
            item={list}
          />
        ))}
      </ul>)}
    </div>
  );
}

export default App;
