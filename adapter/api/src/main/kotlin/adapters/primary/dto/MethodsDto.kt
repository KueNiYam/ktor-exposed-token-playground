package adapters.primary.dto

import domain.TokenizerMeta
import kotlinx.serialization.Serializable

@Serializable
data class MethodsResponse(
    val methods: List<MethodInfo>,
    val total_count: Int
) {
    @Serializable
    data class MethodInfo(
        val id: Int,
        val name: String,
        val description: String
    ) {
        companion object {
            fun from(meta: TokenizerMeta): MethodInfo {
                return MethodInfo(
                    id = meta.id,
                    name = meta.name,
                    description = meta.description
                )
            }
        }
    }
    
    companion object {
        fun from(metaList: List<TokenizerMeta>): MethodsResponse {
            val methods = metaList.map { MethodInfo.from(it) }
            return MethodsResponse(
                methods = methods,
                total_count = methods.size
            )
        }
    }
}
