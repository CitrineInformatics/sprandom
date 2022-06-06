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

  /** TODO: Replace with Apache impl when https://github.com/apache/commons-rng/pull/111 is released. */
  def between(minInclusive: Double, maxExclusive: Double): Double = {
    var v = baseRng.nextDouble()
    // This expression allows (bound - origin) to be infinite
    // origin + (bound - origin) * v
    // == origin - origin * v + bound * v
    v = (1f - v) * minInclusive + v * maxExclusive
    if (v >= maxExclusive) {
      // Correct rounding
      v = math.nextDown(maxExclusive)
    }
    v
  }

  /** TODO: Replace with Apache impl when https://github.com/apache/commons-rng/pull/111 is released. */
  def between(minInclusive: Int, maxExclusive: Int): Int = {
    val n = maxExclusive - minInclusive
    if (n > 0) {
      baseRng.nextInt(n) + minInclusive
    } else {
      // Range too large to fit in a positive integer.
      // Use simple rejection.
      var v = baseRng.nextInt()
      while (v < minInclusive || v >= maxExclusive) {
        v = baseRng.nextInt()
      }
      v
    }
  }

  /** TODO: Replace with Apache impl when https://github.com/apache/commons-rng/pull/111 is released. */
  def between(minInclusive: Long, maxExclusive: Long): Long = {
    val n = maxExclusive - minInclusive
    if (n > 0) {
      baseRng.nextLong(maxExclusive) + minInclusive
    } else {
      // Range too large to fit in a positive integer.
      // Use simple rejection.
      var v = baseRng.nextLong()
      while (v < minInclusive || v >= maxExclusive) {
        v = baseRng.nextLong()
      }
      v
    }
  }
}
