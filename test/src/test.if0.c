#include <stdint.h>
#include <stdbool.h>
#include <assert.h>

extern int32_t f(int32_t, int32_t, bool);

int main() {
    assert(f(12, 21, 1) == 33);
    assert(f(12, 21, 0) == -9);
}