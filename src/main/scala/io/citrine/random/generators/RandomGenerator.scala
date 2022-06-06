package io.citrine.random.generators

sealed trait RandomGenerator

object RandomGenerator {
  case object JavaSplittableRandom extends RandomGenerator
  case object ApacheXoRoShiRo128PlusPlus extends RandomGenerator
}
