package Coding.StringBuilder;

public interface Builder {

    Builder append(char value);

    Builder append(char[] charSequence);

    Builder append(char[] charSequence, int offset, int length);

    Builder append(String value);

    Builder append(int value);

    Builder append(long value);

    Builder append(float value);

    Builder append(double value);

    Builder append(Object object);

    Builder insert(int offset, char value);

    Builder insert(int offset, char[] charSequence);

    Builder insert(int offset, String value);

    Builder insert(int offset, int value);

    Builder insert(int offset, long value);

    Builder insert(int offset, float value);

    Builder insert(int offset, double value);

    Builder insert(int offset, boolean value);

    Builder insert(int offset, Object value);

    int length();

    char charAt(int position);

    int getCapacity();

    Builder deleteCharAt(int position);

    Builder delete(int start, int end);

    Builder reverse();

    int lastIndexOf(String value);

    int lastIndexOf(String value, int fromIndex);

    int indexOf(String value);

    int indexOf(String value, int fromIndex);

    // @TODO more..
}
