/**
 * This class is used by the operating system to interact with the hardware 'FlashMemoryDevice'.
 */
public class DeviceDriver {
    FlashMemoryDevice hw;

    public DeviceDriver(FlashMemoryDevice hardware) {
        hw = hardware;
    }

    public byte read(long address) {
        byte result = checkDataConsistency(address);
        return result;
    }

    private byte checkDataConsistency(long address) {
        byte result = 0;
        for (int i = 0; i < 5; i++) {
            byte current = hw.read(address);

            if (i != 0) {
                if (current != result) {
                    throw new ReadFailException("Read error: inconsistent data read from hardware.");
                }
            }

            result = current;
        }
        return result;
    }

    public void write(long address, byte data) {
        checkDataEmptyness(address);
        hw.write(address, data);
    }

    private void checkDataEmptyness(long address) {
        if (hw.read(address) != (byte) 0xFF) {
            throw new WriteFailException("Write error: hardware is not in a writable state.");
        }
    }

    static class ReadFailException extends RuntimeException {
        public ReadFailException(String s) {
            super(s);
        }
    }

    static class WriteFailException extends RuntimeException {
        public WriteFailException(String s) {
            super(s);
        }
    }
}
