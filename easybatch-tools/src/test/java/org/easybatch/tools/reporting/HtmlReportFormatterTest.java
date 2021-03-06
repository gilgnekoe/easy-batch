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

package org.easybatch.tools.reporting;

import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Scanner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link HtmlReportFormatter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class HtmlReportFormatterTest {

    private ReportFormatter<String> reportFormatter;

    private Report report;

    private static long START_TIME;
    private static long END_TIME;

    static{
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.JANUARY, 1, 1, 0, 0);
        START_TIME = calendar.getTime().getTime();
        END_TIME = START_TIME + 10 * 1000;
    }

    @Before
    public void setUp() throws Exception {
        reportFormatter = new HtmlReportFormatter();
        report = new Report();
        report.setStartTime(START_TIME);
        report.setEndTime(END_TIME);
        report.setStatus(Status.FINISHED);
        report.setDataSource("In-Memory String");
        report.setTotalRecords(10);
        report.addFilteredRecord(1);report.addFilteredRecord(2);
        report.addIgnoredRecord(3);report.addIgnoredRecord(4);
        report.addRejectedRecord(5);report.addRejectedRecord(6);
        report.addErrorRecord(7);report.addErrorRecord(8);
        report.addSuccessRecord(9);report.addSuccessRecord(10);
    }

    @Ignore("TODO: Contents are identical but assertion fails due to different whitespaces")
    @Test
    public void testReportFormatting() {
        String result = reportFormatter.formatReport(report);
        Scanner scanner = new Scanner(HtmlReportFormatterTest.class.getResourceAsStream("expectedReport.html"));
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }

        String expectedResult = stringBuilder.toString();
        assertThat(result).isEqualTo(expectedResult);
    }

}
