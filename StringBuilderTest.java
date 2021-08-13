package Coding.StringBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.security.InvalidParameterException;

public class StringBuilderTest {

    private final static int DEFAULT_SPACE = 10;

    private StringBuilder sb;

    @Before
    public void before() {
        sb = new StringBuilder();
    }

    @Test
    public void testSizeAfterInitWithDefaultConstruct() {
        Assert.assertEquals(DEFAULT_SPACE, sb.getCapacity());
        Assert.assertEquals(0, sb.length());
    }

    @Test
    public void testSizeAfterInitWithCapacityConstruct() {
        sb = new StringBuilder(25);

        Assert.assertEquals(25, sb.getCapacity());
        Assert.assertEquals(0, sb.length());
    }

    @Test
    public void testSizeAfterInitCollectionAsParamConstruct() {
        sb = new StringBuilder("Test");

        Assert.assertEquals(10, sb.getCapacity());
        Assert.assertEquals(4, sb.length());
    }

    @Test
    public void testSizeAfterInitCollectionAsParamConstructTriggeringResizing() {
        sb = new StringBuilder("TestingResizingTestingResizingTestingResizingTestingResizingTestingResizingTestingResizing");

        // the length is 90 characters, and in order to fit this string and preserve less than 75% occupancy
        // the end size should be 160

        Assert.assertEquals(160, sb.getCapacity());
        Assert.assertEquals(90, sb.length());
    }

    @Test
    public void testAppendChar() {
        sb.append('A');

        Assert.assertEquals(1, sb.length());
        Assert.assertEquals("A", sb.toString());

        sb.append('B');

        Assert.assertEquals(2, sb.length());
        Assert.assertEquals("AB", sb.toString());

        sb.append('C');

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("ABC", sb.toString());

        sb.append('D');
        sb.append('D');
        sb.append('D');
        sb.append('D');
        sb.append('D');

        Assert.assertEquals(8, sb.length());
        Assert.assertEquals(20, sb.getCapacity());
    }

    @Test
    public void testAppendCharsAtOffset() {
        sb.append(new char[] { 'A', 'B', 'C' }, 0, 3);

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("ABC", sb.toString());

        sb.append(new char[] { 'D', 'E', 'F' }, 3, 3);

        Assert.assertEquals(6, sb.length());
        Assert.assertEquals("ABCDEF", sb.toString());

        sb.append(new char[] { 'G', 'H', 'I' }, 6, 3);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("ABCDEFGHI", sb.toString());
        Assert.assertEquals(20, sb.getCapacity());

        sb.append(new char[] { 'M', 'M', 'M', 'M' }, 0, 4);

        Assert.assertEquals(13, sb.length());
        Assert.assertEquals(20, sb.getCapacity());
        Assert.assertEquals("MMMMABCDEFGHI", sb.toString());

        sb.append(new char[] { 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R' }, 4, 12);

        Assert.assertEquals(25, sb.length());
        Assert.assertEquals(40, sb.getCapacity());
        Assert.assertEquals("MMMMRRRRRRRRRRRRABCDEFGHI", sb.toString());
    }

    @Test
    public void testInsertCharsAtOffset() {
        sb.insert(0, new char[] { 'A', 'B', 'C' });

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("ABC", sb.toString());

        sb.insert(3, new char[] { 'D', 'E', 'F' });

        Assert.assertEquals(6, sb.length());
        Assert.assertEquals("ABCDEF", sb.toString());

        sb.insert(6, new char[] { 'G', 'H', 'I' });

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("ABCDEFGHI", sb.toString());
        Assert.assertEquals(20, sb.getCapacity());

        sb.insert(0, new char[] { 'M', 'M', 'M', 'M' });

        Assert.assertEquals(13, sb.length());
        Assert.assertEquals(20, sb.getCapacity());
        Assert.assertEquals("MMMMABCDEFGHI", sb.toString());

        sb.insert(4, new char[] { 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R' });

        Assert.assertEquals(25, sb.length());
        Assert.assertEquals(40, sb.getCapacity());
        Assert.assertEquals("MMMMRRRRRRRRRRRRABCDEFGHI", sb.toString());
    }

    @Test
    public void testInsertChar() {
        sb.insert(0, 'A');

        Assert.assertEquals(1, sb.length());
        Assert.assertEquals("A", sb.toString());

        sb.insert(1, 'B');

        Assert.assertEquals(2, sb.length());
        Assert.assertEquals("AB", sb.toString());

        sb.insert(2, 'C');

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("ABC", sb.toString());

        sb.insert(0, 'D');

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("DABC", sb.toString());
        Assert.assertEquals(10, sb.getCapacity());

        sb.append('D');
        sb.append('D');
        sb.append('D');

        // 70% occupied so far

        sb.insert(1, 'G');

        Assert.assertEquals(8, sb.length());
        Assert.assertEquals("DGABCDDD", sb.toString());
        Assert.assertEquals(20, sb.getCapacity());
    }

    @Test
    public void testAppendCharArr() {
        sb.append(new char[] { 'A', 'B', 'C' });

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("ABC", sb.toString());

        sb.append(new char[] { 'D', 'E', 'F' });

        Assert.assertEquals(6, sb.length());
        Assert.assertEquals("ABCDEF", sb.toString());
        Assert.assertEquals(DEFAULT_SPACE, sb.getCapacity());

        sb.append(new char[] { 'D', 'E', 'F' });

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());
    }

    @Test
    public void testAppendString() {
        sb.append("ABC");

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("ABC", sb.toString());

        sb.append("DEF");

        Assert.assertEquals(6, sb.length());
        Assert.assertEquals("ABCDEF", sb.toString());

        sb.append("DEF");

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());
    }

    @Test
    public void testInsertString() {
        sb.insert(0, "ABC");

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("ABC", sb.toString());

        sb.insert(3, "DEF");

        Assert.assertEquals(6, sb.length());
        Assert.assertEquals("ABCDEF", sb.toString());

        sb.insert(0, "RRR");

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("RRRABCDEF", sb.toString());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());
    }

    @Test
    public void testAppendBoolean() {
        sb.append(Boolean.TRUE);

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("true", sb.toString());

        sb.append(Boolean.FALSE);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("truefalse", sb.toString());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());

        sb.append(Boolean.FALSE);

        Assert.assertEquals(14, sb.length());
        Assert.assertEquals("truefalsefalse", sb.toString());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());
    }

    @Test
    public void testInsertBoolean() {
        sb.insert(0, Boolean.TRUE);

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("true", sb.toString());

        sb.insert(4, Boolean.FALSE);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("truefalse", sb.toString());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());

        sb.insert(0, Boolean.FALSE);

        Assert.assertEquals(14, sb.length());
        Assert.assertEquals("falsetruefalse", sb.toString());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());
    }

    @Test
    public void testAppendObject() {
        sb.append(new Object() {
            @Override
            public String toString() {
                return "Dummyy";
            }
        });

        sb.append(new Object() {
            @Override
            public String toString() {
                return "Another";
            }
        });

        Assert.assertEquals(13, sb.length());
        Assert.assertEquals("DummyyAnother", sb.toString());
    }

    @Test
    public void testInsertObject() {
        sb.insert(0, new Object() {
            @Override
            public String toString() {
                return "Dummyy";
            }
        });

        sb.insert(6, new Object() {
            @Override
            public String toString() {
                return "Another";
            }
        });

        Assert.assertEquals(13, sb.length());
        Assert.assertEquals("DummyyAnother", sb.toString());
    }

    @Test
    public void testAppendInt() {
        sb.append(1);

        Assert.assertEquals(1, sb.length());
        Assert.assertEquals("1", sb.toString());

        sb.append(2);

        Assert.assertEquals(2, sb.length());
        Assert.assertEquals("12", sb.toString());

        sb.append(3);

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("123", sb.toString());

        sb.append(456);

        Assert.assertEquals(6, sb.length());
        Assert.assertEquals("123456", sb.toString());

        sb.append(789);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());
        Assert.assertEquals("123456789", sb.toString());
    }

    @Test
    public void testInsertInt() {
        sb.insert(0, 1);

        Assert.assertEquals(1, sb.length());
        Assert.assertEquals("1", sb.toString());

        sb.insert(1, 2);

        Assert.assertEquals(2, sb.length());
        Assert.assertEquals("12", sb.toString());

        sb.insert(2, 3);

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("123", sb.toString());

        sb.insert(3, 456);

        Assert.assertEquals(6, sb.length());
        Assert.assertEquals("123456", sb.toString());

        sb.insert(1, 789);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals(DEFAULT_SPACE * 2, sb.getCapacity());
        Assert.assertEquals("178923456", sb.toString());
    }

    @Test
    public void testDeleteCharAtIndex() {
        sb.append("Dummy");

        Assert.assertEquals('D', sb.charAt(0));

        sb.deleteCharAt(0);

        Assert.assertEquals('u', sb.charAt(0));
        Assert.assertEquals('y', sb.charAt(3));

        sb.deleteCharAt(3);

        Assert.assertEquals('u', sb.charAt(0));
        Assert.assertEquals('m', sb.charAt(1));
        Assert.assertEquals('m', sb.charAt(2));
    }

    @Test
    public void testDeleteCharacters() {
        sb.append("Dummu");

        sb.delete(1, 4); // -> umm deleted, remains Dy

        Assert.assertEquals(2, sb.length());
        Assert.assertEquals('D', sb.charAt(0));
        Assert.assertEquals('u', sb.charAt(1));
        Assert.assertEquals("Du", sb.toString());

        sb.append("mmy");

        Assert.assertEquals("Dummy", sb.toString());
        Assert.assertEquals(5, sb.length());

        sb.delete(0, 1);

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("ummy", sb.toString());

        sb.delete(3, 4);

        Assert.assertEquals(3, sb.length());
        Assert.assertEquals("umm", sb.toString());

        sb.delete(0, 3);

        Assert.assertEquals(0, sb.length());
        Assert.assertEquals("", sb.toString());

        sb.append("TestTestTest");

        Assert.assertEquals(12, sb.length());

        sb.delete(3, 10);

        Assert.assertEquals(5, sb.length());
        Assert.assertEquals("Tesst", sb.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteCharactersNegativeStart() {
        sb.append("Test");
        sb.delete(-2, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteCharactersNegativeEnd() {
        sb.append("Test");
        sb.delete(0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteCharactersNegativeStartAndEnd() {
        sb.append("Test");
        sb.delete(-1, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteCharactersStartOutOfRangeWith1Position() {
        sb.append("Test");
        sb.delete(4, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteCharactersStartOutOfRangeWithManyPositions() {
        sb.append("Test");
        sb.delete(25, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteCharactersEndOutOfRangeWith1Position() {
        sb.append("Test");
        sb.delete(0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDeleteCharactersEndOutOfRangeWithManyPositions() {
        sb.append("Test");
        sb.delete(0, 25);
    }

    @Test
    public void testGetCharAt() {
        sb.append("Hello");

        Assert.assertEquals('H', sb.charAt(0));
        Assert.assertEquals('l', sb.charAt(2));
        Assert.assertEquals('o', sb.charAt(4));
    }

    @Test
    public void testIndexOf() {
        sb.append("Hello");

        Assert.assertEquals(2, sb.indexOf("llo"));
        Assert.assertEquals(0, sb.indexOf("Hello"));
        Assert.assertEquals(4, sb.indexOf("o"));
        Assert.assertEquals(2, sb.indexOf("l"));
    }

    @Test
    public void testIndexOfFromPosition() {
        sb.append("HelloHello");

        Assert.assertEquals(3, sb.indexOf("l", 3));
        Assert.assertEquals(0, sb.indexOf("H", 0));
        Assert.assertEquals(5, sb.indexOf("H", 1));
    }

    @Test
    public void testLastIndexOf() {
        sb.append("HelloHello");

        Assert.assertEquals(9, sb.lastIndexOf("o"));
        Assert.assertEquals(5, sb.lastIndexOf("H"));
        Assert.assertEquals(5, sb.lastIndexOf("Hel"));
    }

    @Test
    public void testLastIndexOfFromPosition() {
        sb.append("HelloHello");

        Assert.assertEquals(8, sb.lastIndexOf("l", 9));
        Assert.assertEquals(7, sb.lastIndexOf("l", 7));
        Assert.assertEquals(7, sb.lastIndexOf("llo", 9));
        Assert.assertEquals(2, sb.lastIndexOf("llo", 6));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementOutOfRangeNegative() {
        sb.charAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetElementOutOfRangePositive() {
        sb.charAt(24);
    }

    @Test
    public void testAppendFloat() {
        sb.append((float) 1.21);

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("1.21", sb.toString());

        sb.append((float) 2.201);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("1.212.201", sb.toString());

        sb.append((float) 3.58971);

        Assert.assertEquals(16, sb.length());
        Assert.assertEquals("1.212.2013.58971", sb.toString());
    }

    @Test
    public void testInsertFloat() {
        sb.insert(0, (float) 1.21);

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("1.21", sb.toString());

        sb.insert(4, (float) 2.201);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("1.212.201", sb.toString());

        sb.insert(1, (float) 3.58971);

        Assert.assertEquals(16, sb.length());
        Assert.assertEquals("13.58971.212.201", sb.toString());
    }

    @Test
    public void testAppendDouble() {
        sb.append(1.21);

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("1.21", sb.toString());

        sb.append(2.201);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("1.212.201", sb.toString());

        sb.append(3.58971);

        Assert.assertEquals(16, sb.length());
        Assert.assertEquals("1.212.2013.58971", sb.toString());
    }

    @Test
    public void testInsertDouble() {
        sb.insert(0, 1.21);

        Assert.assertEquals(4, sb.length());
        Assert.assertEquals("1.21", sb.toString());

        sb.insert(4, 2.201);

        Assert.assertEquals(9, sb.length());
        Assert.assertEquals("1.212.201", sb.toString());

        sb.insert(3, 3.58971);

        Assert.assertEquals(16, sb.length());
        Assert.assertEquals("1.23.5897112.201", sb.toString());
    }

    @Test
    public void testToStringWithNotEmptyInputConstruct() {
        sb = new StringBuilder("Hello");

        Assert.assertEquals("Hello", sb.toString());
    }

    @Test(expected = InvalidParameterException.class)
    public void testToStringWithEmptyInputConstruct() {
        sb = new StringBuilder("");
    }

    @Test(expected = InvalidParameterException.class)
    public void testToStringWithNullInputConstruct() {
        sb = new StringBuilder(null);
    }

    @Test
    public void testToStringWithEmptyValue() {
        sb = new StringBuilder();

        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testReverse() {
        sb = new StringBuilder("Dummy");

        Assert.assertEquals("ymmuD", sb.reverse().toString());
    }

    @Test
    public void testReverseOneCharacter() {
        sb.append('A');

        Assert.assertEquals("A", sb.reverse().toString());
    }

    @Test
    public void testEmptyReverse() {
        Assert.assertEquals("", sb.reverse().toString());
    }

    @Test
    public void testReverseTwoCharacters() {
        sb.append('A');
        sb.append('B');

        Assert.assertEquals("BA", sb.reverse().toString());
    }
}
