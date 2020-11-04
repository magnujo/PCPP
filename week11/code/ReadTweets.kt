import java.io.File

fun main() {
  // TODO
}

fun readLines(): Sequence<String> = sequence<String> {
  val aPlaceOnYourDisk = "Somewhere/AirlineTweets2015.csv"
  val file = File( aPlaceOnYourDisk )
  val reader = file.bufferedReader()
  var line:String? = reader.readLine()
  while (line != null) {
    yield( line )
    line = reader.readLine()
  }
}

fun tweetFromLine(line: String): Tweet? {
    val cols : List<String> = line.split(",")
    if (cols.size == 15)
      return Tweet(cols[0], cols[1],cols[2],cols[3],cols[4],cols[5],cols[6],cols[7],
        cols[8],cols[9],cols[10],cols[11],cols[12],cols[13],cols[14])
    else
      return null
}

data class Tweet (
  var tweet_id : String,
  var airline_sentiment : String,
  var airline_sentiment_confidence : String,
  var negativereason : String,
  var negativereason_confidence : String,
  var airline : String,
  var airline_sentiment_gold : String,
  var name : String,
  var negativereason_gold : String,
  var retweet_count : String,
  var text : String,
  var tweet_coord : String,
  var tweet_created : String,
  var tweet_location : String,
  var user_timezone : String
  )
