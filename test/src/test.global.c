#include <stdint.h>
#include <assert.h>

extern void set(int32_t);
extern int32_t get();

int main() {
    set(12);
    assert(get() == 12);
    set(21);
    assert(get() == 21);
}