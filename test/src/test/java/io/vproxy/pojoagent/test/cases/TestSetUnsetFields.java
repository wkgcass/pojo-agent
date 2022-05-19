package io.vproxy.pojoagent.test.cases;

import io.vproxy.pojoagent.test.entity.BigEntity;
import io.vproxy.pojoagent.test.entity.Entity;
import io.vproxy.pojoagent.test.entity.SimpleEntity;
import io.vproxy.pojoagent.test.entity.SmallEntity;
import org.junit.Test;

import java.util.BitSet;
import java.util.function.Supplier;

public class TestSetUnsetFields {
    @Test
    public void smallSetUnset() {
        testSetUnset(1, SmallEntity::new);
    }

    @Test
    public void simpleSetUnset() {
        testSetUnset(13, SimpleEntity::new);
    }

    @Test
    public void bigSetUnset() {
        testSetUnset(70, 3, BigEntity::new);
    }

    private static void testSetUnset(final int fieldCount, Supplier<Entity> supplier) {
        testSetUnset(fieldCount, fieldCount, supplier);
    }

    private static void testSetUnset(final int fieldCount, int setFieldsLimit, Supplier<Entity> supplier) {
        for (int setCount = 1; setCount <= setFieldsLimit; ++setCount) {
            int[] indexes = new int[setCount];
            for (int i = 0; i < setCount; ++i) {
                indexes[i] = -1;
            }
            recursiveTestSetUnset(fieldCount, 0, indexes, supplier);
        }
    }

    private static void recursiveTestSetUnset(final int fieldCount, int indexToFill, int[] indexes, Supplier<Entity> supplier) {
        if (indexToFill == indexes.length) {
            for (int i = 0; i < indexes.length; ++i) {
                Entity entity = supplier.get();
                BitSet bitset = new BitSet();
                for (int index : indexes) {
                    bitset.set(index);
                }
                entity.doSet(bitset);
                entity.doAssert(bitset);
                entity.doAssertByField(bitset);

                for (int index : indexes) {
                    BitSet unsetBitset = new BitSet();
                    unsetBitset.set(index);
                    entity.doUnset(unsetBitset);

                    bitset.clear(index);
                    entity.doAssert(bitset);
                    entity.doAssertByField(bitset);
                }

                for (int index : indexes) {
                    BitSet unsetBitset = new BitSet();
                    unsetBitset.set(index);
                    entity.doUnsetByField(unsetBitset);

                    bitset.clear(index);
                    entity.doAssert(bitset);
                    entity.doAssertByField(bitset);
                }
            }
            return;
        }
        for (int i = (indexToFill == 0 ? 0 : indexes[indexToFill - 1] + 1); i <= fieldCount; ++i) {
            indexes[indexToFill] = i;
            recursiveTestSetUnset(fieldCount, indexToFill + 1, indexes, supplier);
        }
    }
}
