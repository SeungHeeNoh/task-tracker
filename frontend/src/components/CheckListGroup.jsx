import { useEffect, useState } from 'react';
import { getCheckLists, releaseCheck, confirmCheck } from '../api/checkListApi';

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
        <>
        {!checkLists || checkLists.length === 0 ? (<p> 체크리스트가 없습니다.</p>) :
        (<ul>
            {checkLists.map((checkList) => (
            <CheckListItem
                key={checkList.checkListId}
                checkList={checkList}
                onClick = {() => handleToggle(checkList.checkListId, checkList.isChecked)}
            />
            ))}
        </ul>)}
        </>
    );
}
