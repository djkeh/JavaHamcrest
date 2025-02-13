package org.hamcrest.core;

import org.hamcrest.test.AbstractMatcherTest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.test.MatcherAssertions.*;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;

public class IsCollectionContainingTest extends AbstractMatcherTest {

    @Override
    protected Matcher<?> createMatcher() {
        return hasItem(equalTo("irrelevant"));
    }

    @Test
    public void testMatchesACollectionThatContainsAnElementMatchingTheGivenMatcher() {
        Matcher<Iterable<? extends String>> itemMatcher = hasItem(equalTo("a"));

        assertMatches("should match list that contains 'a'",
                itemMatcher, asList("a", "b", "c"));
    }

    @Test
    public void testDoesNotMatchCollectionThatDoesntContainAnElementMatchingTheGivenMatcher() {
        final Matcher<Iterable<? extends String>> matcher1 = hasItem(mismatchable("a"));
        assertMismatchDescription("mismatches were: [mismatched: b, mismatched: c]", matcher1, asList("b", "c"));

        final Matcher<Iterable<? extends String>> matcher2 = hasItem(equalTo("a"));
        assertMismatchDescription("was empty", matcher2, new ArrayList<String>());
    }

    @Test
    public void testDoesNotMatchNull() {
        assertDoesNotMatch("should not matches null", hasItem(equalTo("a")), null);
    }

    @Test
    public void testHasAReadableDescription() {
        assertDescription("a collection containing \"a\"", hasItem(equalTo("a")));
    }

    @Test
    public void testCanMatchItemWhenCollectionHoldsSuperclass() // Issue 24
    {
      final Set<Number> s = new HashSet<>();
      s.add(2);
      assertThat(s, new IsCollectionContaining<>(new IsEqual<Number>(2)));
      assertThat(s, IsCollectionContaining.hasItem(2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMatchesAllItemsInCollection() {
        final Matcher<Iterable<? extends String>> matcher1 = hasItems(equalTo("a"), equalTo("b"), equalTo("c"));
        assertMatches("should match list containing all items",
                matcher1,
                asList("a", "b", "c"));

        final Matcher<Iterable<? extends String>> matcher2 = hasItems("a", "b", "c");
        assertMatches("should match list containing all items (without matchers)",
                matcher2,
                asList("a", "b", "c"));

        final Matcher<Iterable<? extends String>> matcher3 = hasItems(equalTo("a"), equalTo("b"), equalTo("c"));
        assertMatches("should match list containing all items in any order",
                matcher3,
                asList("c", "b", "a"));

        final Matcher<Iterable<? extends String>> matcher4 = hasItems(equalTo("a"), equalTo("b"), equalTo("c"));
        assertMatches("should match list containing all items plus others",
                matcher4,
                asList("e", "c", "b", "a", "d"));

        final Matcher<Iterable<? extends String>> matcher5 = hasItems(equalTo("a"), equalTo("b"), equalTo("c"));
        assertDoesNotMatch("should not match list unless it contains all items",
                matcher5,
                asList("e", "c", "b", "d")); // 'a' missing
    }

    private static Matcher<? super String> mismatchable(final String string) {
      return new TypeSafeDiagnosingMatcher<String>() {
        @Override
        protected boolean matchesSafely(String item, Description mismatchDescription) {
          if (string.equals(item))
            return true;

          mismatchDescription.appendText("mismatched: " + item);
          return false;
        }

        @Override
        public void describeTo(Description description) {
          description.appendText("mismatchable: " + string);
        }
      };
    }

}
