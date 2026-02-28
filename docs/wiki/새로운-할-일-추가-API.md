# [T0001] 새로운 할 일 추가 API

새로운 할 일을 등록하는 API입니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `T0001` |
| **URL** | `/api/v1/tasks` |
| **HTTP Method** | `POST` |
| **요청 Content-Type** | `application/json` |
| **응답 Content-Type** | `application/json; charset=utf-8` |

---

### [Request]

#### HEADER
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `X-Access-Token` | String | O | 인증 토큰 |

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
  "message": "Success"
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
    participant TaskLogMapper
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

    Client->>TaskController: addTask(ManageTaskRequest)
    activate TaskController
    TaskController->>ManageTaskRequest: checkValidation()
    activate ManageTaskRequest
    ManageTaskRequest-->>TaskController: (return)
    deactivate ManageTaskRequest
    
    alt [Validation Failed]
        TaskController-->>Client: CommonResponse.fail(error message)
    else [Validation Passed]
        TaskController->>TaskService: addTask(Task)
        activate TaskService
        
        TaskService->>TaskMapper: addTask(task)
        activate TaskMapper
        TaskMapper->>Database: INSERT task (MyBatis)
        activate Database
        Database-->>TaskMapper: (success)
        deactivate Database
        TaskMapper-->>TaskService: (success)
        deactivate TaskMapper
        
        TaskService->>TaskLogMapper: addTaskLog(TaskLog)
        activate TaskLogMapper
        TaskLogMapper->>Database: INSERT task_log (CREATED)
        activate Database
        Database-->>TaskLogMapper: (success)
        deactivate Database
        TaskLogMapper-->>TaskService: (success)
        deactivate TaskLogMapper
        
        TaskService-->>TaskController: CommonResponse.success("할 일을 추가하는 데 성공했습니다.")
        deactivate TaskService
        TaskController-->>Client: 200 OK (CommonResponse)
    end
    deactivate TaskController
```