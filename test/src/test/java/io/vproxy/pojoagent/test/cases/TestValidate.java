package io.vproxy.pojoagent.test.cases;

import io.vproxy.pojoagent.api.ValidationResult;
import io.vproxy.pojoagent.test.entity.BigEntity;
import io.vproxy.pojoagent.test.entity.SimpleEntity;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TestValidate {
    @Test
    public void missing() {
        Set<String> expectedMissingFields = new HashSet<>(Arrays.asList("i", "l"));
        SimpleEntity simple = new SimpleEntity();
        simple.setA(null);
        simple.setD(0.0);
        ValidationResult result = simple.validate(1);

        assertEquals(expectedMissingFields, result.missing);
        assertEquals(0, result.redundant.size());
        assertEquals(0, result.isnull.size());

        assertEquals(Integer.valueOf(1), simple.preValidateCalled);
        assertSame(result, simple.postValidateCalled);
    }

    @Test
    public void redundant() {
        Set<String> expectedRedundantFields = new HashSet<>(Arrays.asList("a", "d"));
        SimpleEntity simple = new SimpleEntity();
        simple.setA(null);
        simple.setD(0.0);
        ValidationResult result = simple.validate(2);

        assertEquals(0, result.missing.size());
        assertEquals(expectedRedundantFields, result.redundant);
        assertEquals(0, result.isnull.size());

        assertEquals(Integer.valueOf(2), simple.preValidateCalled);
        assertSame(result, simple.postValidateCalled);
    }

    @Test
    public void isnull() {
        Set<String> expectedNullFields = new HashSet<>(Arrays.asList("a", "aa", "ai"));
        SimpleEntity simple = new SimpleEntity();
        simple.setA(null);
        simple.setAa(null);
        simple.setAi(null);
        simple.setAaa(new String[0][]);
        simple.setAii(new int[0][]);
        ValidationResult result = simple.validate(4);

        assertEquals(0, result.missing.size());
        assertEquals(0, result.redundant.size());
        assertEquals(expectedNullFields, result.isnull);

        assertEquals(Integer.valueOf(4), simple.preValidateCalled);
        assertSame(result, simple.postValidateCalled);
    }

    @Test
    public void privatePrePost() {
        BigEntity big = new BigEntity();
        ValidationResult result = big.validate(123);

        assertEquals(Integer.valueOf(123), big.preValidateCalled);
        assertSame(result, big.postValidateCalled);
    }
}
