#include <stdint.h>
#include <assert.h>

int32_t value = 33;
extern void set(int32_t);

int main() {
    assert(value == 33);
    set(12);
    assert(value == 12);
    set(21);
    assert(value == 21);
}