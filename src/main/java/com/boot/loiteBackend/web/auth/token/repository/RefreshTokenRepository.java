
package com.boot.loiteBackend.web.auth.token.repository;

import com.boot.loiteBackend.web.auth.token.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
