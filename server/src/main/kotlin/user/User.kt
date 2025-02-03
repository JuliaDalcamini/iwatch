package user

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

/**
 * User model with all fields. Should not be used in API responses, as it contains sensitive data.
 */

@Serializable
data class User(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

