fun main() {
  foo().forEach(::println)
  FooIterator().forEach(::println)
}

fun foo() = sequence<Int> {
  var i = 17
  i -= 10
  yield(i)
  i += 18/9
  yield(i)
  i = 125/25+2*2*2
  yield(i)
}

class FooMachine {
  var i = 0 // just some integer value
  var state = 0

  fun callMe():Int {
    var returnValue : Int
    when (state) {
        0 -> {i = 17
              i -= 10
              returnValue = i
              state = 1 }
        1 -> {i += 18/9
              returnValue = i
              state = 2 }
        2 -> {i = 125/25+2*2*2
              returnValue = i
              state = -1 }
        else -> throw IllegalStateException("Bad boy!")
      }
    return returnValue
  }
}

class FooIterator : Iterator<Int> {
  val machine = FooMachine()
  var nextValue : Int? = null

  init{
    if (machine.state != -1 )
      nextValue = machine.callMe()
  }

  override fun hasNext():Boolean {
    return nextValue != null
  }

  override fun next(): Int {
    if ( nextValue == null) throw NoSuchElementException("Bad boy!")
    val ret : Int = nextValue!!
    if (machine.state != -1 )
      nextValue = machine.callMe()
    else
      nextValue = null
    return ret
  }
}
