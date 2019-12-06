package li.doerf.feeder.scraper

import li.doerf.feeder.scraper.dto.FeedDto

sealed class DownloadResult
data class DownloadSuccess(val url: String, val content: String) : DownloadResult()
data class DownloadError(val reason: String) : DownloadResult()

sealed class ParserResult
data class ParserSuccess(val url: String, val feedDto: FeedDto) : ParserResult()
data class ParserError(val reason: String) : ParserResult()

sealed class PersisterResult
data class PersisterSuccess(val firstDownload: Boolean, val itemsUpdated: Boolean, val pkey: Long): PersisterResult()
