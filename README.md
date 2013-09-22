[![Build Status](https://secure.travis-ci.org/avh4/magnum-di.png?branch=master)](http://travis-ci.org/avh4/magnum-di)

## Magnum D.I.

> If basketball was all there was to life, you'd be set.

Unfortunately there's more to life than basketball, which is why you probably need
[dependency injection](http://martinfowler.com/articles/injection.html).  **Magnum D.I.**
is a dependency injection framework that assists in automatically wiring up your dependencies.

[Guice](https://code.google.com/p/google-guice/), [Spring DI](http://spring.io/search?q=dependency+injection), and
 [PicoContainer](http://picocontainer.codehaus.org/) are other popular dependency injection frameworks.
 **Magnum D.I.** is superior in the following ways:

- Containers are [immutable](http://clojure.org/rationale) and [persistent](http://en.wikipedia.org/wiki/Persistent_data_structure)
- Supporting scoped dependencies for arbitrary scopes is a core feature, and thus simple to use
- Configuration of containers/modules can be done with a single constructor call
- Declaring dependencies is done by adding constructor parameters

**Magnum D.I.** is intended to enforce its opinions on your code, and may not be suitable for use in legacy projects.

### Usage

Add the following dependencies to your `pom.xml`:

```xml
  <dependency>
    <groupId>net.avh4.util.di</groupId>
    <artifactId>magnum-di</artifactId>
    <version>0.0.0</version>
  </dependency>
```

### Building your DeLorean

(This DI example was taken from Public Object's blog post, ["What's a Hierarchical Injector?"](http://blog.publicobject.com/2008/06/whats-hierarchical-injector.html))

Suppose we have the following components:

```
    public class DeLorean {
        public DeLorean(TimeCircuits timeCircuits, FluxCapacitor fluxCapacitor, EnergySource energySource) { }
    }

    interface FluxCapacitor { }
    public class RealFluxCapacitor implements FluxCapacitor {
        public RealFluxCapacitor(TimeCircuits timeCircuits) { }
    }

    public class TimeCircuits {
        public TimeCircuits(EnergySource energySource) { }
    }

    interface EnergySource { }
    public static class Plutonium implements EnergySource { }
    public static class LightningBolt implements EnergySource { }
```

Our goal is to configure our container to be able to make DeLoreans with different energy sources.

First, we will make a container with the reusable components.

```
MagnumDI magnum = new MagnumDI(TimeCircuits.class, RealFluxCapacitor.class);
```

Now we can create DeLoreans!  In each case, we will specify additional scoped dependencies (for our
DeLoreans we will specify the EnergySource).  Any other objects that depend on EnergySource in the scope
of this DeLorean will get the same EnergySource instance.

```
final DeLorean deLorean1 = magnum.get(DeLorean.class, Plutonium.class);
final DeLorean deLorean2 = magnum.get(DeLorean.class, LightningBolt.class);
```

## Build commands

* [Mutation coverage](http://pitest.org/): `mvn clean test org.pitest:pitest-maven:mutationCoverage`
