package io.citrine.random

import java.util.SplittableRandom
import java.io._
import breeze.stats.distributions.{ChiSquared, RandBasis}
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

  test("Random can be round-trip serialized.") {
    val seed = Random(23794L)
    val rng = Random(seed)

    def roundRobinBinary[T](obj: T): T = {
      val tmpFile: File = File.createTempFile("tmp", ".csv")
      tmpFile.deleteOnExit()
      // val outStream = new FileOutputStream(tmpFile)
      val outStream = new ByteArrayOutputStream()
      val oos: ObjectOutputStream = new ObjectOutputStream(outStream)
      oos.writeObject(obj)
      oos.close()

      // val inStream = new FileInputStream(tmpFile)
      val inStream = new ByteArrayInputStream(outStream.toByteArray)
      new ObjectInputStream(inStream).readObject().asInstanceOf[T]
    }

    val roundTrip = roundRobinBinary(rng)
    assert(rng.nextLong() == roundTrip.nextLong())

    val rngSplit = rng.split()
    val roundTripSplit = roundTrip.split()
    assert(rngSplit.nextLong() == roundTripSplit.nextLong())
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

    val min = 1.1243
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

  test("The statistical properties of the nextGaussian() convenience function.") {
    val seed = 2934L
    val rng = Random(seed)
    val nSamples = 1000

    def cdfStandardNormal(x: Double): Double = {
      0.5 * (1.0 + breeze.numerics.erf(x / Math.sqrt(2.0)))
    }

    // Kolmogorov-Smirnov statistic  against normal with known mean m and standard deviation std.
    def ks(X: Seq[Double], m: Double, std: Double): Double = {
      X.sorted.zipWithIndex.map {
        case (x, i) =>
          Math.abs(i / X.length.toDouble - cdfStandardNormal((x - m) / std))
      }.max
    }

    def testNormality(X: Seq[Double], meanExpected: Double, stdExpected: Double): Unit = {
      val ksStat = ks(X, meanExpected, stdExpected)
      assert(
        ksStat < 1.94947 / Math.sqrt(X.length.toDouble),
        "Kolmogorov-Smirnov test statistic exceeded p=0.001 threshold."
      )
    }

    Seq(-100.0, -10.0, 0.0, 10.0, 200.0).foreach { meanExpected =>
      Seq(0.001, 1.0, 100.0).foreach { stdExpected =>
        val X = Seq.fill[Double](nSamples)(rng.nextGaussian(meanExpected, stdExpected))
        testNormality(X, meanExpected, stdExpected)
      }
    }

    // Check version without arguments is standard normal.
    testNormality(Seq.fill[Double](nSamples)(rng.nextGaussian()), 0.0, 1.0)
  }

  test("Check statistical properties of shuffle.") {
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

      outputs.zipWithIndex.foreach {
        case (x, i) =>
          var thisItem = countInEachPosition(x)
          thisItem += (i -> (thisItem(i) + 1))
          countInEachPosition += (x -> thisItem)
      }
    }
    val expected = nTrials.toDouble / nItems
    val chiSqDist = ChiSquared(nItems.toDouble - 1)(RandBasis.withSeed(rng.nextInt()))
    val chiSqCriticalValueLower = chiSqDist.inverseCdf(0.01)
    val chiSqCriticalValueUpper = chiSqDist.inverseCdf(0.99)
    countInEachPosition.foreach {
      case (_, m) =>
        val chiSq = m.values.map { count => Math.pow(count - expected, 2) / expected }.sum
        assert(chiSq > chiSqCriticalValueLower)
        assert(chiSq < chiSqCriticalValueUpper)
    }
  }
}
