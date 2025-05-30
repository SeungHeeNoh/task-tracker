import { useState } from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSquareCheck } from '@fortawesome/free-solid-svg-icons';

import '../assets/css/CheckListPage.css'

import CheckListGroup from '../components/CheckListGroup';
import CheckListForm from '../components/CheckListForm';

export default function CheckListPage() {

    const[mode, setMode] = useState('list');    // mode : list | add

    const handleRegisterSuccess = () => {
        setMode('list');
    };

    return (
        <>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <h1><FontAwesomeIcon icon={faSquareCheck} style={{color: "#74C0FC",}} /> 나의 체크리스트</h1>
                { mode === 'list' && (<button onClick={() => setMode('add')} className="add-button">+ 항목 추가</button>) }
                { mode === 'add' && (<button onClick={() => setMode('list')} className="add-button">← 돌아가기</button>)}
            </div>
            { mode === 'list' && <CheckListGroup />}
            { mode === 'add' && <CheckListForm onSuccess={handleRegisterSuccess} />}
        </>
    );
}