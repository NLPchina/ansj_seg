package org.ansj.recognition.impl;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Assert;
import org.junit.Test;


/**
 * DESC: 千分位格式数字识别单元测试
 *
 * @author baicaixiaozhan
 * @since v5.1.6
 */
public class ThousandsSeparatorRecognitionTest {

    @Test
    public void test_ThousandsSeparatorRecognition_whenThousandsSeparatorExisted() {
        final String WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT = "当日访问量为10,234,543 10000.00。是预期结果";

        Result result = ToAnalysis.parse(WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT).recognition(new ThousandsSeparatorRecognition());

        Assert.assertEquals("10,234,543", result.get(3).getName());
        Assert.assertEquals("10,234,543/thousands_separator", result.get(3).toString());
        Assert.assertEquals("10000.00", result.get(5).getName());
        Assert.assertEquals("10000.00/m", result.get(5).toString());
    }

    @Test
    public void test_ThousandsSeparatorRecognition_whenUseCustomSeparator() {
        final String WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT = "10，234，543 102_234_543.00";

        Result result = ToAnalysis.parse(WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT)
                .recognition(new ThousandsSeparatorRecognition("，"))
                .recognition(new ThousandsSeparatorRecognition("_"));

        Assert.assertEquals("10，234，543", result.get(0).getName());
        Assert.assertEquals("10，234，543/thousands_separator", result.get(0).toString());
        Assert.assertEquals("102_234_543.00", result.get(2).getName());
        Assert.assertEquals("102_234_543.00/thousands_separator", result.get(2).toString());
    }

    @Test
    public void test_ThousandsSeparatorRecognition_whenThousandsSeparatorIsError() {
        Result result1 = ToAnalysis.parse("10,234,5430").recognition(new ThousandsSeparatorRecognition());
        Assert.assertEquals("10,234 | , | 5430", result1.toStringWithOutNature(" | "));

        Result result2 = ToAnalysis.parse("1088,234,5430").recognition(new ThousandsSeparatorRecognition());
        Assert.assertEquals("1088 | , | 234 | , | 5430", result2.toStringWithOutNature(" | "));

        Result result3 = ToAnalysis.parse("108,234,5430.00").recognition(new ThousandsSeparatorRecognition());
        Assert.assertEquals("108,234 | , | 5430.00", result3.toStringWithOutNature(" | "));

        Result result4 = ToAnalysis.parse("108,234.00,430.00").recognition(new ThousandsSeparatorRecognition());
        Assert.assertEquals("108,234.00 | , | 430.00", result4.toStringWithOutNature(" | "));
    }

    @Test
    public void test_ThousandsSeparatorRecognition_whenThousandsSeparatorInEnd() {
        final String WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT = "存在金额：100,234,543.00元";

        Result result = ToAnalysis.parse(WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT).recognition(new ThousandsSeparatorRecognition());

        Assert.assertEquals("100,234,543.00元", result.get(3).getName());
    }

    @Test
    public void test_ThousandsSeparatorRecognition_whenThousandsSeparatorInStart() {
        final String WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT = "100,234,543.00是预期结果";

        Result result = ToAnalysis.parse(WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT).recognition(new ThousandsSeparatorRecognition());

        Assert.assertEquals("100,234,543.00", result.get(0).getName());
    }

    @Test
    public void test_ThousandsSeparatorRecognition_whenThousandsSeparatorInCenter() {
        final String WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT = "当日访问量为10,234,543。是预期结果";

        Result result = ToAnalysis.parse(WITH_THOUSANDS_SEPARATOR_NUMBER_TEXT).recognition(new ThousandsSeparatorRecognition());

        Assert.assertEquals("10,234,543", result.get(3).getName());
    }

}
