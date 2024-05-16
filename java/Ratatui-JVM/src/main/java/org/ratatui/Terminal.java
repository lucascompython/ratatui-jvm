package org.ratatui;


import static org.ratatui.bindings.bindings_h.*;
import org.ratatui.bindings.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;


public final class Terminal implements AutoCloseable {

    public Terminal() {
        new_terminal();
    }

    public void enableRawMode() {
        enable_raw_mode();
    }

    public void enterAlternateScreen() {
        enter_alternate_screen();
    }

    public void leaveAlternateScreen() {
        leave_alternate_screen();
    }

    public void disableRawMode() {
        disable_raw_mode();
    }

    public void dropTerminal() {
        drop_terminal();
    }

    public void draw(String string) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment stringSegment = arena.allocateFrom(string);
            terminal_draw(stringSegment);
        }
    }

    public boolean handleEvents() {
        return handle_events();
    }

    @Override
    public void close() {
        dropTerminal();
    }
}
