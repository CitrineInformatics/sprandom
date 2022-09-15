package io.citrine.random

import java.util.SplittableRandom
import org.scalatest.funsuite.AnyFunSuite

class SpRandomTest extends AnyFunSuite {

  test("Constructors appropriately use the passed seed or base RNG.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rngFromBaseRng = SpRandom(baseRng)
    val rngFromSeed = SpRandom(seed)
    assert(rngFromBaseRng.nextLong() == rngFromSeed.nextLong())

    val rng0 = SpRandom()
    val rng1 = SpRandom()
    assert(rng0.nextLong() != rng1.nextLong())
  }

  test("split() produces a new independent stream.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val baseRngSplit = baseRng.split()
    val rng = SpRandom(seed)
    val rngSplit = rng.split()
    assert(baseRngSplit.nextLong() == rngSplit.nextLong())
    val rngSplitAgain = rng.split()
    rngSplitAgain.nextLong() // Advance rng state to ensure it doesn't change other splits.
    assert(baseRngSplit.nextLong() == rngSplit.nextLong())
  }

  test("between() works for Long-valued bounds.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = SpRandom(seed)

    val min = 12L
    val max = 3127974L
    (1 to 10).foreach { _ =>
      assert(baseRng.nextLong(min, max) == rng.between(min, max))
    }
  }

  test("between() works for Int-valued bounds.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = SpRandom(seed)

    val min = 12
    val max = 3127974
    (1 to 10).foreach { _ =>
      assert(baseRng.nextInt(min, max) == rng.between(min, max))
    }
  }

  test("between() works for Double-valued bounds.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = SpRandom(seed)

    val min = 0.1243
    val max = 22.1331
    (1 to 10).foreach { _ =>
      assert(baseRng.nextDouble(min, max) == rng.between(min, max))
    }
  }

  test("nextBoolean() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = SpRandom(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextBoolean() == rng.nextBoolean())
    }
  }

  test("nextDouble() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = SpRandom(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextDouble() == rng.nextDouble())
    }
  }

  test("nextInt() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = SpRandom(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextInt == rng.nextInt())
      assert(baseRng.nextInt(10) == rng.nextInt(10))
    }
  }

  test("nextLong() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = SpRandom(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextLong == rng.nextLong())
      assert(baseRng.nextLong(10) == rng.nextLong(10))
    }
  }

  test("The statistical properties of the nextGaussian() convenience function.") {}

  test("Check statistical properties of shuffle.") {
    // Check that each value is rearranged to a new index with uniform probability over the possible new
  }
}
