# [T0003] 할 일 내용 삭제 API

선택한 할 일의 내용을 삭제하는 API입니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `T0003` |
| **URL** | `/api/v1/tasks/{id}` |
| **HTTP Method** | `DELETE` |
| **요청 Content-Type** | `application/json` |
| **응답 Content-Type** | `application/json; charset=utf-8` |

---

### [Request]

#### HEADER
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `X-AccessToken` | String | O | 인증 토큰 |

#### BODY
해당 없음.

---

### [Response]

| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| **status** | String | O | SC, FA 반환 |
| **message** | String | O | 결과 메시지 |

---

### [Response Example]
```json
{
  "status": "SC",
  "message": "할 일을 삭제하는 데 성공했습니다."
}
```

---

### [Sequence Diagram]
```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant JWTAuthenticationFilter
    participant TaskController
    participant TaskService
    participant SecurityContext
    participant TaskMapper
    participant Database

    Client->>JWTAuthenticationFilter: Request with X-AccessToken
    activate JWTAuthenticationFilter
    JWTAuthenticationFilter->>JWTAuthenticationFilter: extractToken & validToken
    
    alt [Token is valid]
        JWTAuthenticationFilter->>SecurityContext: Set Authentication (UserDetails)
    else [Token is invalid or missing]
        JWTAuthenticationFilter->>SecurityContext: Clear Context
    end
    deactivate JWTAuthenticationFilter

    Client->>TaskController: deleteTask(taskId)
    activate TaskController
    
    TaskController->>TaskService: deleteTask(Task)
    activate TaskService
    
    TaskService->>SecurityContext: getCurrentUserGroupSeq()
    activate SecurityContext
    SecurityContext-->>TaskService: List<Long> (groupSeqs)
    deactivate SecurityContext

    TaskService->>TaskMapper: deleteTask(task, groupSeqs)
    activate TaskMapper
    TaskMapper->>Database: DELETE task (MyBatis)
    activate Database
    Database-->>TaskMapper: Integer (deleteCount)
    deactivate Database
    TaskMapper-->>TaskService: Integer (deleteCount)
    deactivate TaskMapper
    
    alt [deleteCount == 0]
        TaskService-->>TaskController: throw IllegalArgumentException
        TaskController-->>Client: CommonResponse.fail("삭제 권한이 없거나 이미 처리된 요청입니다.")
    else [deleteCount > 0]
        TaskService-->>TaskController: CommonResponse.success("할 일을 삭제하는 데 성공했습니다.")
        TaskController-->>Client: 200 OK (CommonResponse)
    end
    
    deactivate TaskService
    deactivate TaskController
```
