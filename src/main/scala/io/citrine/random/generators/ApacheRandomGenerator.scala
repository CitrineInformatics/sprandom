package io.citrine.random.generators

import io.citrine.random.Random
import org.apache.commons.rng.UniformRandomProvider

protected[random] trait ApacheRandomGenerator extends Random {

  def baseRng: UniformRandomProvider

  def nextBoolean(): Boolean = baseRng.nextBoolean()

  def nextDouble(): Double = baseRng.nextDouble()

  def nextInt(): Int = baseRng.nextInt()

  def nextInt(bound: Int): Int = baseRng.nextInt(bound)

  def nextLong(): Long = baseRng.nextLong()

  def nextLong(bound: Long): Long = baseRng.nextLong(bound)

  def between(minInclusive: Double, maxExclusive: Double): Double = ???

  def between(minInclusive: Int, maxExclusive: Int): Int = ???

  def between(minInclusive: Long, maxExclusive: Long): Long = ???
}
