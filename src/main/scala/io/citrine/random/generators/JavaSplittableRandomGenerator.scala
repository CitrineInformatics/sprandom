package io.citrine.random.generators

import io.citrine.random.Random

import java.util.SplittableRandom

protected[random] class JavaSplittableRandomGenerator private (val baseRng: SplittableRandom) extends Random {

  override def split(): Random = new JavaSplittableRandomGenerator(baseRng.split())

  override def nextBoolean(): Boolean = baseRng.nextBoolean()

  override def nextDouble(): Double = baseRng.nextDouble()

  override def nextInt(): Int = baseRng.nextInt()

  override def nextInt(bound: Int): Int = baseRng.nextInt(bound)

  override def nextLong(): Long = baseRng.nextLong()

  override def nextLong(bound: Long): Long = baseRng.nextLong(bound)

  override def between(minInclusive: Double, maxExclusive: Double): Double =
    baseRng.nextDouble(minInclusive, maxExclusive)

  override def between(minInclusive: Int, maxExclusive: Int): Int =
    baseRng.nextInt(minInclusive, maxExclusive)

  override def between(minInclusive: Long, maxExclusive: Long): Long =
    baseRng.nextLong(minInclusive, maxExclusive)
}

object JavaSplittableRandomGenerator {
  def apply(seed: Long): JavaSplittableRandomGenerator = {
    val baseRng = new SplittableRandom(seed)
    new JavaSplittableRandomGenerator(baseRng)
  }
}
