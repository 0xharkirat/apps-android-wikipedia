package org.wikipedia.oacp

import android.content.Context
import android.content.Intent
import org.oacp.android.OacpParams
import org.oacp.android.OacpReceiver
import org.oacp.android.OacpResult
import org.wikipedia.Constants
import org.wikipedia.WikipediaApp
import org.wikipedia.dataclient.WikiSite
import org.wikipedia.history.HistoryEntry
import org.wikipedia.page.PageActivity
import org.wikipedia.page.PageTitle
import org.wikipedia.random.RandomActivity
import org.wikipedia.search.SearchActivity

/**
 * Handles OACP actions for Wikipedia.
 *
 * - search: opens SearchActivity with query pre-filled
 * - open_article: opens PageActivity with the article loaded
 * - random_article: opens RandomActivity
 */
class OacpActionReceiver : OacpReceiver() {

    override fun onAction(
        context: Context,
        action: String,
        params: OacpParams,
        requestId: String?
    ): OacpResult? {
        return when {
            action.endsWith(".oacp.ACTION_SEARCH") -> {
                val query = params.getString("query")
                if (query.isNullOrBlank()) {
                    return OacpResult.error("missing_parameters", "Search query is required")
                }
                val intent = SearchActivity.newIntent(
                    context,
                    Constants.InvokeSource.INTENT_UNKNOWN,
                    query
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                OacpResult.success("Searching Wikipedia for \"$query\"")
            }
            action.endsWith(".oacp.ACTION_OPEN_ARTICLE") -> {
                val title = params.getString("title")
                if (title.isNullOrBlank()) {
                    return OacpResult.error("missing_parameters", "Article title is required")
                }
                val langCode = params.getString("language")
                    ?: WikipediaApp.instance.languageState.appLanguageCode
                val wikiSite = WikiSite.forLanguageCode(langCode)
                val pageTitle = PageTitle(title, wikiSite)
                val entry = HistoryEntry(pageTitle, HistoryEntry.SOURCE_EXTERNAL_LINK)
                val intent = PageActivity.newIntentForNewTab(context, entry, pageTitle)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                OacpResult.success("Opening Wikipedia article: $title")
            }
            action.endsWith(".oacp.ACTION_RANDOM_ARTICLE") -> {
                val langCode = WikipediaApp.instance.languageState.appLanguageCode
                val wikiSite = WikiSite.forLanguageCode(langCode)
                val intent = RandomActivity.newIntent(
                    context,
                    wikiSite,
                    Constants.InvokeSource.INTENT_UNKNOWN
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                OacpResult.success("Opening random Wikipedia article")
            }
            else -> null
        }
    }
}
