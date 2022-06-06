package io.citrine.random

sealed trait RandomType

object RandomType {
  case object JavaSplittableRandom extends RandomType
  case object ApacheXoRoShiRo128PlusPlus extends RandomType
}
