package io.vproxy.pojoagent.test.entity;

import io.vproxy.pojoagent.api.*;
import io.vproxy.pojoagent.api.template.PojoUpdateFrom;
import io.vproxy.pojoagent.api.template.PojoValidate;

import java.util.Arrays;
import java.util.BitSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Pojo
public class SimpleEntity implements Entity, PojoUpdateFrom<SimpleEntity>, PojoValidate {
    @MustExist(1)
    @MustNotExist(2)
    @MustNotNull(4)
    private String a;
    @MustExist(1)
    @MustNotExist(2)
    private int i;
    @MustExist(1)
    @MustNotExist(2)
    private long l;
    @MustExist(1)
    @MustNotExist(2)
    private double d;
    private float f;
    private char c;
    private boolean z;
    private short s;
    private byte b;
    @MustNotNull(4)
    private String[] aa;
    @MustNotNull(4)
    private int[] ai;
    @MustNotNull(4)
    private String[][] aaa;
    @MustNotNull(4)
    private int[][] aii;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public boolean isZ() {
        return z;
    }

    public void setZ(boolean z) {
        this.z = z;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public String[] getAa() {
        return aa;
    }

    public void setAa(String[] aa) {
        this.aa = aa;
    }

    public int[] getAi() {
        return ai;
    }

    public void setAi(int[] ai) {
        this.ai = ai;
    }

    public String[][] getAaa() {
        return aaa;
    }

    public void setAaa(String[][] aaa) {
        this.aaa = aaa;
    }

    public int[][] getAii() {
        return aii;
    }

    public void setAii(int[][] aii) {
        this.aii = aii;
    }

    @Override
    public String toString() {
        return "SimpleEntity{" +
            "a='" + a + '\'' +
            ", i=" + i +
            ", l=" + l +
            ", d=" + d +
            ", f=" + f +
            ", c=" + c +
            ", z=" + z +
            ", s=" + s +
            ", b=" + b +
            ", aa=" + Arrays.toString(aa) +
            ", ai=" + Arrays.toString(ai) +
            ", aaa=" + Arrays.toString(aaa) +
            ", aii=" + Arrays.toString(aii) +
            '}';
    }

    @Override
    public void doSet(BitSet bitset) {
        if (bitset.get(0)) {
            this.setA(null);
        }
        if (bitset.get(1)) {
            this.setI(0);
        }
        if (bitset.get(2)) {
            this.setL(0L);
        }
        if (bitset.get(3)) {
            this.setD(0.0);
        }
        if (bitset.get(4)) {
            this.setF(0f);
        }
        if (bitset.get(5)) {
            this.setC((char) 0);
        }
        if (bitset.get(6)) {
            this.setZ(false);
        }
        if (bitset.get(7)) {
            this.setS((short) 0);
        }
        if (bitset.get(8)) {
            this.setB((byte) 0);
        }
        if (bitset.get(9)) {
            this.setAa(null);
        }
        if (bitset.get(10)) {
            this.setAi(null);
        }
        if (bitset.get(11)) {
            this.setAaa(null);
        }
        if (bitset.get(12)) {
            this.setAii(null);
        }
    }

    @PojoCaller
    @Override
    public void doSet2(BitSet bitset) {
        if (bitset.get(0)) {
            PojoAgent.setField(a);
        }
        if (bitset.get(1)) {
            PojoAgent.setField(i);
        }
        if (bitset.get(2)) {
            PojoAgent.setField(l);
        }
        if (bitset.get(3)) {
            PojoAgent.setField(d);
        }
        if (bitset.get(4)) {
            PojoAgent.setField(f);
        }
        if (bitset.get(5)) {
            PojoAgent.setField(c);
        }
        if (bitset.get(6)) {
            PojoAgent.setField(z);
        }
        if (bitset.get(7)) {
            PojoAgent.setField(s);
        }
        if (bitset.get(8)) {
            PojoAgent.setField(b);
        }
        if (bitset.get(9)) {
            PojoAgent.setField(aa);
        }
        if (bitset.get(10)) {
            PojoAgent.setField(ai);
        }
        if (bitset.get(11)) {
            PojoAgent.setField(aaa);
        }
        if (bitset.get(12)) {
            PojoAgent.setField(aii);
        }
    }

    @PojoCaller
    @Override
    public void doUnset(BitSet bitset) {
        if (bitset.get(0)) {
            PojoAgent.unsetField(this.getA());
        }
        if (bitset.get(1)) {
            PojoAgent.unsetField(this.getI());
        }
        if (bitset.get(2)) {
            PojoAgent.unsetField(this.getL());
        }
        if (bitset.get(3)) {
            PojoAgent.unsetField(this.getD());
        }
        if (bitset.get(4)) {
            PojoAgent.unsetField(this.getF());
        }
        if (bitset.get(5)) {
            PojoAgent.unsetField(this.getC());
        }
        if (bitset.get(6)) {
            PojoAgent.unsetField(this.isZ());
        }
        if (bitset.get(7)) {
            PojoAgent.unsetField(this.getS());
        }
        if (bitset.get(8)) {
            PojoAgent.unsetField(this.getB());
        }
        if (bitset.get(9)) {
            PojoAgent.unsetField(this.getAa());
        }
        if (bitset.get(10)) {
            PojoAgent.unsetField(this.getAi());
        }
        if (bitset.get(11)) {
            PojoAgent.unsetField(this.getAaa());
        }
        if (bitset.get(12)) {
            PojoAgent.unsetField(this.getAii());
        }
    }

    @PojoCaller
    @Override
    public void doUnsetByField(BitSet bitset) {
        if (bitset.get(0)) {
            PojoAgent.unsetField(a);
        }
        if (bitset.get(1)) {
            PojoAgent.unsetField(i);
        }
        if (bitset.get(2)) {
            PojoAgent.unsetField(l);
        }
        if (bitset.get(3)) {
            PojoAgent.unsetField(d);
        }
        if (bitset.get(4)) {
            PojoAgent.unsetField(f);
        }
        if (bitset.get(5)) {
            PojoAgent.unsetField(c);
        }
        if (bitset.get(6)) {
            PojoAgent.unsetField(z);
        }
        if (bitset.get(7)) {
            PojoAgent.unsetField(s);
        }
        if (bitset.get(8)) {
            PojoAgent.unsetField(b);
        }
        if (bitset.get(9)) {
            PojoAgent.unsetField(aa);
        }
        if (bitset.get(10)) {
            PojoAgent.unsetField(ai);
        }
        if (bitset.get(11)) {
            PojoAgent.unsetField(aaa);
        }
        if (bitset.get(12)) {
            PojoAgent.unsetField(aii);
        }
    }

    @PojoCaller
    @Override
    public void doAssert(BitSet bitset) {
        if (bitset.get(0)) {
            assertTrue(PojoAgent.fieldIsSet(this.getA()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getA()));
        }
        if (bitset.get(1)) {
            assertTrue(PojoAgent.fieldIsSet(this.getI()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getI()));
        }
        if (bitset.get(2)) {
            assertTrue(PojoAgent.fieldIsSet(this.getL()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getL()));
        }
        if (bitset.get(3)) {
            assertTrue(PojoAgent.fieldIsSet(this.getD()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getD()));
        }
        if (bitset.get(4)) {
            assertTrue(PojoAgent.fieldIsSet(this.getF()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getF()));
        }
        if (bitset.get(5)) {
            assertTrue(PojoAgent.fieldIsSet(this.getC()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getC()));
        }
        if (bitset.get(6)) {
            assertTrue(PojoAgent.fieldIsSet(this.isZ()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.isZ()));
        }
        if (bitset.get(7)) {
            assertTrue(PojoAgent.fieldIsSet(this.getS()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getS()));
        }
        if (bitset.get(8)) {
            assertTrue(PojoAgent.fieldIsSet(this.getB()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getB()));
        }
        if (bitset.get(9)) {
            assertTrue(PojoAgent.fieldIsSet(this.getAa()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getAa()));
        }
        if (bitset.get(10)) {
            assertTrue(PojoAgent.fieldIsSet(this.getAi()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getAi()));
        }
        if (bitset.get(11)) {
            assertTrue(PojoAgent.fieldIsSet(this.getAaa()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getAaa()));
        }
        if (bitset.get(12)) {
            assertTrue(PojoAgent.fieldIsSet(this.getAii()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(this.getAii()));
        }
    }

    @PojoCaller
    @Override
    public void doAssertByField(BitSet bitset) {
        if (bitset.get(0)) {
            assertTrue(PojoAgent.fieldIsSet(a));
        } else {
            assertFalse(PojoAgent.fieldIsSet(a));
        }
        if (bitset.get(1)) {
            assertTrue(PojoAgent.fieldIsSet(i));
        } else {
            assertFalse(PojoAgent.fieldIsSet(i));
        }
        if (bitset.get(2)) {
            assertTrue(PojoAgent.fieldIsSet(l));
        } else {
            assertFalse(PojoAgent.fieldIsSet(l));
        }
        if (bitset.get(3)) {
            assertTrue(PojoAgent.fieldIsSet(d));
        } else {
            assertFalse(PojoAgent.fieldIsSet(d));
        }
        if (bitset.get(4)) {
            assertTrue(PojoAgent.fieldIsSet(f));
        } else {
            assertFalse(PojoAgent.fieldIsSet(f));
        }
        if (bitset.get(5)) {
            assertTrue(PojoAgent.fieldIsSet(c));
        } else {
            assertFalse(PojoAgent.fieldIsSet(c));
        }
        if (bitset.get(6)) {
            assertTrue(PojoAgent.fieldIsSet(z));
        } else {
            assertFalse(PojoAgent.fieldIsSet(z));
        }
        if (bitset.get(7)) {
            assertTrue(PojoAgent.fieldIsSet(s));
        } else {
            assertFalse(PojoAgent.fieldIsSet(s));
        }
        if (bitset.get(8)) {
            assertTrue(PojoAgent.fieldIsSet(b));
        } else {
            assertFalse(PojoAgent.fieldIsSet(b));
        }
        if (bitset.get(9)) {
            assertTrue(PojoAgent.fieldIsSet(aa));
        } else {
            assertFalse(PojoAgent.fieldIsSet(aa));
        }
        if (bitset.get(10)) {
            assertTrue(PojoAgent.fieldIsSet(ai));
        } else {
            assertFalse(PojoAgent.fieldIsSet(ai));
        }
        if (bitset.get(11)) {
            assertTrue(PojoAgent.fieldIsSet(aaa));
        } else {
            assertFalse(PojoAgent.fieldIsSet(aaa));
        }
        if (bitset.get(12)) {
            assertTrue(PojoAgent.fieldIsSet(aii));
        } else {
            assertFalse(PojoAgent.fieldIsSet(aii));
        }
    }

    public SimpleEntity preUpdateFromCalled = null;
    public SimpleEntity postUpdateFromCalled = null;
    public Integer preValidateCalled = null;
    public ValidationResult postValidateCalled = null;

    @PojoAutoImpl
    @Override
    public void updateFrom(SimpleEntity another) {
        throw new RequirePojoAutoImplException();
    }

    @Override
    public void preUpdateFrom(SimpleEntity another) {
        preUpdateFromCalled = another;
    }

    @Override
    public void postUpdateFrom(SimpleEntity another) {
        postUpdateFromCalled = another;
    }

    @PojoAutoImpl
    @Override
    public ValidationResult validate(int action) {
        throw new RequirePojoAutoImplException();
    }

    @Override
    public void preValidate(int action) {
        preValidateCalled = action;
    }

    @Override
    public ValidationResult postValidate(ValidationResult result) {
        postValidateCalled = result;
        return result;
    }

    @PojoAutoImpl
    @Override
    public void setAllFields() {
        throw new RequirePojoAutoImplException();
    }

    @PojoAutoImpl
    @Override
    public void unsetAllFields() {
        throw new RequirePojoAutoImplException();
    }
}
