package org.ratatui;

import static org.ratatui.bindings.bindings_h.*;
import org.ratatui.bindings.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(add(1, 2));

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment string = arena.allocateFrom("Jhon Doe");
            MemorySegment greeted = greet(string);

            System.out.println(greeted.getString(0));
        }

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment string = arena.allocateFrom("Hello Ratatui from Java! (press 'q' to quit)");

            new_terminal();
            enable_raw_mode();
            enter_alternate_screen();
            while (true) {
                terminal_draw(string);
                if (handle_events()) {
                    break;
                }
            }
            leave_alternate_screen();
            disable_raw_mode();
            drop_terminal();
        }

    }
}