package io.citrine.random

import io.citrine.random.generators.{ApacheXoRoShiRo128PlusPlus, JavaSplittableRandomGenerator}

import scala.collection.BuildFrom
import scala.collection.mutable.ArrayBuffer

trait Random {

  /**
    * Split off an independent parallel stream of random numbers.
    *
    * @return a new instance of Random whose stream of random numbers are independent of the present stream.
    */
  def split(): Random

  /**
    * Generate a uniformly random Boolean.
    *
    * @return a random Boolean
    */
  def nextBoolean(): Boolean

  /**
    * Generate a uniformly random Double between zero (inclusive) and one (exclusive).
    *
    * @return a uniformly random number on the unit interval
    */
  def nextDouble(): Double

  /**
    * Generate a uniformly random Int on the whole allowed range.
    *
    * @return a random Int
    */
  def nextInt(): Int

  /**
    * Generate a uniformly random Int between zero and a given exclusive upper bound.
    *
    * @param bound exclusive upper bound
    * @return a random Int between 0 (inclusive) and bound (exclusive)
    */
  def nextInt(bound: Int): Int

  /**
    * Generate a uniformly random Long on the whole allowed range.
    *
    * @return a random Long
    */
  def nextLong(): Long

  /**
    * Generate a uniformly random Long between zero and a given exclusive upper bound.
    *
    * @param bound exclusive upper bound
    * @return a random Long between 0 (inclusive) and bound (exclusive)
    */
  def nextLong(bound: Long): Long

  /**
    * Generate a uniformly random Double in a given interval.
    *
    * @param minInclusive inclusive minimum of the interval
    * @param maxExclusive exclusive maximum of the interval
    * @return uniformly random Double between minInclusive and maxExclusive
    */
  def between(minInclusive: Double, maxExclusive: Double): Double

  /**
    * Generate a uniformly random Int in a given interval.
    *
    * @param minInclusive
    *   inclusive minimum of the interval
    * @param maxExclusive
    *   exclusive maximum of the interval
    * @return
    *   uniformly random Int between minInclusive and maxExclusive
    */
  def between(minInclusive: Int, maxExclusive: Int): Int

  /**
    * Generate a uniformly random Long in a given interval.
    *
    * @param minInclusive inclusive minimum of the interval
    * @param maxExclusive exclusive maximum of the interval
    * @return uniformly random Long between minInclusive and maxExclusive
    */
  def between(minInclusive: Long, maxExclusive: Long): Long

  /**
    * Generate a Gaussian-distributed random Double with mean 0 and variance 1
    *
    * @return a draw from the standard normal distribution.
    */
  def nextGaussian(): Double = {
    // This uses the Box-Muller transform.
    // It's a bit inefficient, especially since it throws away the second Gaussian available from the two uniforms.
    math.sqrt(-2.0 * math.log(nextDouble())) * math.cos(2.0 * math.Pi * nextDouble())
  }

  /**
    * Generate a Gaussian-distributed random variable with given mean and standard deviation.
    *
    * @param mean of the distribution
    * @param stdDev of the distribution
    */
  def nextGaussian(mean: Double, stdDev: Double): Double = {
    mean + stdDev * nextGaussian()
  }

  /**
    * Returns a new collection of the same type in a randomly chosen order.
    *
    * Taken from scala.util.Random
    *
    * @param xs the values to shuffle
    * @param bf builder for type CC[T] from type CC[T]
    * @tparam T the type of elements in xs
    * @tparam CC the type of collection
    * @return a collection of type CC[T] with elements uniformly shuffled
    */
  def shuffle[T, CC[X] <: IterableOnce[X]](xs: CC[T])(implicit bf: BuildFrom[CC[T], T, CC[T]]): CC[T] = {
    val buf = new ArrayBuffer[T] ++= xs

    def swap(i1: Int, i2: Int): Unit = {
      val tmp = buf(i1)
      buf(i1) = buf(i2)
      buf(i2) = tmp
    }

    for (n <- buf.length to 2 by -1) {
      val k = nextInt(n)
      swap(n - 1, k)
    }

    (bf.newBuilder(xs) ++= buf).result()
  }
}

object Random {

  import RandomType._

  def default: Random = build(JavaSplittableRandom)

  def apply(seed: Long = scala.util.Random.nextLong()): Random = build(JavaSplittableRandom, seed)

  def build(generator: RandomType, seed: Long = scala.util.Random.nextLong()): Random = {
    generator match {
      case JavaSplittableRandom       => new JavaSplittableRandomGenerator(seed)
      case ApacheXoRoShiRo128PlusPlus => new ApacheXoRoShiRo128PlusPlus(seed)
    }
  }
}
