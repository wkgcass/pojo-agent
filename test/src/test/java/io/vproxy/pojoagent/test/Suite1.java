package io.vproxy.pojoagent.test;

import io.vproxy.pojoagent.test.cases.TestSetUnsetFields;
import io.vproxy.pojoagent.test.cases.TestUpdateFrom;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestSetUnsetFields.class,
    TestUpdateFrom.class,
})
public class Suite1 {
}
