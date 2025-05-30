import { useState } from 'react';

import '../assets/css/CheckListForm.css'
import { registerCheckList } from "../api/checkListApi";

export default function CheckListForm( {onSuccess} ) {

    const[title, setTitle] = useState('');

    const register = async () => {
        const res = await registerCheckList(title);

        if(res?.message) {
            alert(res.message);
        } else if(res?.result === 'SC' && onSuccess) {
            onSuccess();
        }

    };

    return (
    <div className="checklist-form">
        <div className="form-row">
        <label htmlFor="title">체크리스트 이름 : </label>
        <input
            type="text" id="title" maxLength={50} value={title}
            placeholder="예: 운동하기, 독서하기 등"
            onChange={(e) => setTitle(e.target.value)} 
        />
        </div>
          <div className="button-row">
            <button onClick={register}>등록하기</button>
        </div>
    </div>
    );
}