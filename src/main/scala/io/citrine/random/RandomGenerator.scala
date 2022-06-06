package io.citrine.random

sealed trait RandomGenerator

object RandomGenerator {
  case object JavaSplittableRandom extends RandomGenerator
  case object ApacheXoRoShiRo128PlusPlus extends RandomGenerator
}
