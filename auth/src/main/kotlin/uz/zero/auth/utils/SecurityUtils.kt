package uz.zero.auth.utils

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.springframework.security.core.authority.SimpleGrantedAuthority
import uz.zero.auth.model.security.CustomUserDetails

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
abstract class LongMixin

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonDeserialize(using = CustomUserDetailsDeserializer::class)
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
internal abstract class CustomUserDetailsMixin

class CustomUserDetailsDeserializer : JsonDeserializer<CustomUserDetails>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): CustomUserDetails {
        val mapper = p.codec as ObjectMapper
        val root = mapper.readTree<JsonNode>(p)

        // Majburiy maydonlarni tekshirish uchun yordamchi funksiya
        fun req(field: String): JsonNode =
            root.get(field) ?: throw IllegalArgumentException("Missing field: $field")

        val id = req("id").asLong()
        val phoneNumber = req("phoneNumber").asText()
        val password = req("password").asText()
        val enabled = req("enabled").asBoolean()

        // --- Authorities (Huquqlar) qismini xavfsiz o'qish ---
        val authoritiesNode = root.get("authorities")
        val authorities = mutableSetOf<SimpleGrantedAuthority>()

        authoritiesNode?.forEach { node ->
            val roleStr = if (node.isObject) {
                // Agar [{"authority": "ROLE_ADMIN"}] ko'rinishida bo'lsa
                node.get("authority")?.asText()
            } else {
                // Agar ["ROLE_ADMIN"] ko'rinishida bo'lsa
                node.asText()
            }

            if (!roleStr.isNullOrBlank()) {
                authorities.add(SimpleGrantedAuthority(roleStr))
            }
        }

        // Agar huquqlar topilmasa, NPE yoki boshqa xatolikni oldini olish uchun default role
        if (authorities.isEmpty()) {
            authorities.add(SimpleGrantedAuthority("ROLE_USER"))
        }
        // -----------------------------------------------------

        // organizationId null bo'lishi mumkinligini hisobga olamiz
        val organizationId = root.get("organizationId")?.asLong() ?: 0L

        return CustomUserDetails(
            id = id,
            phoneNumber = phoneNumber,
            password = password,
            organizationId = organizationId,
            authorities = authorities,
            enabled = enabled
        )
    }
}