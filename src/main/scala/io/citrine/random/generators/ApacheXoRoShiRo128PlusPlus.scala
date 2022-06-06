package io.citrine.random.generators

import io.citrine.random.Random
import org.apache.commons.rng.JumpableUniformRandomProvider
import org.apache.commons.rng.simple.RandomSource

protected[random] class ApacheXoRoShiRo128PlusPlus private (val baseRng: JumpableUniformRandomProvider)
    extends ApacheRandomGenerator {

  /**
    * Split off an independent parallel stream of random numbers.
    *
    * @return a new instance of Random whose stream of random numbers are independent of the present stream.
    */
  override def split(): Random = {
    val jumped = baseRng.jump().asInstanceOf[JumpableUniformRandomProvider]
    new ApacheXoRoShiRo128PlusPlus(jumped)
  }
}

object ApacheXoRoShiRo128PlusPlus {
  def apply(seed: Long): ApacheXoRoShiRo128PlusPlus = {
    // TODO: Does their public API support a way to avoid casting?
    val baseRng = RandomSource.XO_SHI_RO_128_PP.create(seed).asInstanceOf[JumpableUniformRandomProvider]
    new ApacheXoRoShiRo128PlusPlus(baseRng)
  }
}
