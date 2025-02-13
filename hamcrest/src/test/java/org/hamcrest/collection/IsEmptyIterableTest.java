package org.hamcrest.collection;

import org.hamcrest.test.AbstractMatcherTest;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.test.MatcherAssertions.*;
import static org.hamcrest.collection.IsEmptyIterable.emptyIterable;

public class IsEmptyIterableTest extends AbstractMatcherTest {

    @Override
    protected Matcher<Iterable<?>> createMatcher() {
        return emptyIterable();
    }

    @Test
    public void testMatchesAnEmptyIterable() {
        assertMatches("empty iterable", createMatcher(), emptyCollection());
    }

    @Test
    public void testDoesNotMatchAnIterableWithItems() {
        assertDoesNotMatch("iterable with an item", createMatcher(), collectionOfValues());
    }

    @Test
    public void testHasAReadableDescription() {
        assertDescription("an empty iterable", createMatcher());
    }

    @Test
    public void testCompiles() {
        needs(IsEmptyIterable.emptyIterableOf(String.class));
    }

    private void needs(@SuppressWarnings("unused") Matcher<Iterable<String>> bar) { }

    private static Collection<String> collectionOfValues() {
        return new ArrayList<>(asList("one", "three"));
    }

    private static Collection<Integer> emptyCollection() {
        return new ArrayList<>();
    }

}
