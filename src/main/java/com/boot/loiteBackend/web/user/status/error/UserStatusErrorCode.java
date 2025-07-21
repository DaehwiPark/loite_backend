
import com.boot.loiteBackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserStatusErrorCode implements ErrorCode {

    ROLE_NOT_FOUND("USER_404_02", "지정된 역할(Role)을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    private final String code;
    private final String message;
    private final HttpStatus status;
    }