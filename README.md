# SpRandom

Splittable, serializable pseudorandom number generation in Scala.

SpRandom produces random numbers that are reproducible in a multi-threaded context.
It also allows the random state to be serialized and deserialized.

SpRandom builds on top of the `java.util.SplittableRandom` class.
It exposes the following features:
* A `zip` method that facilitates reproducibility in a multi-threaded context
* Serializability by persisting the underlying seed (SplittableRandom is not serializable)
* Multiple, convenient ways to instantiate a random state
* A variety of convenience methods to generate pseudorandom outcomes

## Usage

A `Random` object can be instantiated from an integer seed, a long seed, or another `Random` object.

```scala
import io.citrine.random.Random

val rng = Random(17L)
val rng2 = rng.split() // an independent stream
val uniformRandom = rng.between(1.5, 2.5) // draw from a uniform distribution between 1.5 and 2.5
val normalRandom = rng.nextGaussian(mean = 5.0, stdDev = 0.1) // draw from a normal with mean 5.0 and standard deviation 0.1
val orderedVector = (1 to 10).toVector
val shuffledVector = rng.shuffle(orderedVector) // random permutation of the range (1 to 10)
```

To get reproducible results in a multi-threaded context, use `.zip`.
Imagine that we have some sequence, `items`, that we want to process in parallel.
Processing is done by the stochastic, reproducible method `heavyStochasticComputation`.
The following call is then reproducible.

```scala
rng.zip(items).par.map { case (thisRng, item) => heavyStochasticComputation(item, thisRng) }
```
