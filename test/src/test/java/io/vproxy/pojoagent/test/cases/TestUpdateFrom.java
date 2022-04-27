package io.vproxy.pojoagent.test.cases;

import io.vproxy.pojoagent.test.entity.BigEntity;
import io.vproxy.pojoagent.test.entity.SimpleEntity;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestUpdateFrom {
    @Test
    public void simple() {
        SimpleEntity a = new SimpleEntity();
        a.setA("aaa");
        a.setZ(false);
        a.setC('a');
        a.setI(0);
        a.setL(123L);
        a.setD(0.0);

        SimpleEntity b = new SimpleEntity();
        b.setA("bbb");
        b.setZ(true);
        b.setC('b');
        b.setI(987);
        b.setL(456L);
        b.setD(1.6);
        b.setF(3.2f);

        b.updateFrom(a);

        assertEquals("aaa", b.getA());
        assertFalse(b.isZ());
        assertEquals('a', b.getC());
        assertEquals(0, b.getI());
        assertEquals(123, b.getL());
        assertEquals(0.0, b.getD(), 0.0);
        assertEquals(3.2f, b.getF(), 0.0);

        assertSame(a, b.preUpdateFromCalled);
        assertSame(a, b.postUpdateFromCalled);
    }

    @Test
    public void privatePrePost() {
        BigEntity a = new BigEntity();
        a.setF0("f0");
        a.setF1("f1");

        BigEntity b = new BigEntity();
        b.setF0("xx");
        b.setF1("yy");
        b.setF2("zz");

        b.updateFrom(a);

        assertEquals("f0", b.getF0());
        assertEquals("f1", b.getF1());
        assertEquals("zz", b.getF2());

        assertSame(a, b.preUpdateFromCalled);
        assertSame(a, b.postUpdateFromCalled);
    }
}
