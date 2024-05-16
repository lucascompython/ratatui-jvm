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

        try (var terminal = new Terminal()) {
            terminal.enableRawMode();
            terminal.enterAlternateScreen();


            while (!terminal.handleEvents()) {
                terminal.draw("Hello Ratatui from java! (Press 'q' to exit)");
            }

            terminal.leaveAlternateScreen();
            terminal.disableRawMode();
        }
    }
}