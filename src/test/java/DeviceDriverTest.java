import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceDriverTest {
    DeviceDriver driver;

    @Mock
    FlashMemoryDevice hardware;

    @BeforeEach
    void setUp() {
        driver = new DeviceDriver(hardware);
    }

    @Test
    public void read_From_Hardware() {
        // TODO: replace hardware with a Test Double
        byte data = driver.read(0xFF);
        assertEquals(0, data);
    }

    @Test
    public void read_5_times_when_consistent() {
        byte data = driver.read(0xFF);
        verify(hardware, times(5)).read(0xFF);
    }

    @Test
    public void read_5_times_when_inconsistent() {
        when(hardware.read(0xFF))
                .thenReturn((byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x02);

        assertThatThrownBy(() -> driver.read(0xFF))
                .isInstanceOf(DeviceDriver.ReadFailException.class);
    }

    @Test
    public void write_From_Hardware() {
        when(hardware.read(0xFF))
                .thenReturn((byte) 0xFF);

        driver.write(0xFF, (byte) 0x01);
        verify(hardware).write(0xFF, (byte) 0x01);
    }

    @Test
    public void write_From_Hardware_whether_read() {
        when(hardware.read(0xFF))
                .thenReturn((byte) 0xFF);

        driver.write(0xFF, (byte) 0x01);
        verify(hardware).read(0xFF);
    }

    @Test
    public void write_when_not_empty() {
        when(hardware.read(0xFF))
                .thenReturn((byte) 0x01);

        assertThatThrownBy(() -> driver.write(0xFF, (byte) 0x01))
                .isInstanceOf(DeviceDriver.WriteFailException.class);
    }
}
