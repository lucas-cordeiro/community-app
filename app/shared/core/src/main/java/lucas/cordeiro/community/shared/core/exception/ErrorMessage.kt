package lucas.cordeiro.community.shared.core.exception

private const val DEFAULT_ERROR_TITLE = "Something went wrong"
private const val DEFAULT_ERROR_BODY = "Please try again in a few moments."

data class ErrorMessage(
    val title: String,
    val body: String?,
)

fun Throwable.toErrorMessage(
    defaultTitle: String = DEFAULT_ERROR_TITLE,
    defaultBody: String = DEFAULT_ERROR_BODY,
): ErrorMessage = ErrorMessage(
    title = defaultTitle,
    body = localizedMessage ?: defaultBody,
)
