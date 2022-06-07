package io.citrine.random

import java.util.SplittableRandom
import org.scalatest.funsuite.AnyFunSuite

class RandomTest extends AnyFunSuite {

  test("Constructors appropriately use the passed seed or base RNG.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rngFromSeed = Random(seed)
    assert(baseRng.nextLong() == rngFromSeed.nextLong())

    val rng0 = Random()
    val rng1 = Random()
    assert(rng0.nextLong() != rng1.nextLong())
  }

  test("split() produces a new independent stream.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val baseRngSplit = baseRng.split()
    val rng = Random(seed)
    val rngSplit = rng.split()
    assert(baseRngSplit.nextLong() == rngSplit.nextLong())
    val rngSplitAgain = rng.split()
    rngSplitAgain.nextLong() // Advance rng state to ensure it doesn't change other splits.
    assert(baseRngSplit.nextLong() == rngSplit.nextLong())
  }

  test("between() works for Long-valued bounds.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = Random(seed)

    val min = 12L
    val max = 3127974L
    (1 to 10).foreach { _ =>
      assert(baseRng.nextLong(min, max) == rng.between(min, max))
    }
  }

  test("between() works for Int-valued bounds.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = Random(seed)

    val min = 12
    val max = 3127974
    (1 to 10).foreach { _ =>
      assert(baseRng.nextInt(min, max) == rng.between(min, max))
    }
  }

  test("between() works for Double-valued bounds.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = Random(seed)

    val min = 0.1243
    val max = 22.1331
    (1 to 10).foreach { _ =>
      assert(baseRng.nextDouble(min, max) == rng.between(min, max))
    }
  }

  test("nextBoolean() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = Random(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextBoolean() == rng.nextBoolean())
    }
  }

  test("nextDouble() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = Random(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextDouble() == rng.nextDouble())
    }
  }

  test("nextInt() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = Random(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextInt == rng.nextInt())
      assert(baseRng.nextInt(10) == rng.nextInt(10))
    }
  }

  test("nextLong() passes the call off to the baseRng.") {
    val seed = 23794L
    val baseRng = new SplittableRandom(seed)
    val rng = Random(seed)

    (1 to 10).foreach { _ =>
      assert(baseRng.nextLong == rng.nextLong())
      assert(baseRng.nextLong(10) == rng.nextLong(10))
    }
  }

  test("Check the statistical properties of the shuffle function.") {
    // Check that each value is rearranged to a new index with uniform probability over the possible new indices
    val seed = 123894L
    val rng = Random(seed)

    val nItems = 10
    val inputs = (1 to nItems).toVector
    var countInEachPosition =
      inputs.map { x => x -> inputs.indices.map { i => i -> 0 }.toMap }.toMap[Int, Map[Int, Int]]

    val nTrials = 100
    (1 to nTrials).foreach { _ =>
      val outputs = rng.shuffle(inputs)
      assert(inputs == outputs.sorted)

      outputs.zipWithIndex.foreach { case (x, i) =>
        var thisItem = countInEachPosition(x)
        thisItem += i -> (thisItem(i) + 1)
        countInEachPosition += x -> thisItem
      }
    }

    // Critical values for chi2 distribution with DOF=10-1=9
    val chiSqCriticalValueLower = 2.088
    val chiSqCriticalValueUpper = 21.66

    val expected = nTrials.toDouble / nItems
    countInEachPosition.foreach { case (_, m) =>
      val chiSq = m.values.map { count => math.pow(count - expected, 2) / expected }.sum
      assert(chiSq > chiSqCriticalValueLower)
      assert(chiSq < chiSqCriticalValueUpper)
    }
  }
}
