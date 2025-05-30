import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSquareCheck } from '@fortawesome/free-solid-svg-icons';

import '../assets/css/CheckListPage.css'

import CheckListGroup from '../components/CheckListGroup';

export default function CheckListPage() {

    return (
        <>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <h1><FontAwesomeIcon icon={faSquareCheck} style={{color: "#74C0FC",}} /> 나의 체크리스트</h1>
                <button className="add-button">+ 항목 추가</button>
            </div>
            <CheckListGroup />
        </>
    );
}