✅ Spring Boot 글로벌 예외 처리 구조
단일 응답 구조 기반: ApiResponse<T> 사용

📁 패키지 구조 예시
vbnet
복사
편집
com.boot.loiteBackend
├── global.error
│   ├── exception
│   │   ├── ErrorCode (interface)
│   │   ├── CustomException
│   │   └── GlobalExceptionHandler
│   └── response
│       └── ApiResponse
├── support
│   └── counsel
│       └── error
│           └── CounselErrorCode
🔧 구성 요소별 설명
1. ErrorCode (interface)
   역할: 모든 도메인 에러 Enum들이 구현해야 하는 공통 인터페이스

예시 메서드:

java
복사
편집
HttpStatus getStatus();
String getCode();
String getMessage();
2. CustomException
   역할: 비즈니스 로직 중 예외 발생 시 사용하는 커스텀 예외 클래스

사용 예시:

java
복사
편집
throw new CustomException(CounselErrorCode.NOT_FOUND);
3. ApiResponse<T>
   역할: 성공/실패를 하나의 포맷으로 통합한 응답 DTO

구성 필드:

success: boolean

message: string

code: string (nullable)

data: T

extra: Map<String, Object> (nullable)

사용 예시:

java
복사
편집
// 성공 응답
return ApiResponse.ok(data);

// 실패 응답
return ApiResponse.error(errorCode);
4. GlobalExceptionHandler
   역할: 모든 예외를 전역적으로 핸들링

구현 예시:

java
복사
편집
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustom(CustomException ex) {
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(ApiResponse.error(ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex) {
        return ResponseEntity
            .status(500)
            .body(ApiResponse.<Void>builder()
                .success(false)
                .message("예상치 못한 오류가 발생했습니다.")
                .code("INTERNAL_SERVER_ERROR")
                .build());
    }
}
5. CounselErrorCode (도메인별 에러 코드 예시)
   java
   복사
   편집
   @RequiredArgsConstructor
   public enum CounselErrorCode implements ErrorCode {
   NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_COUNSEL_404", "해당 문의 내역을 찾을 수 없습니다."),
   ALREADY_ANSWERED(HttpStatus.CONFLICT, "ADMIN_COUNSEL_409", "이미 답변이 등록된 문의입니다.");

   private final HttpStatus status;
   private final String code;
   private final String message;

   public HttpStatus getStatus() { return status; }
   public String getCode() { return code; }
   public String getMessage() { return message; }
   }
   🔄 예외 처리 흐름
   text
   복사
   편집
   ❶ 서비스/컨트롤러에서 예외 발생
   → throw new CustomException(CounselErrorCode.NOT_FOUND);

❷ GlobalExceptionHandler 가 예외를 처리

❸ ApiResponse.error(...)를 통해 JSON 응답 구성

❹ 클라이언트는 일관된 응답 포맷을 수신
📦 예시 응답 (실패)
json
복사
편집
{
"success": false,
"message": "해당 문의 내역을 찾을 수 없습니다.",
"code": "ADMIN_COUNSEL_404",
"data": null,
"extra": null
}
✅ 장점 요약
항목	설명
✅ 응답 포맷 통일	성공/실패 모두 ApiResponse<T>로 관리
✅ 프론트 처리 간결	일관된 구조로 분기처리 용이
✅ 도메인별 에러 분리	CounselErrorCode, UserErrorCode 등으로 관리
✅ Swagger 문서화 용이	단일 응답 스키마 문서화 가능
✅ 확장성 우수	extra 필드를 통한 디버깅 정보 전달 가능

🚀 확장 팁
@Valid 검증 실패 예외(MethodArgumentNotValidException)도 ApiResponse.error(...)로 통합 추천

Swagger에 다음과 같이 실패 응답 스키마를 명시하세요:

java
복사
편집
@ApiResponse(
responseCode = "400",
description = "잘못된 요청",
content = @Content(schema = @Schema(implementation = ApiResponse.class))
)
필요하시다면 @Valid 검증 오류나 BindingResult 기반의 validation 예외도 ApiResponse로 통일하는 방법을 추가로 안내해드릴 수 있습니다.