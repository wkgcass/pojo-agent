package io.vproxy.pojoagent.test.entity;

import java.util.BitSet;

public interface Entity {
    void doSet(BitSet bitset);

    void doUnset(BitSet bitset);

    void doAssert(BitSet bitset);
}
