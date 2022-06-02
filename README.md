# random

Reliable random number generation in Scala.

The primary reason for this repo is reproducibility in multi-threaded contexts, so we wrap up Java.utl.SplittableRandom with some Scala methods that are useful in Mithril/Lolo.

One of the goals of the DARE team, which we’ve been working towards for years, is to make our stochastic computations reproducible when given the same initial state. This is primarily to make our tests more reliable, but it might also be valuable for users to have repeatable training and candidate generation.

This was moved out of Mithril in order to make it available to lolo.
