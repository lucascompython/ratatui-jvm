use std::io::Stdout;
use std::sync::{Arc, Mutex};

use ratatui::backend::CrosstermBackend;
use ratatui::Terminal;

lazy_static::lazy_static! {
    pub static ref TERMINAL: Arc<Mutex<Option<Terminal<CrosstermBackend<Stdout>>>>> = Arc::new(Mutex::new(None));
}
