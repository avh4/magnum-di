package net.avh4.util.di.magnum.integration;

import net.avh4.util.di.magnum.MagnumDI;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RobotTest {

    private Robot robot1;
    private Robot robot2;

    @Before
    public void setUp() throws Exception {
        MagnumDI magnum = new MagnumDI(Clock.class, Leg.class, RightToes.class, LeftToes.class);

        robot1 = createRobot(magnum.add(RobotHead.class));
        robot2 = createRobot(magnum.add(ChickenHead.class));
    }

    private Robot createRobot(MagnumDI magnum) {
        magnum = magnum.add(Power.class);
        return new Robot(
                magnum.get(Head.class),
                magnum.add(LeftFoot.class).get(Leg.class),
                magnum.add(RightFoot.class).get(Leg.class),
                magnum.get(Power.class));
    }

    @Test
    public void shouldShareClock() throws Exception {
        assertThat(robot1.power.clock).isSameAs(robot2.power.clock);
    }

    @Test
    public void shouldHaveDifferentPower() throws Exception {
        assertThat(robot1.power).isNotSameAs(robot2.power);
    }

    @Test
    public void withinRobot_shouldHaveCommonPower() throws Exception {
        assertThat(robot1.power)
                .isSameAs(robot1.head.power)
                .isSameAs(((LeftFoot) robot1.left.foot).power)
                .isSameAs(((RightFoot) robot1.right.foot).power);
        assertThat(robot2.power)
                .isSameAs(robot2.head.power)
                .isSameAs(((LeftFoot) robot2.left.foot).power)
                .isSameAs(((RightFoot) robot2.right.foot).power);
    }

    @Test
    public void shouldHaveCorrectFeet() throws Exception {
        assertThat(robot1.left.foot).isInstanceOf(LeftFoot.class);
        assertThat(robot1.right.foot).isInstanceOf(RightFoot.class);
        assertThat(robot2.left.foot).isInstanceOf(LeftFoot.class);
        assertThat(robot2.right.foot).isInstanceOf(RightFoot.class);
    }

    @Test
    public void shouldHaveCorrectHead() throws Exception {
        assertThat(robot1.head).isInstanceOf(RobotHead.class);
        assertThat(robot2.head).isInstanceOf(ChickenHead.class);
    }

    public static class Robot {
        final Head head;
        final Leg left, right;
        final Power power;

        public Robot(Head head, Leg left, Leg right, Power power) {
            this.head = head;
            this.left = left;
            this.right = right;
            this.power = power;
        }
    }

    public static abstract class Head {
        final Power power;

        protected Head(Power power) {
            this.power = power;
        }
    }

    public static class RobotHead extends Head {
        public RobotHead(Power power) {
            super(power);
        }
    }

    public static class ChickenHead extends Head {
        public ChickenHead(Power power) {
            super(power);
        }
    }

    public static class Leg {
        final Foot foot;

        public Leg(Foot foot) {
            this.foot = foot;
        }
    }

    public interface Foot {}

    public static class LeftFoot implements Foot {
        final Power power;
        final LeftToes toes;

        public LeftFoot(Power power, LeftToes toes) {
            this.power = power;
            this.toes = toes;
        }
    }

    public static class LeftToes {}

    public static class RightFoot implements Foot {
        final Power power;
        final RightToes toes;

        public RightFoot(Power power, RightToes toes) {
            this.power = power;
            this.toes = toes;
        }
    }

    public static class RightToes {}

    public static class Power {
        final Clock clock;

        public Power(Clock clock) {
            this.clock = clock;
        }
    }

    public static class Clock {}
}
