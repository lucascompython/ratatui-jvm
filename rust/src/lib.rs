use std::ffi::{c_char, CStr, CString};

mod global;
mod terminal;

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
