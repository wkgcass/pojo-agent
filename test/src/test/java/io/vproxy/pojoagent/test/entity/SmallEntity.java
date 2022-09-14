package io.vproxy.pojoagent.test.entity;

import io.vproxy.pojoagent.api.*;

import java.util.BitSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Pojo
public class SmallEntity implements Entity {
    private String name;
    private boolean ok;

    public String getName() {
        return name;
    }

    public boolean isOk() {
        return ok;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return "SmallEntity{" +
            "name='" + name + '\'' +
            ", ok=" + ok +
            '}';
    }

    @Override
    public void doSet(BitSet bitset) {
        if (bitset.get(0)) {
            setName(null);
        }
        if (bitset.get(1)) {
            setOk(false);
        }
    }

    @PojoCaller
    @Override
    public void doSet2(BitSet bitset) {
        if (bitset.get(0)) {
            PojoAgent.setField(name);
        }
        if (bitset.get(1)) {
            PojoAgent.setField(ok);
        }
    }

    @PojoCaller
    @Override
    public void doUnset(BitSet bitset) {
        if (bitset.get(0)) {
            PojoAgent.unsetField(getName());
        }
        if (bitset.get(1)) {
            PojoAgent.unsetField(isOk());
        }
    }

    @PojoCaller
    @Override
    public void doUnsetByField(BitSet bitset) {
        if (bitset.get(0)) {
            PojoAgent.unsetField(name);
        }
        if (bitset.get(1)) {
            PojoAgent.unsetField(ok);
        }
    }

    @PojoCaller
    @Override
    public void doAssert(BitSet bitset) {
        if (bitset.get(0)) {
            assertTrue(PojoAgent.fieldIsSet(getName()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(getName()));
        }
        if (bitset.get(1)) {
            assertTrue(PojoAgent.fieldIsSet(isOk()));
        } else {
            assertFalse(PojoAgent.fieldIsSet(isOk()));
        }
    }

    @PojoCaller
    @Override
    public void doAssertByField(BitSet bitset) {
        if (bitset.get(0)) {
            assertTrue(PojoAgent.fieldIsSet(name));
        } else {
            assertFalse(PojoAgent.fieldIsSet(name));
        }
        if (bitset.get(1)) {
            assertTrue(PojoAgent.fieldIsSet(ok));
        } else {
            assertFalse(PojoAgent.fieldIsSet(ok));
        }
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
