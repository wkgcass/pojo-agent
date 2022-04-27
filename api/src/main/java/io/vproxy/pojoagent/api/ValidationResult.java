package io.vproxy.pojoagent.api;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ValidationResult {
    public final int action;
    public final Set<String> missing = new HashSet<>();
    public final Set<String> redundant = new HashSet<>();
    public final Set<String> isnull = new HashSet<>();

    public ValidationResult(int action) {
        this.action = action;
    }

    public boolean pass() {
        return missing.isEmpty() && redundant.isEmpty() && isnull.isEmpty();
    }

    public boolean fail() {
        return !missing.isEmpty() || !redundant.isEmpty() || !isnull.isEmpty();
    }

    private String buildErrorMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("validation failed: ");
        boolean separator = false;
        if (!missing.isEmpty()) {
            separator = true;
            sb.append("the following fields are missing: ").append(missing);
        }
        if (!redundant.isEmpty()) {
            if (separator) {
                sb.append(", ");
            }
            separator = true;
            sb.append("the following fields are redundant: ").append(redundant);
        }
        if (!isnull.isEmpty()) {
            if (separator) {
                sb.append(", ");
            }
            //noinspection UnusedAssignment
            separator = true;
            sb.append("the following fields must not be null: ").append(isnull);
        }
        return sb.toString();
    }

    public <EX extends RuntimeException> ValidationResult throwOnError(Function<String, EX> errBuilder) throws EX {
        if (pass()) {
            return this;
        }
        throw errBuilder.apply(buildErrorMessage());
    }

    public void addMissingIf(String name, int action, int mask) {
        if ((action & mask) == action) {
            missing.add(name);
        }
    }

    public void addRedundantIf(String name, int action, int mask) {
        if ((action & mask) == action) {
            redundant.add(name);
        }
    }

    public void addNullIf(String name, int action, int mask) {
        if ((action & mask) == action) {
            isnull.add(name);
        }
    }

    @Override
    public String toString() {
        if (pass()) {
            return "ValidationResult(no error)";
        } else {
            return "ValidationResult(" + buildErrorMessage() + ")";
        }
    }
}
