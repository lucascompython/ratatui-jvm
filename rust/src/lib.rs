use std::ffi::{c_char, CStr, CString};
use std::io::{stdout, Stdout};
use std::sync::{Arc, Mutex};

lazy_static::lazy_static! {
static ref TERMINAL: Arc<Mutex<Option<Terminal<CrosstermBackend<Stdout>>>>> = Arc::new(Mutex::new(None));

}

use crossterm::{
    event::{self, KeyCode, KeyEventKind},
    terminal::{
        disable_raw_mode as _disable_raw_mode, enable_raw_mode as _enable_raw_mode,
        EnterAlternateScreen, LeaveAlternateScreen,
    },
    ExecutableCommand,
};
use ratatui::{
    prelude::{CrosstermBackend, Stylize, Terminal},
    widgets::Paragraph,
};

#[no_mangle]
pub extern "C" fn add(a: i32, b: i32) -> i32 {
    println!("Hello from Rust!");
    a + b
}

#[no_mangle]
pub extern "C" fn greet(name: *const c_char) -> *mut c_char {
    let c_str = unsafe { CStr::from_ptr(name) };
    let name = c_str.to_str().expect("Not a valid UTF-8 string");
    let rust_greeting = format!("Hello, {}!", name);
    CString::new(rust_greeting).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn enter_alternate_screen() {
    stdout().execute(EnterAlternateScreen).unwrap();
}

#[no_mangle]
pub extern "C" fn leave_alternate_screen() {
    stdout().execute(LeaveAlternateScreen).unwrap();
}

#[no_mangle]
pub extern "C" fn enable_raw_mode() {
    _enable_raw_mode().unwrap();
}

#[no_mangle]
pub extern "C" fn disable_raw_mode() {
    _disable_raw_mode().unwrap();
}

#[no_mangle]
pub extern "C" fn new_terminal() {
    let backend = CrosstermBackend::new(stdout());
    let terminal = Terminal::new(backend).unwrap();

    let mut global_terminal = TERMINAL.lock().unwrap();
    *global_terminal = Some(terminal);
}

#[no_mangle]
pub extern "C" fn terminal_draw(string: *const c_char) {
    let c_str = unsafe { CStr::from_ptr(string) };
    let string = c_str.to_str().expect("Not a valid UTF-8 string");

    let mut global_terminal = TERMINAL.lock().unwrap();
    let terminal = global_terminal.as_mut().unwrap();

    terminal
        .draw(|frame| {
            let area = frame.size();
            frame.render_widget(Paragraph::new(string).white().on_blue(), area);
        })
        .unwrap();
}

#[no_mangle]
pub extern "C" fn handle_events() -> bool {
    if event::poll(std::time::Duration::from_millis(16)).unwrap() {
        if let event::Event::Key(key) = event::read().unwrap() {
            if key.kind == KeyEventKind::Press && key.code == KeyCode::Char('q') {
                return true;
            }
        }
    }
    false
}

#[no_mangle]
pub extern "C" fn drop_terminal() -> bool {
    let mut global_terminal = TERMINAL.lock().unwrap();
    global_terminal.take();
    true
}
