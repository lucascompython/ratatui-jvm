#ifndef BINDINGS_H
#define BINDINGS_H

#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

int32_t add(int32_t a, int32_t b);

char *greet(const char *name);

void enter_alternate_screen(void);

void leave_alternate_screen(void);

void enable_raw_mode(void);

void disable_raw_mode(void);

void new_terminal(void);

void terminal_draw(const char *string);

bool handle_events(void);

bool drop_terminal(void);

#endif /* BINDINGS_H */
