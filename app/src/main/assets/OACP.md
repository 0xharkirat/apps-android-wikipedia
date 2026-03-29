# Wikipedia — OACP Context

## What this app does
Wikipedia lets the user search for encyclopedia topics and open Wikipedia articles for reading. The OACP integration in this app is focused on discovery and reading flows, not editing, account management, or moderation tasks.

## Vocabulary
- "Wikipedia", "wiki", and "Wikipedia app" all refer to this app.
- "article", "page", and "entry" all mean a Wikipedia article to open for reading.
- "look up", "search", and "find on Wikipedia" all map to article search.
- "random", "surprise me", and "something interesting" map to opening a random article.
- language codes like "en", "es", "de", and "fr" refer to Wikipedia language editions.
- language names like "English", "Spanish", "German", and "French" also refer to Wikipedia language editions.

## Capabilities
- `search_articles` opens the Wikipedia search UI with the provided query and shows matching results.
- `open_article` opens a specific article title directly. If `language_code` is omitted, the app uses the current app language.
- `open_random_article` opens a random article. If `language_code` is omitted, the app uses the current app language.

## Constraints
- These capabilities require the Wikipedia app UI to come to the foreground.
- Wikipedia needs network access to search for and load article content.
- `open_article` works best when the requested title is close to an actual Wikipedia article title.
- If the user sounds unsure about the exact title, prefer `search_articles` over `open_article`.
- This integration does not currently expose editing, reading list management, or moderation workflows.

## Confirmation And Execution
- `search_articles` can be announced as "Searching Wikipedia."
- `open_article` can be announced as "Opening the Wikipedia article."
- `open_random_article` can be announced as "Opening a random Wikipedia article."
- all current Wikipedia capabilities can run without spoken confirmation

## Examples
- "Search Wikipedia for Ada Lovelace"
- "Look up black holes on Wikipedia"
- "Open the Wikipedia article for Kyoto"
- "Show me the Spanish Wikipedia page for Barcelona"
- "Open a random Wikipedia article"
- "Surprise me with something from Wikipedia in German"
