package id.dev.story.domain.stories


data class RemoteKeyDomain(
   val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
