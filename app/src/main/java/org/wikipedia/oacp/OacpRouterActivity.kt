package org.wikipedia.oacp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.wikipedia.Constants
import org.wikipedia.WikipediaApp
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.history.HistoryEntry
import org.wikipedia.page.PageActivity
import org.wikipedia.page.PageTitle
import org.wikipedia.random.RandomActivity
import org.wikipedia.search.SearchActivity

class OacpRouterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        routeIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        routeIntent(intent)
    }

    private fun routeIntent(incomingIntent: Intent?) {
        val nextIntent = when (incomingIntent?.action) {
            OacpActions.searchArticlesAction -> buildSearchIntent(incomingIntent)
            OacpActions.openArticleAction -> buildOpenArticleIntent(incomingIntent)
            OacpActions.openRandomArticleAction -> buildRandomArticleIntent(incomingIntent)
            else -> null
        }

        if (nextIntent != null) {
            startActivity(nextIntent)
        }
        finish()
    }

    private fun buildSearchIntent(incomingIntent: Intent): Intent? {
        val query = incomingIntent.requireTrimmedStringExtra(OacpActions.EXTRA_QUERY) ?: return null
        return SearchActivity.newIntent(
            context = this,
            source = Constants.InvokeSource.VOICE,
            query = query
        )
    }

    private fun buildOpenArticleIntent(incomingIntent: Intent): Intent? {
        val articleTitle = incomingIntent.requireTrimmedStringExtra(OacpActions.EXTRA_ARTICLE_TITLE) ?: return null
        val wikiSite = resolveWikiSite(incomingIntent.optionalTrimmedStringExtra(OacpActions.EXTRA_LANGUAGE_CODE))
        val pageTitle = PageTitle(articleTitle, wikiSite)
        val historyEntry = HistoryEntry(pageTitle, HistoryEntry.SOURCE_SEARCH)
        return PageActivity.newIntentForCurrentTab(this, historyEntry, pageTitle, false)
    }

    private fun buildRandomArticleIntent(incomingIntent: Intent): Intent {
        val wikiSite = resolveWikiSite(incomingIntent.optionalTrimmedStringExtra(OacpActions.EXTRA_LANGUAGE_CODE))
        return RandomActivity.newIntent(this, wikiSite, Constants.InvokeSource.VOICE)
    }

    private fun resolveWikiSite(languageCode: String?): WikiSite {
        val resolvedLanguageCode = languageCode?.lowercase().orEmpty()
        return if (resolvedLanguageCode.isNotEmpty()) {
            WikiSite.forLanguageCode(resolvedLanguageCode)
        } else {
            WikiSite.forLanguageCode(WikipediaApp.instance.appOrSystemLanguageCode)
        }
    }

    private fun Intent.requireTrimmedStringExtra(key: String): String? {
        return optionalTrimmedStringExtra(key)?.takeIf { it.isNotEmpty() }
    }

    private fun Intent.optionalTrimmedStringExtra(key: String): String? {
        return getStringExtra(key)?.trim()
    }
}
