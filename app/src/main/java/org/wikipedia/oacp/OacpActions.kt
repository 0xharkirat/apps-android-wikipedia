package org.wikipedia.oacp

import org.wikipedia.BuildConfig

object OacpActions {
    private const val SEARCH_ARTICLES_SUFFIX = "ACTION_SEARCH_ARTICLES"
    private const val OPEN_ARTICLE_SUFFIX = "ACTION_OPEN_ARTICLE"
    private const val OPEN_RANDOM_ARTICLE_SUFFIX = "ACTION_OPEN_RANDOM_ARTICLE"

    const val EXTRA_QUERY = "org.wikipedia.oacp.extra.QUERY"
    const val EXTRA_ARTICLE_TITLE = "org.wikipedia.oacp.extra.ARTICLE_TITLE"
    const val EXTRA_LANGUAGE_CODE = "org.wikipedia.oacp.extra.LANGUAGE_CODE"

    val searchArticlesAction: String
        get() = "${BuildConfig.APPLICATION_ID}.$SEARCH_ARTICLES_SUFFIX"

    val openArticleAction: String
        get() = "${BuildConfig.APPLICATION_ID}.$OPEN_ARTICLE_SUFFIX"

    val openRandomArticleAction: String
        get() = "${BuildConfig.APPLICATION_ID}.$OPEN_RANDOM_ARTICLE_SUFFIX"
}
