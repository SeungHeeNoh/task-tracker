# [T0006] 전체 할 일 목록 조회 API

등록된 전체 할 일 목록을 조회하는 API입니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `T0006` |
| **URL** | `/api/v1/tasks` |
| **HTTP Method** | `GET` |
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

    Client->>TaskController: getTasks(TaskListRequest)
    activate TaskController
    
    alt [viewMode is missing]
        TaskController-->>Client: CommonResponse.fail("viewMode는 필수값입니다.")
    else [viewMode is valid]
        TaskController->>TaskService: getTaskList(viewMode)
        activate TaskService
        TaskService->>SecurityContext: getCurrentUser().getGroup()
        activate SecurityContext
        SecurityContext-->>TaskService: List<Groups>
        deactivate SecurityContext
        TaskService->>TaskMapper: getTaskList(groupSeqs, viewMode)
        activate TaskMapper
        TaskMapper->>Database: SELECT tasks (MyBatis)
        activate Database
        Database-->>TaskMapper: Result Set
        deactivate Database
        TaskMapper-->>TaskService: List<TaskInfo>
        deactivate TaskMapper
        TaskService-->>TaskController: CommonResponse.success(List<TaskInfo>)
        deactivate TaskService
        TaskController-->>Client: 200 OK (CommonResponse)
    end
    deactivate TaskController
```