package io.citrine.random

sealed trait GeneratorType

object GeneratorType {
  case object JavaSplittableRandom extends GeneratorType
  case object ApacheXoRoShiRo128PlusPlus extends GeneratorType
}
