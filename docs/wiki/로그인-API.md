# [A0001] 로그인 API

사용자 인증 및 로그인 처리를 위한 API입니다.

### [기본 정보]
| 항목 | 내용 |
| :--- | :--- |
| **API Code**| `A0001` |
| **URL** | `/api/v1/auth/login` |
| **HTTP Method** | `POST` |
| **요청 Content-Type** | `application/json` |
| **응답 Content-Type** | `application/json; charset=utf-8` |

---

### [Request]

#### HEADER
해당 없음.

#### BODY
| 파라미터명 | 타입 | 필수여부 | 설명 |
| :--- | :---: | :---: | :--- |
| `userId` | String | O | 사용자 ID |
| `password` | String | O | 비밀번호 |

---

### [Response]

| 파라미터명(1) | 파라미터명(2) | 타입(1) | 타입(2) | 필수여부 | 설명 |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **status** | | String | | O | SC, FA 반환 |
| **message** | | String | | O | 결과 메시지 |
| **data** | | json | | X | FA이면 data값 없음 |
| | **accessToken** | | String | X | |
| | **userSeq** | | int | X | |
| | **userId** | | String | X | |
| | **userName** | | String | X | |
| | **avatarImg** | | String | X | |

---

### [Response Example]
```json
{
  "status": "SC",
  "message": "로그인에 성공하였습니다.",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ...",
    "userSeq": 1,
    "userId": "test1234",
    "userName": "홍길동",
    "avatarImg": "avatar1.png"
  }
}
```

---

### [Sequence Diagram]
```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant AuthController
    participant AuthenticationManager
    participant SecurityContextHolder
    participant SecurityContext
    participant TokenProvider

    Client->>AuthController: login(LoginRequest)
    activate AuthController

    AuthController->>AuthenticationManager: authenticate(UsernamePasswordAuthenticationToken)
    activate AuthenticationManager
    
    alt [BadCredentialsException]
        AuthenticationManager-->>AuthController: throws BadCredentialsException
        AuthController-->>Client: CommonResponse.fail("아이디 또는 비밀번호가 일치하지 않습니다.")
    else [Authentication Success]
        AuthenticationManager-->>AuthController: Authentication
        deactivate AuthenticationManager
        
        AuthController->>SecurityContextHolder: getContext().setAuthentication(authentication)
        activate SecurityContextHolder
        SecurityContextHolder-->>AuthController: (success)
        deactivate SecurityContextHolder

        AuthController->>SecurityContext: getCurrentUser()
        activate SecurityContext
        SecurityContext-->>AuthController: Users (currentUser)
        deactivate SecurityContext

        AuthController->>TokenProvider: generateAccessToken(currentUser)
        activate TokenProvider
        TokenProvider-->>AuthController: String (accessToken)
        deactivate TokenProvider

        AuthController-->>Client: 200 OK (CommonResponse.success(data))
    end
    deactivate AuthController
```
