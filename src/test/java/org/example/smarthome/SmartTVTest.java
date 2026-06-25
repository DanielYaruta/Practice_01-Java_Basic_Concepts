package org.example.smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmartTVTest {

    private SmartTV tv;

    @BeforeEach
    void setUp() {
        tv = new SmartTV("TV", RoomType.LIVING_ROOM);
        tv.turnOn();
    }

    // --- Initial state ---

    @Test
    void initialVolume_is20() {
        SmartTV fresh = new SmartTV("New", RoomType.BEDROOM);
        assertEquals(20, fresh.getVolume());
    }

    @Test
    void initialChannel_is1() {
        SmartTV fresh = new SmartTV("New", RoomType.BEDROOM);
        assertEquals(1, fresh.getChannel());
    }

    // --- increaseValue (volume) ---

    @Test
    void increaseValue_addsFive() {
        tv.increaseValue();
        assertEquals(25, tv.getVolume());
    }

    @Test
    void increaseValue_atMax_staysAt100() {
        for (int i = 0; i < 20; i++) tv.increaseValue();
        assertEquals(100, tv.getVolume());
    }

    @Test
    void increaseValue_whenOff_doesNotChange() {
        tv.turnOff();
        tv.increaseValue();
        assertEquals(20, tv.getVolume());
    }

    // --- decreaseValue (volume) ---

    @Test
    void decreaseValue_subtractsFive() {
        tv.decreaseValue();
        assertEquals(15, tv.getVolume());
    }

    @Test
    void decreaseValue_atMin_staysAt0() {
        for (int i = 0; i < 10; i++) tv.decreaseValue();
        assertEquals(0, tv.getVolume());
    }

    @Test
    void decreaseValue_whenOff_doesNotChange() {
        tv.turnOff();
        tv.decreaseValue();
        assertEquals(20, tv.getVolume());
    }

    // --- boundary: volume ---

    @Test
    void increaseValue_doesNotExceedMax() {
        for (int i = 0; i < 16; i++) tv.increaseValue(); // 20 + 80 = 100
        assertEquals(100, tv.getVolume());
        tv.increaseValue();
        assertEquals(100, tv.getVolume());
    }

    @Test
    void decreaseValue_doesNotGoBelowMin() {
        for (int i = 0; i < 4; i++) tv.decreaseValue(); // 20 - 20 = 0
        assertEquals(0, tv.getVolume());
        tv.decreaseValue();
        assertEquals(0, tv.getVolume());
    }

    // --- nextChannel ---

    @Test
    void nextChannel_incrementsByOne() {
        tv.nextChannel();
        assertEquals(2, tv.getChannel());
    }

    @Test
    void nextChannel_multipleSteps() {
        tv.nextChannel();
        tv.nextChannel();
        tv.nextChannel();
        assertEquals(4, tv.getChannel());
    }

    @Test
    void nextChannel_whenOff_doesNotChange() {
        tv.turnOff();
        tv.nextChannel();
        assertEquals(1, tv.getChannel());
    }

    // --- prevChannel ---

    @Test
    void prevChannel_decrementsbyOne() {
        tv.nextChannel();
        tv.nextChannel();
        tv.prevChannel();
        assertEquals(2, tv.getChannel());
    }

    @Test
    void prevChannel_atChannel1_staysAt1() {
        tv.prevChannel();
        assertEquals(1, tv.getChannel());
    }

    @Test
    void prevChannel_whenOff_doesNotChange() {
        tv.nextChannel();
        tv.turnOff();
        tv.prevChannel();
        assertEquals(2, tv.getChannel());
    }

    // --- getStatus ---

    @Test
    void getStatus_containsVolumeAndChannel() {
        String status = tv.getStatus();
        assertTrue(status.contains("20"));
        assertTrue(status.contains("1"));
    }

    @Test
    void getStatus_updatesAfterVolumeChange() {
        tv.increaseValue();
        assertTrue(tv.getStatus().contains("25"));
    }

    @Test
    void getStatus_updatesAfterChannelChange() {
        tv.nextChannel();
        assertTrue(tv.getStatus().contains("2"));
    }
}
