# [T0002] 할 일 내용 수정 API

기존에 등록된 할 일 목록의 내용을 수정하는 API입니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `T0002` |
| **URL** | `/api/v1/tasks/{id}` |
| **HTTP Method** | `POST` |
| **요청 Content-Type** | `application/json` |
| **응답 Content-Type** | `application/json; charset=utf-8` |

---

### [Request]

#### HEADER
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `X-AccessToken` | String | O | 인증 토큰 |

#### BODY
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `title` | String | O | 할 일 제목 |
| `groupSeq` | Long | O | 그룹 순번 |
| `duedate` | String | O | 마감 기한 (YYYY-MM-DD) |

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
  "message": "할 일을 수정하는 데 성공했습니다."
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
    participant ManageTaskRequest
    participant TaskService
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

    Client->>TaskController: modifyTask(taskId, ManageTaskRequest)
    activate TaskController
    TaskController->>ManageTaskRequest: checkValidation()
    activate ManageTaskRequest
    ManageTaskRequest-->>TaskController: (return)
    deactivate ManageTaskRequest
    
    alt [Validation Failed]
        TaskController-->>Client: CommonResponse.fail(error message)
    else [Validation Passed]
        TaskController->>TaskService: modifyTask(Task)
        activate TaskService
        
        TaskService->>TaskMapper: modifyTask(task)
        activate TaskMapper
        TaskMapper->>Database: UPDATE task (MyBatis)
        activate Database
        Database-->>TaskMapper: (success)
        deactivate Database
        TaskMapper-->>TaskService: (success)
        deactivate TaskMapper
        
        TaskService-->>TaskController: CommonResponse.success("할 일을 수정하는 데 성공했습니다.")
        deactivate TaskService
        TaskController-->>Client: 200 OK (CommonResponse)
    end
    deactivate TaskController
```
