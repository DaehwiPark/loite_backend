## 예외 처리 구조 정리 (Spring Boot + Global Error Handling)

---

### 패키지 구조

```
com.boot.loiteBackend
├── global.error
│   ├── exception
│   │   ├── CustomException
│   │   ├── ErrorCode
│   │   ├── ErrorResponse
│   │   ├── ErrorResponseUtil
│   │   └── GlobalExceptionHandler
│   └── ...
├── support
│   └── counsel
│       ├── error
│       │   └── CounselErrorCode
```

---

## 구성 요소별 설명

### 1. `ErrorCode` (Interface)

- **역할**: 모든 도메인별 에러코드 Enum들이 구현해야 하는 인터페이스.
- **구현 메서드**:

    ```java
    HttpStatus getStatus();
    String getMessage();
    ```


> 예외 상황마다 상태코드와 메시지를 공통 규격으로 만들기 위한 기준 역할을 함.
>

---

### 2. `CustomException` (RuntimeException 확장)

- **역할**: 비즈니스 에러 발생 시 직접 던지는 커스텀 예외 클래스.
- **사용법**:

    ```java
    throw new CustomException(CounselErrorCode.NOT_FOUND);
    ```

- 내부에 `ErrorCode` 타입을 가지고 있어, 어떤 에러인지 명확하게 구분 가능.

---

### 3. `ErrorResponse` (DTO)

- **역할**: 클라이언트에 반환되는 통일된 에러 응답 형태.
- **필드**:
    - `timestamp`
    - `status` (예: 404)
    - `error` (예: Not Found)
    - `message` (예: 해당 문의 내역을 찾을 수 없습니다.)
    - `path` (예: `/api/support/counsel/1`)
- **Swagger 문서화용 `@Schema` 어노테이션** 포함.

---

### 4. `ErrorResponseUtil`

- **역할**: `ErrorCode` 또는 일반 Exception → `ErrorResponse` 로 변환하여 `ResponseEntity`를 생성하는 유틸.
- **예시**:

    ```java
    ErrorResponseUtil.toResponseEntity(errorCode, request);
    ErrorResponseUtil.toInternalServerError(ex, request);
    ```


---

### 5. `GlobalExceptionHandler`

- **역할**: 전역 예외를 핸들링하는 클래스.
- **어노테이션**: `@RestControllerAdvice(annotations = RestController.class)`
- **핸들링 대상**:
    - `CustomException`
    - `Exception` (기타 예외 → 500 응답)

> Swagger 충돌을 피하기 위해 @Hidden을 붙이거나 basePackages로 범위 제한 가능.
>

---

### 6. `CounselErrorCode` (도메인별 에러 코드 Enum 예시)

```java
@RequiredArgsConstructor
public enum CounselErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 문의 내역을 찾을 수 없습니다."),
    ALREADY_ANSWERED(HttpStatus.CONFLICT, "이미 답변이 등록된 문의입니다.");    private final HttpStatus status;
    private final String message;    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
```

- **역할**: 도메인(`counsel`) 관련 오류 상황 정의
- **장점**: 도메인별로 나누어 관리하므로 유지보수 및 확장 용이
-

---

## 사용 흐름 정리

```
❶ 서비스 코드 내부
→ throw new CustomException(CounselErrorCode.NOT_FOUND);
❷ GlobalExceptionHandler
→ @ExceptionHandler(CustomException.class) 에 의해 잡힘
❸ ErrorResponseUtil
→ ErrorCode 기반으로 ErrorResponse 생성
❹ 클라이언트
← status, message, path 등이 담긴 JSON 응답 수신
```

---

## 예시 응답 (Swagger 및 실제 API 응답)

```json
{
  "timestamp": "2025-05-29T14:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "해당 문의 내역을 찾을 수 없습니다.",
  "path": "/api/support/counsel/1"
}
```

---

## 장점 요약

- **도메인별 관리** → 유지보수 및 검색 용이
- **Swagger 문서화 자동화**
- **예외 일관성** 유지
- **실무 확장성 매우 좋음**