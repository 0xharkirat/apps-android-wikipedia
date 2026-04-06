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

/**
 * Transparent routing activity for OACP actions.
 * Receives the intent from Hark, launches the right Wikipedia screen, and finishes.
 */
class OacpRouterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
        finish()
    }

    private fun handleIntent(intent: Intent?) {
        val action = intent?.action ?: return

        when {
            action.endsWith(".oacp.ACTION_SEARCH") -> {
                val query = intent.getStringExtra("query")
                    ?: intent.extras?.keySet()?.firstOrNull { it.endsWith("query") }
                        ?.let { intent.getStringExtra(it) }
                startActivity(
                    SearchActivity.newIntent(this, Constants.InvokeSource.INTENT_UNKNOWN, query)
                )
            }
            action.endsWith(".oacp.ACTION_OPEN_ARTICLE") -> {
                val title = intent.getStringExtra("title")
                    ?: intent.extras?.keySet()?.firstOrNull { it.endsWith("title") }
                        ?.let { intent.getStringExtra(it) }
                if (title != null) {
                    val langCode = intent.getStringExtra("language")
                        ?: WikipediaApp.instance.languageState.appLanguageCode
                    val wikiSite = WikiSite.forLanguageCode(langCode)
                    val pageTitle = PageTitle(title, wikiSite)
                    val entry = HistoryEntry(pageTitle, HistoryEntry.SOURCE_EXTERNAL_LINK)
                    startActivity(PageActivity.newIntentForNewTab(this, entry, pageTitle))
                }
            }
            action.endsWith(".oacp.ACTION_RANDOM_ARTICLE") -> {
                val langCode = WikipediaApp.instance.languageState.appLanguageCode
                val wikiSite = WikiSite.forLanguageCode(langCode)
                startActivity(
                    RandomActivity.newIntent(this, wikiSite, Constants.InvokeSource.INTENT_UNKNOWN)
                )
            }
        }
    }
}
