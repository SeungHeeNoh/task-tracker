import { useEffect, useState } from 'react';
import { getCheckLists, releaseCheck, confirmCheck } from '../api/checkListApi';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSquareCheck } from '@fortawesome/free-solid-svg-icons';

import CheckListItem from './CheckListItem';

export default function CheckListGroup() {

    const [checkLists, setCheckLists] = useState([]);

    useEffect(() => {
        getCheckLists().then(setCheckLists).catch(console.error);
    }, []);

    const handleToggle = async (checkListId, isChecked) => {
        const today = new Date().toISOString().split('T')[0];
        let res;

        if(isChecked) {
            res = await releaseCheck(checkListId, today);
        } else {
            res = await confirmCheck(checkListId, today);
        }

        if(res?.message) {
            alert(res.message);
        }

        const updated = await getCheckLists();
        setCheckLists(updated);
    };

    return (
        <div style={{ padding: '2rem' }}>
        <h1><FontAwesomeIcon icon={faSquareCheck} style={{color: "#74C0FC",}} /> 나의 체크리스트</h1>
        {!checkLists || checkLists.length === 0 ? 
        (<p> 체크리스트가 없습니다.</p>) :
        (<ul>
            {checkLists.map((checkList) => (
            <CheckListItem
                key={checkList.checkListId}
                checkList={checkList}
                onClick = {() => handleToggle(checkList.checkListId, checkList.isChecked)}
            />
            ))}
        </ul>)}
        </div>
    );
}
