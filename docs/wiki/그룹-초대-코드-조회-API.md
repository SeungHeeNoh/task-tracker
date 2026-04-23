# [G0002] 그룹 초대 코드 조회 API

발급된 초대 코드로 가입 대상 그룹 정보를 미리 조회하는 API입니다. 수락 전 미리보기 용도로 사용됩니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `G0002` |
| **URL** | `/api/v1/invitations/{code}` |
| **HTTP Method** | `GET` |
| **요청 Content-Type** | `application/json` |
| **응답 Content-Type** | `application/json; charset=utf-8` |

---

### [Request]

#### HEADER
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `X-AccessToken` | String | O | 인증 토큰 |

#### PATH
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `code` | String | O | 초대 코드 (8자리) |

#### BODY
해당 없음.

---

### [Response]

| 파라미터명(1) | 파라미터명(2) | 타입(1) | 타입(2) | 필수여부 | 설명 |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **status** | | String | | O | SC, FA 반환 |
| **code** | | String | | | FA인 경우 ErrorCode |
| **message** | | String | | O | 결과 메시지 |
| **data** | | json | | | FA이면 data값 없음 |
| | **groupSeq** | | Long | | 그룹 식별자 |
| | **groupName** | | String | | 그룹 이름 |
| | **memberCount** | | int | | 현재 인원 |

---

### [Response Example]
```json
{
  "status": "SC",
  "message": "초대 코드 정보를 조회했습니다.",
  "data": {
    "groupSeq": 1,
    "groupName": "우리 가족",
    "memberCount": 3
  }
}
```

---

### [Error Response]
| 상황 | code | message |
| :--- | :--- | :--- |
| 코드 없음 또는 만료 | `INVITATION_NOT_FOUND` | 존재하지 않거나 만료된 초대 코드입니다. |
| 코드의 그룹이 더 이상 존재하지 않음 | `GROUP_NOT_FOUND` | 존재하지 않는 그룹입니다. |
| 서버 내부 오류 | `INTERNAL_SERVER_ERROR` | 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요. |
