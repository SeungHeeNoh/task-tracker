# [G0003] 그룹 초대 수락 API

초대 코드로 그룹에 가입하는 API입니다. 이미 해당 그룹의 멤버인 경우 멱등하게 성공 응답을 반환합니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `G0003` |
| **URL** | `/api/v1/invitations/{code}/accept` |
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
| | **groupSeq** | | Long | | 가입된 그룹 식별자 |
| | **groupName** | | String | | 가입된 그룹 이름 |
| | **memberCount** | | int | | 가입 후 그룹 현재 인원 |

---

### [Response Example]
```json
{
  "status": "SC",
  "message": "그룹에 가입되었습니다.",
  "data": {
    "groupSeq": 1,
    "groupName": "우리 가족",
    "memberCount": 4
  }
}
```

---

### [Error Response]
| 상황 | code | message |
| :--- | :--- | :--- |
| 코드 없음 또는 만료 | `INVITATION_NOT_FOUND` | 존재하지 않거나 만료된 초대 코드입니다. |
| 코드의 그룹이 더 이상 존재하지 않음 | `GROUP_NOT_FOUND` | 존재하지 않는 그룹입니다. |
| 그룹 정원 초과 | `GROUP_MEMBER_LIMIT_EXCEEDED` | 그룹 최대 인원을 초과했습니다. |
| 사용자 참여 그룹 수 초과 | `USER_GROUP_LIMIT_EXCEEDED` | 참여할 수 있는 그룹 수를 초과했습니다. |
| 사용 가능 횟수 소진 | `INVITATION_EXHAUSTED` | 사용 가능 횟수를 모두 소진한 초대 코드입니다. |
| 서버 내부 오류 | `INTERNAL_SERVER_ERROR` | 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요. |

---

### [비고]
- 수락 시 `user_group_map` 테이블에 `MEMBER` 역할로 insert됩니다.
- 내부적으로 Redis의 남은 사용 횟수를 원자적으로 차감한 뒤 DB insert를 수행합니다. 잔여 횟수가 0이 되면 코드가 즉시 삭제됩니다.
- 이미 해당 그룹의 멤버인 경우 한도 검증 없이 성공 응답을 반환합니다 (멱등 처리).
