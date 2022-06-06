package io.citrine.random.generators

import io.citrine.random.Random
import org.apache.commons.rng.JumpableUniformRandomProvider
import org.apache.commons.rng.simple.RandomSource

protected[random] class ApacheXoRoShiRo128PlusPlus private (val baseRng: JumpableUniformRandomProvider)
    extends ApacheRandomGenerator {

  def this(seed: Long) {
    this(RandomSource.XO_SHI_RO_128_PP.create(seed).asInstanceOf[JumpableUniformRandomProvider])
  }

  /**
    * Split off an independent parallel stream of random numbers.
    *
    * @return a new instance of Random whose stream of random numbers are independent of the present stream.
    */
  override def split(): Random = {
    baseRng.jump() match {
      case j: JumpableUniformRandomProvider => new ApacheXoRoShiRo128PlusPlus(j)
      case _                                => new ApacheXoRoShiRo128PlusPlus(nextLong()) // Bad
    }
  }
}
