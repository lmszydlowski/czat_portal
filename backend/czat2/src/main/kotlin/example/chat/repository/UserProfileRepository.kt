package example.chat.repository

interface UserProfileRepository {
    abstract fun <UserProfile> save(profile: UserProfile): Any

}
