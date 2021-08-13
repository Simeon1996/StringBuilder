package Coding.StringBuilder;

import java.security.InvalidParameterException;

public class StringBuilder implements Builder {

    private final static int DEFAULT_SIZE = 10;

    private final static int RESIZING_FACTOR = 2;

    private char[] arr;

    private final static int LOAD_FACTOR = 75;

    private int index = 0;

    public StringBuilder() {
        arr = new char[DEFAULT_SIZE];
    }

    public StringBuilder(int capacity) {
        arr = new char[capacity];
    }

    public StringBuilder(String value) {
        if (value == null || value.isEmpty()) {
            throw new InvalidParameterException("Invalid value.");
        }

        char[] valueCharArr = value.toCharArray();
        int strLength = value.length();

        int desiredSize = getRequiredSize(DEFAULT_SIZE, strLength);

        arr = new char[desiredSize];

        System.arraycopy(valueCharArr, 0, arr, 0, strLength);
        updateIndex(index + strLength);
    }

    public int length() {
        return index;
    }

    @Override
    public char charAt(int position) {
        checkPositionOutOfBounds(position);

        return arr[position];
    }

    @Override
    public int getCapacity() {
        return arr.length;
    }

    @Override
    public Builder deleteCharAt(int position) {
        checkPositionOutOfBounds(position);

        shiftElementsLeft(position, index);
        updateIndex(index - 1);

        return this;
    }

    @Override
    public Builder delete(int start, int end) {
        if ((start < 0 || end < 0) || (start >= index) || (end > index)) {
            throw new IndexOutOfBoundsException("Index out ouf range.");
        }

        int elementsToDelete = end - start;

        if (end != index) {
            System.arraycopy(arr, end, arr, start, index - end);
        }

        updateIndex(index - elementsToDelete);

        return this;
    }

    @Override
    public Builder reverse() {
        int middleIndex = index / 2;

        int oppositeIndex;
        for (int i = 0; i < middleIndex; i++) {
            char tmp = arr[i];
            oppositeIndex = index - 1 - i;
            arr[i] = arr[oppositeIndex];
            arr[oppositeIndex] = tmp;
        }

        return this;
    }

    @Override
    public int lastIndexOf(String value) {
        return this.toString().lastIndexOf(value);
    }

    @Override
    public int lastIndexOf(String value, int fromIndex) {
        return this.toString().lastIndexOf(value, fromIndex);
    }

    @Override
    public int indexOf(String value) {
        return this.toString().indexOf(value);
    }

    @Override
    public int indexOf(String value, int fromIndex) {
        return this.toString().indexOf(value, fromIndex);
    }

    public StringBuilder append(char value) {
        arr[index++] = value;

        probeResizing();

        return this;
    }

    public StringBuilder append(char[] charSequence) {
        appendElements(charSequence);

        return this;
    }

    @Override
    public Builder append(char[] charSequence, int offset, int length) {
        checkIndexExceeded(offset);
        ensureEnoughSpace(length);

        if (offset < index) {
            System.arraycopy(arr, offset, arr, offset + length, index - offset);
        }

        System.arraycopy(charSequence, 0, arr, offset, length);

        updateIndex(index + length);

        return this;
    }

    private void ensureEnoughSpace(int length) {
        int size = getRequiredSize(arr.length, length);

        if (size != arr.length) {
            resizeArrayAndCopyContent(size);
        }
    }

    private void checkIndexExceeded(int offset) {
        if (offset < 0 || offset > index) {
            throw new InvalidParameterException("Index out of range.");
        }
    }

    public StringBuilder append(Object object) {
        return this.append(object.toString());
    }

    @Override
    public Builder insert(int offset, char value) {
        checkIndexExceeded(offset);

        shiftElementsRight(offset);

        arr[offset] = value;
        updateIndex(index + 1);

        probeResizing();

        return this;
    }

    @Override
    public Builder insert(int offset, char[] charSequence) {
        return this.append(charSequence, offset, charSequence.length);
    }

    @Override
    public Builder insert(int offset, String value) {
        return this.append(value.toCharArray(), offset, value.length());
    }

    @Override
    public Builder insert(int offset, int value) {
        String valueAsStr = String.valueOf(value);
        return this.append(valueAsStr.toCharArray(), offset, valueAsStr.length());
    }

    @Override
    public Builder insert(int offset, long value) {
        String valueAsStr = String.valueOf(value);
        return this.append(valueAsStr.toCharArray(), offset, valueAsStr.length());
    }

    @Override
    public Builder insert(int offset, float value) {
        String valueAsStr = String.valueOf(value);
        return this.append(valueAsStr.toCharArray(), offset, valueAsStr.length());
    }

    @Override
    public Builder insert(int offset, double value) {
        String valueAsStr = String.valueOf(value);
        return this.append(valueAsStr.toCharArray(), offset, valueAsStr.length());
    }

    @Override
    public Builder insert(int offset, boolean value) {
        char[] boolAsCharArr = getBooleanCharArrRepresentation(value);
        return this.append(boolAsCharArr, offset, boolAsCharArr.length);
    }

    @Override
    public Builder insert(int offset, Object value) {
        String objectStrRepresentation = value.toString();
        return this.append(objectStrRepresentation.toCharArray(), offset, objectStrRepresentation.length());
    }

    public StringBuilder append(boolean value) {
        char[] charArrRepresentation = getBooleanCharArrRepresentation(value);

        appendElements(charArrRepresentation);

        return this;
    }

    public StringBuilder append(int value) {
        return this.append(String.valueOf(value));
    }

    public StringBuilder append(float value) {
        return this.append(String.valueOf(value));
    }

    public StringBuilder append(double value) {
        return this.append(String.valueOf(value));
    }

    public StringBuilder append(long value) {
        return this.append(String.valueOf(value));
    }

    public StringBuilder append(String value) {
        return this.append(value.toCharArray());
    }

    @Override
    public String toString() {
        return String.valueOf(arr, 0, index);
    }

    /**
     * Append a sequence of elements, while resizing the array if required,
     * copying the elements from the old array to the new one and switching the array reference to be
     * the updated one.
     *
     * @param elements
     */
    private void appendElements(char[] elements) {
        int charArrLength = elements.length;

        ensureEnoughSpace(charArrLength);

        System.arraycopy(elements, 0, arr, index, charArrLength);

        updateIndex(index + charArrLength);
    }

    private char[] getBooleanCharArrRepresentation(boolean value) {
        char[] result;

        if (value == Boolean.TRUE) {
            result = new char[] { 't', 'r', 'u', 'e' };
        } else {
            result = new char[] { 'f', 'a', 'l', 's', 'e' };
        }

        return result;
    }

    private void probeResizing() {
        if (getPercentageOccupied(index, arr.length) >= LOAD_FACTOR) {
            resizeArrayAndCopyContent(arr.length * RESIZING_FACTOR);
        }
    }

    private int getRequiredSize(int currentLength, int arrSize) {
        int arrSpace = currentLength;

        // @TODO Optimize
        while ((arrSpace - index) < arrSize || getPercentageOccupied(index + arrSize, arrSpace) > LOAD_FACTOR) {
            arrSpace *= RESIZING_FACTOR;
        }

        return arrSpace;
    }

    private void checkPositionOutOfBounds(int position) {
        if (position < 0 || position >= index) {
            throw new IndexOutOfBoundsException("Index out of range.");
        }
    }

    private void resizeArrayAndCopyContent(int size) {
        char[] resizedArr = new char[size];
        System.arraycopy(arr, 0, resizedArr, 0, index);
        arr = resizedArr;
    }

    private long getPercentageOccupied(int elements, int totalElements) {
        return Math.round(((float) elements / (float) totalElements) * 100);
    }

    private void shiftElementsLeft(int position, int lastElementPos) {
        int nextPos;

        while (position < lastElementPos) {
            nextPos = position + 1;
            arr[position] = arr[nextPos];
            position = nextPos;
        }
    }

    private void shiftElementsRight(int position) {
        if (index - position >= 0) {
            System.arraycopy(arr, position, arr, position + 1, index - position);
        }
    }

    private void updateIndex(int size) {
        index = size;
    }
}
