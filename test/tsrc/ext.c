#include <stdint.h>
#include <assert.h>

extern int32_t f(int32_t, int32_t);

int32_t add(int32_t a, int32_t b) {
    return a + b;
}

int main() {
    assert(f(12, 21) == 33);
}