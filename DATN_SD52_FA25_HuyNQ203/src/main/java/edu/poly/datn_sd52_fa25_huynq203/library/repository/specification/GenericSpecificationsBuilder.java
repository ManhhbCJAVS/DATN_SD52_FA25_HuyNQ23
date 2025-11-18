package edu.poly.datn_sd52_fa25_huynq203.library.repository.specification;


import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SearchOperation.*;

public final class GenericSpecificationsBuilder<T> {

    private final List<SpecSearchCriteria> params;

    public GenericSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    // API
    public GenericSpecificationsBuilder<T> with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public GenericSpecificationsBuilder<T> with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        //TODO: searchOperation may be null -> throw exception

        if (searchOperation == EQUALITY) { // the operation may be complex operation
            final boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
            final boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

            if (startWithAsterisk && endWithAsterisk) {
                searchOperation = CONTAINS;
            } else if (startWithAsterisk) {
                searchOperation = ENDS_WITH;
            } else if (endWithAsterisk) {
                searchOperation = STARTS_WITH;
            }
        }
        params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value));
        return this;
    }

    public Specification<T> build() {
        if (params.isEmpty()) return null;

        Specification<T> result = new GenericSpecification<>(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            GenericSpecification<T> currentSpec = new GenericSpecification<>(params.get(i));
            result = params.get(i).isOrPredicate()
                    ? result.or(currentSpec)
                    : result.and(currentSpec);
//                    ? Specification.where(result).or(new GenericSpecification<>(params.get(i)))
//                    : Specification.where(result).and(new GenericSpecification<>(params.get(i)));
        }
        return result;
    }

}
