def section(String title) {
    echo ""
    echo "--------------------------------------------------"
    echo title
    echo "--------------------------------------------------"
}

def info(String message) {
    echo "[INFO] ${message}"
}

def warn(String message) {
    echo "[WARN] ${message}"
}

def kv(String key, def value) {
    echo "${key.padRight(16)}: ${value}"
}

def banner(String title, Map fields) {
    echo ""
    echo "========================================"
    echo title
    echo "========================================"
    fields.each { k, v -> kv(k, v) }
    echo "========================================"
}

return this