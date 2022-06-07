package io.citrine.random

import io.citrine.random.GeneratorType._
import io.citrine.random.generators.{ApacheJumpableRandomGenerator, JavaSplittableRandomGenerator}
import org.apache.commons.rng.LongJumpableUniformRandomProvider
import org.apache.commons.rng.simple.RandomSource

import java.util.SplittableRandom

object RandomFactory {

  def from(generator: GeneratorType, seed: Long = scala.util.Random.nextLong()): Random = {
    generator match {
      case JavaSplittableRandom =>
        val baseRng = new SplittableRandom(seed)
        new JavaSplittableRandomGenerator(baseRng)
      case ApacheXoRoShiRo128PlusPlus =>
        val baseRng = RandomSource.XO_RO_SHI_RO_128_PP.create(seed).asInstanceOf[LongJumpableUniformRandomProvider]
        new ApacheJumpableRandomGenerator(baseRng)
    }
  }
}
