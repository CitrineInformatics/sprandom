package io.citrine.random

import java.util.SplittableRandom
import scala.collection.BuildFrom
import scala.collection.mutable.ArrayBuffer

class SpRandom private (seed: SpRandom.RandomSeed) extends Serializable {
  @transient private lazy val baseRng: SplittableRandom = seed match {
    case SpRandom.EmptySeed()     => new SplittableRandom
    case SpRandom.IntSeed(value)  => new SplittableRandom(value.toLong)
    case SpRandom.LongSeed(value) => new SplittableRandom(value)
    case SpRandom.RngSeed(value)  => value.baseRng.split()
  }

  /**
    * Split off an independent parallel stream of random numbers.
    *
    * @return a new instance of SpRandom whose stream of random numbers are independent of the present stream.
    */
  def split(): SpRandom = SpRandom(this)

  /**
    * Generate a uniformly random Long in a given interval.
    *
    * @param minInclusive inclusive minimum of the interval
    * @param maxExclusive exclusive maximum of the interval
    * @return uniformly random Long between minInclusive and maxExclusive
    */
  def between(minInclusive: Long, maxExclusive: Long): Long = baseRng.nextLong(minInclusive, maxExclusive)

  /**
    * Generate a uniformly random Int in a given interval.
    *
    * @param minInclusive inclusive minimum of the interval
    * @param maxExclusive exclusive maximum of the interval
    * @return uniformly random Int between minInclusive and maxExclusive
    */
  def between(minInclusive: Int, maxExclusive: Int): Int = baseRng.nextInt(minInclusive, maxExclusive)

  /**
    * Generate a uniformly random Double in a given interval.
    *
    * @param minInclusive inclusive minimum of the interval
    * @param maxExclusive exclusive maximum of the interval
    * @return uniformly random Double between minInclusive and maxExclusive
    */
  def between(minInclusive: Double, maxExclusive: Double): Double = baseRng.nextDouble(minInclusive, maxExclusive)

  /**
    * Generate a uniformly random Boolean.
    *
    * @return a random Boolean
    */
  def nextBoolean(): Boolean = baseRng.nextBoolean()

  /**
    * Generate a uniformly random Double between zero (inclusive) and one (exclusive).
    *
    * @return a uniformly random number on the unit interval
    */
  def nextDouble(): Double = baseRng.nextDouble()

  /**
    * Generate a uniformly random Int on the whole allowed range.
    *
    * @return a random Int
    */
  def nextInt(): Int = baseRng.nextInt()

  /**
    * Generate a uniformly random Int between zero and a given exclusive upper bound.
    *
    * @param bound exclusive upper bound
    * @return a random Int between 0 (inclusive) and bound (exclusive)
    */
  def nextInt(bound: Int): Int = baseRng.nextInt(bound)

  /**
    * Generate a uniformly random Long on the whole allowed range.
    *
    * @return a random Long
    */
  def nextLong(): Long = baseRng.nextLong()

  /**
    * Generate a uniformly random Long between zero and a given exclusive upper bound.
    *
    * @param bound exclusive upper bound
    * @return a random Long between 0 (inclusive) and bound (exclusive)
    */
  def nextLong(bound: Long): Long = baseRng.nextLong(bound)

  /**
    * Generate a Gaussian-distributed random Double with mean 0 and variance 1
    *
    * @return a draw from the standard normal distribution.
    */
  def nextGaussian(): Double = {
    // This uses the Box-Muller transform.
    // It's a bit inefficient, especially since it throws away the second Gaussian available from the two uniforms.
    Math.sqrt(-2.0 * Math.log(nextDouble())) * Math.cos(2.0 * Math.PI * nextDouble())
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

  /**
    * Zip the rng with the iterable collection, splitting the rng on each new element.
    *
    * This method is useful for producing a stream of independent random generators for multi-threaded applications.
    *
    * @param xs the collection to zip with
    * @param bf builder for type CC[(Random, T)] from type CC[T]
    * @tparam T  the type of elements in xs
    * @tparam CC the type of collection
    * @return a collection of type CC[(Random, T)] with elements zipped with split random generators
    */
  def zip[T, CC[X] <: IterableOnce[X]](
      xs: CC[T]
  )(implicit bf: BuildFrom[CC[T], (SpRandom, T), CC[(SpRandom, T)]]): CC[(SpRandom, T)] = {
    bf.fromSpecific(xs)(xs.iterator.map(x => (split(), x)))
  }
}

object SpRandom {
  sealed trait RandomSeed
  case class EmptySeed() extends RandomSeed
  case class IntSeed(value: Int) extends RandomSeed
  case class LongSeed(value: Long) extends RandomSeed
  case class RngSeed(value: SpRandom) extends RandomSeed

  def apply(): SpRandom = new SpRandom(EmptySeed())
  def apply(seed: Int): SpRandom = new SpRandom(IntSeed(seed))
  def apply(seed: Long): SpRandom = new SpRandom(LongSeed(seed))
  def apply(seed: SpRandom): SpRandom = new SpRandom(RngSeed(seed))

  /** Construct a default SpRandom object seeded from the global RNG. */
  def default: SpRandom = SpRandom(scala.util.Random.nextLong())

  /** Construct a scala Random object from the random state. */
  def scalaRandom(rng: SpRandom): scala.util.Random = new scala.util.Random(rng.nextLong())
}
