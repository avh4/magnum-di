package net.avh4.util.di.magnum.integration;

import net.avh4.util.di.magnum.MagnumDI;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example from http://blog.publicobject.com/2008/06/whats-hierarchical-injector.html
 */
public class DeloreanTest {

    @Test
    public void test() throws Exception {
        MagnumDI magnum = new MagnumDI(TimeCircuits.class, RealFluxCapacitor.class);
        final DeLorean deLorean1 = magnum.add(Plutonium.class).get(DeLorean.class);
        assertThat(deLorean1.fluxCapacitor).isInstanceOf(RealFluxCapacitor.class);
        assertThat(deLorean1.timeCircuits).isSameAs(((RealFluxCapacitor) deLorean1.fluxCapacitor).timeCircuits);
        assertThat(deLorean1.energySource).isInstanceOf(Plutonium.class);
        assertThat(deLorean1.timeCircuits.energySource).isSameAs(deLorean1.energySource);

        final DeLorean deLorean2 = magnum.add(LightningBolt.class).get(DeLorean.class);
        assertThat(deLorean2.fluxCapacitor).isInstanceOf(RealFluxCapacitor.class);
        assertThat(deLorean2.timeCircuits).isSameAs(((RealFluxCapacitor) deLorean2.fluxCapacitor).timeCircuits);
        assertThat(deLorean2.energySource).isInstanceOf(LightningBolt.class);
        assertThat(deLorean2.timeCircuits.energySource).isSameAs(deLorean2.energySource);
    }

    public static class DeLorean {
        final TimeCircuits timeCircuits;
        final FluxCapacitor fluxCapacitor;
        final EnergySource energySource;

        public DeLorean(TimeCircuits timeCircuits, FluxCapacitor fluxCapacitor, EnergySource energySource) {
            this.timeCircuits = timeCircuits;
            this.fluxCapacitor = fluxCapacitor;
            this.energySource = energySource;
        }
    }

    interface FluxCapacitor {
        boolean isFluxing();
    }

    public static class RealFluxCapacitor implements FluxCapacitor {
        final TimeCircuits timeCircuits;
        boolean isFluxing;

        public RealFluxCapacitor(TimeCircuits timeCircuits) {
            this.timeCircuits = timeCircuits;
        }

        public boolean isFluxing() {
            return isFluxing;
        }
    }

    public static class TimeCircuits {
        Date whereYouveBeen;
        Date whereYouAre;
        Date whereYourGoing;
        final EnergySource energySource;

        public TimeCircuits(EnergySource energySource) {
            this.energySource = energySource;
        }
    }

    interface EnergySource {
        void generateOnePointTwentyOneGigawatts();
    }

    public static class Plutonium implements EnergySource {
        @Override public void generateOnePointTwentyOneGigawatts() {
        }
    }

    public static class LightningBolt implements EnergySource {
        @Override public void generateOnePointTwentyOneGigawatts() {
        }
    }
}
