# [T0004] 완료 상태 토글 API

선택한 할 일의 완료 상태를 토글하는 API입니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `T0004` |
| **URL** | `/api/v1/tasks/{id}/status` |
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
  "message": "할 일 상태를 변경하는 데 성공했습니다."
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

    Client->>TaskController: changeStatus(taskId)
    activate TaskController
    
    TaskController->>TaskService: changeStatus(taskId)
    activate TaskService
    
    TaskService->>SecurityContext: getCurrentUserGroupSeq()
    activate SecurityContext
    SecurityContext-->>TaskService: List<Long> (groupSeqs)
    deactivate SecurityContext

    TaskService->>TaskMapper: getTaskStatus(taskId, groupSeqs)
    activate TaskMapper
    TaskMapper->>Database: SELECT task status (MyBatis)
    activate Database
    Database-->>TaskMapper: TaskInfo
    deactivate Database
    TaskMapper-->>TaskService: TaskInfo (currentTask)
    deactivate TaskMapper
    
    alt [currentTask == null]
        TaskService-->>TaskController: throw IllegalArgumentException
        TaskController-->>Client: CommonResponse.fail("권한이 없거나 존재하지 않는 할 일입니다.")
    else [currentTask != null]
        TaskService->>TaskLogMapper: addTaskLog(TaskLog)
        activate TaskLogMapper
        TaskLogMapper->>Database: INSERT task_log (nextStatus)
        activate Database
        Database-->>TaskLogMapper: (success)
        deactivate Database
        TaskLogMapper-->>TaskService: (success)
        deactivate TaskLogMapper
        
        TaskService-->>TaskController: CommonResponse.success("할 일 상태를 변경하는 데 성공했습니다.")
        TaskController-->>Client: 200 OK (CommonResponse)
    end
    
    deactivate TaskService
    deactivate TaskController
```
