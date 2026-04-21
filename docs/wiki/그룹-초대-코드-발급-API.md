# [G0001] 그룹 초대 코드 발급 API

그룹 소유자(OWNER)가 다른 사용자를 해당 그룹으로 초대하기 위한 초대 코드를 발급하는 API입니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `G0001` |
| **URL** | `/api/v1/groups/{groupSeq}/invitations` |
| **HTTP Method** | `POST` |
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
| `groupSeq` | Long | O | 초대 코드를 발급할 그룹의 식별자 |

#### BODY
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `maxUses` | Integer | | 초대 코드 최대 사용 횟수. 미지정/0 이하일 경우 기본값(10), 상한(100) 초과 시 상한으로 클램프 |

---

### [Response]

| 파라미터명(1) | 파라미터명(2) | 타입(1) | 타입(2) | 필수여부 | 설명 |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **status** | | String | | O | SC, FA 반환 |
| **code** | | String | | | FA인 경우 ErrorCode |
| **message** | | String | | O | 결과 메시지 |
| **data** | | json | | | FA이면 data값 없음 |
| | **code** | | String | | 발급된 초대 코드 (8자리) |
| | **maxUses** | | int | | 최대 사용 횟수 |
| | **expiresInSeconds** | | long | | 만료까지 남은 초 (TTL) |

---

### [Response Example]
```json
{
  "status": "SC",
  "message": "초대 코드가 발급되었습니다.",
  "data": {
    "code": "A7KQX2P9",
    "maxUses": 10,
    "expiresInSeconds": 86400
  }
}
```

---

### [Error Response]
| 상황 | code | message |
| :--- | :--- | :--- |
| 존재하지 않는 그룹 | `GROUP_NOT_FOUND` | 존재하지 않는 그룹입니다. |
| OWNER 권한 없음 | `GROUP_ACCESS_DENIED` | 그룹에 대한 권한이 없습니다. |
| 그룹 인원 상한 초과 | `GROUP_MEMBER_LIMIT_EXCEEDED` | 그룹 최대 인원을 초과했습니다. |
| 서버 내부 오류 | `INTERNAL_SERVER_ERROR` | 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요. |
