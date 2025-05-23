package com.example.chat.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets
import kotlin.jvm.java

object JwtUtil {
    // Przykładowy 512-bitowy klucz zapisany jako 128-znakowy ciąg szesnastkowy.
    // UWAGA: W produkcji użyj bezpiecznego sposobu przechowywania i generowania klucza.
    private const val SECRET_KEY = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF"
    private const val EXPIRATION_TIME = 1000 * 60 * 60 * 10L // 10 godzin

    private val key = SecretKeySpec(SECRET_KEY.toByteArray(Charsets.UTF_8), SignatureAlgorithm.HS512.jcaName)

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaims(token)
            !claims.expiration.before(Date())
        } catch (ex: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String? {
        return try {
            getClaims(token).subject
        } catch (ex: Exception) {
            null
        }
    }

    fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
    fun extractRoles(claims: Claims): List<GrantedAuthority> {
        return claims.get("roles", String::class.java)
            .split(",")
            .map { SimpleGrantedAuthority(it) }
    }
}
