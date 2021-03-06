/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.filter;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link StartWithStringRecordFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class StartWithStringRecordFilterTest extends BaseRecordFilterTest {

    private StartWithStringRecordFilter startWithStringRecordFilter;

    @Test
    public void whenTheRecordStartsWithExpectedPrefixThenItShouldBeFiltered() {
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix1");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord1)).isTrue();
    }

    @Test
    public void whenTheRecordStartsWithOneOfTheExpectedPrefixesThenItShouldBeFiltered() {
        startWithStringRecordFilter = new StartWithStringRecordFilter("prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord1)).isTrue();
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord2)).isTrue();
    }

    /*
     * Test the negate behavior
     */

    @Test
    public void whenTheRecordStartsWithExpectedPrefixThenItShouldNotBeFiltered() {
        startWithStringRecordFilter = new StartWithStringRecordFilter(true, "prefix1");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord1)).isFalse();
    }

    @Test
    public void whenTheRecordStartsWithOneOfTheExpectedPrefixesThenItShouldNotBeFiltered() {
        startWithStringRecordFilter = new StartWithStringRecordFilter(true, "prefix1", "prefix2");
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord1)).isFalse();
        assertThat(startWithStringRecordFilter.filterRecord(stringRecord2)).isFalse();
    }

}
